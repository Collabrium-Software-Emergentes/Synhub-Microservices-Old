package com.collabrium.iam.authentication.application.internal.commandservices;

import com.collabrium.iam.authentication.application.internal.outboundservices.hashing.HashingService;
import com.collabrium.iam.authentication.application.internal.outboundservices.email.EmailService;
import com.collabrium.iam.authentication.application.internal.outboundservices.messaging.IamEventPublisher;
import com.collabrium.iam.authentication.application.internal.outboundservices.tokens.TokenService;
import com.collabrium.iam.authentication.domain.exceptions.DifferentPasswordException;
import com.collabrium.iam.authentication.domain.exceptions.EmailAlreadyExistsException;
import com.collabrium.iam.authentication.domain.exceptions.FileEmptyOrNullException;
import com.collabrium.iam.authentication.domain.exceptions.ImageUploadException;
import com.collabrium.iam.authentication.domain.exceptions.InvalidPasswordException;
import com.collabrium.iam.authentication.domain.exceptions.InvalidTokenException;
import com.collabrium.iam.authentication.domain.exceptions.RoleNotFoundException;
import com.collabrium.iam.authentication.domain.exceptions.UserNotActiveException;
import com.collabrium.iam.authentication.domain.exceptions.UserNotFoundException;
import com.collabrium.iam.authentication.domain.exceptions.UserNotVerifiedException;
import com.collabrium.iam.authentication.domain.exceptions.UsernameAlreadyExistsException;
import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.authentication.domain.model.commands.UpdateUserLeaderIdCommand;
import com.collabrium.iam.authentication.domain.model.commands.UpdateUserMemberIdCommand;
import com.collabrium.iam.authentication.domain.model.commands.SignInCommand;
import com.collabrium.iam.authentication.domain.model.commands.SignUpCommand;
import com.collabrium.iam.authentication.domain.model.commands.VerifyUserCommand;
import com.collabrium.iam.authentication.domain.model.events.UserLeaderCreatedEvent;
import com.collabrium.iam.authentication.domain.model.events.UserMemberCreatedEvent;
import com.collabrium.iam.authentication.domain.model.valueobjects.LeaderId;
import com.collabrium.iam.authentication.domain.model.valueobjects.MemberId;
import com.collabrium.iam.authentication.domain.model.valueobjects.Roles;
import com.collabrium.iam.authentication.domain.services.UserCommandService;
import com.collabrium.iam.authentication.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.collabrium.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.collabrium.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;
  private final IamEventPublisher iamEventPublisher;
  private final EmailService emailService;

  public UserCommandServiceImpl(RoleRepository roleRepository,
                                UserRepository userRepository,
                                HashingService hashingService,
                                TokenService tokenService,
                                IamEventPublisher iamEventPublisher,
                                EmailService emailService
  ) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
    this.iamEventPublisher = iamEventPublisher;
    this.emailService = emailService;
  }

  /**
   * Handle the sign-in command
   * <p>
   *     This method handles the {@link SignInCommand} command and returns the user and the token.
   * </p>
   * @param command the sign-in command containing the username and password
   * @return and optional containing the user matching the username and the generated token
   * @throws RuntimeException if the user is not found or the password is invalid
   */
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {

    var user = userRepository.findByUsernameWithRoles(command.username());

    if (user.isEmpty())
      throw new UserNotFoundException(command.username());

    if (!hashingService.matches(command.password(), user.get().getPassword()))
      throw new InvalidPasswordException();

    if (!user.get().isVerified()) {
      throw new UserNotVerifiedException(user.get().getUsername());
    }

    if (!user.get().isActive()) {
      throw new UserNotActiveException(user.get().getUsername());
    }

    UserDetailsImpl userDetails = UserDetailsImpl.build(user.get());

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );

    var token = tokenService.generateToken(authentication);

    return Optional.of(ImmutablePair.of(user.get(), token));
  }

  /**
   * Handle the sign-up command
   * <p>
   *     This method handles the {@link SignUpCommand} command and returns the user.
   * </p>
   * @param command the sign-up command containing the username and password
   * @return the created user
   */
  @Override
  @Transactional
  public Optional<User> handle(SignUpCommand command) {

    if (userRepository.existsByUsername(command.username()))
      throw new UsernameAlreadyExistsException(command.username());

    if(userRepository.existsByEmail(command.email()))
      throw new EmailAlreadyExistsException(command.email());

    var roles = command.roles().stream()
        .map(role ->
            roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RoleNotFoundException(role.getName().name())))
        .toList();

    var user = new User(
        command.username(),
        command.name(),
        command.surname(),
        command.imgUrl(),
        command.email(),
        hashingService.encode(command.password()),
        roles);

    userRepository.save(user);

    var token = tokenService.generateToken(user.getUsername());
    var verificationLink = command.baseUrl() + "/verification/complete?token=" + token;
    emailService.sendVerificationEmail(user.getEmail(), verificationLink);

    var savedUser = userRepository.findByUsernameWithRoles(command.username())
        .orElseThrow();

    publishDomainEvents(savedUser);

    return Optional.of(savedUser);
  }

  @Override
  @Transactional
  public Optional<User> handle(VerifyUserCommand command) {
    if (!tokenService.validateToken(command.token())) {
      throw new InvalidTokenException();
    }

    var username = tokenService.getUsernameFromToken(command.token());
    var user = userRepository.findByUsernameWithRoles(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    user.markAsVerified();
    userRepository.save(user);
    return Optional.of(user);
  }

  @Override
  public void handle(UpdateUserLeaderIdCommand command) {

    updateUser(
        command.userId(),
        user -> user.setLeaderId(
            new LeaderId(command.leaderId())
        )
    );
  }

  @Override
  public void handle(UpdateUserMemberIdCommand command) {

    updateUser(
        command.userId(),
        user -> user.setMemberId(
            new MemberId(command.memberId())
        )
    );
  }

  private void publishDomainEvents(User user) {

    boolean isLeader = user.getRoles().stream()
        .anyMatch(r -> r.getName() == Roles.ROLE_LEADER);

    if (isLeader) {
      iamEventPublisher.publishUserLeaderCreated(
          new UserLeaderCreatedEvent(user.getId())
      );
    }

    boolean isMember = user.getRoles().stream()
        .anyMatch(r -> r.getName() == Roles.ROLE_MEMBER);

    if (isMember) {
      iamEventPublisher.publishUserMemberCreated(
          new UserMemberCreatedEvent(user.getId())
      );
    }
  }

  private void updateUser(
      Long userId,
      Consumer<User> updater
  ) {

    var user = userRepository.findById(userId)
        .orElseThrow(() ->
            new UserNotFoundException(userId));

    updater.accept(user);

    userRepository.save(user);
  }
}