package com.collabrium.requests.shared.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  // =========================
  // EXCHANGE
  // =========================
  public static final String REQUESTS_EXCHANGE = "requests.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String REQUEST_CREATED_KEY = "request.created";

  // =========================
  // EXCHANGE BEANS
  // =========================
  @Bean
  public TopicExchange requestsExchange() {
    return new TopicExchange(REQUESTS_EXCHANGE);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
