package adrianliz.shared.infrastructure.bus.event.rabbit;

import adrianliz.shared.infrastructure.bus.event.DomainEventSubscribersInformation;
import adrianliz.shared.infrastructure.bus.event.DomainEventsInformation;
import adrianliz.shared.infrastructure.config.Parameter;
import adrianliz.shared.infrastructure.config.ParameterNotExist;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqEventBusConfiguration {
  private final DomainEventSubscribersInformation domainEventSubscribersInformation;
  private final DomainEventsInformation domainEventsInformation;
  private final Parameter config;
  private final String exchangeName;

  public RabbitMqEventBusConfiguration(
      final DomainEventSubscribersInformation domainEventSubscribersInformation,
      final DomainEventsInformation domainEventsInformation,
      final Parameter config)
      throws ParameterNotExist {
    this.domainEventSubscribersInformation = domainEventSubscribersInformation;
    this.domainEventsInformation = domainEventsInformation;
    this.config = config;
    exchangeName = config.get("RABBITMQ_EXCHANGE");
  }

  @Bean
  public CachingConnectionFactory connection() throws ParameterNotExist {
    final CachingConnectionFactory factory = new CachingConnectionFactory();

    factory.setHost(config.get("RABBITMQ_HOST"));
    factory.setPort(config.getInt("RABBITMQ_PORT"));
    factory.setUsername(config.get("RABBITMQ_LOGIN"));
    factory.setPassword(config.get("RABBITMQ_PASSWORD"));

    return factory;
  }

  @Bean
  public Declarables declaration() {
    final String retryExchangeName = RabbitMqExchangeNameFormatter.retry(exchangeName);
    final String deadLetterExchangeName = RabbitMqExchangeNameFormatter.deadLetter(exchangeName);

    final TopicExchange domainEventsExchange = new TopicExchange(exchangeName, true, false);
    final TopicExchange retryDomainEventsExchange =
        new TopicExchange(retryExchangeName, true, false);
    final TopicExchange deadLetterDomainEventsExchange =
        new TopicExchange(deadLetterExchangeName, true, false);
    final List<Declarable> declarables = new ArrayList<>();
    declarables.add(domainEventsExchange);
    declarables.add(retryDomainEventsExchange);
    declarables.add(deadLetterDomainEventsExchange);

    final Collection<Declarable> queuesAndBindings =
        declareQueuesAndBindings(
                domainEventsExchange, retryDomainEventsExchange, deadLetterDomainEventsExchange)
            .stream()
            .flatMap(Collection::stream)
            .toList();

    declarables.addAll(queuesAndBindings);

    return new Declarables(declarables);
  }

  private Collection<Collection<Declarable>> declareQueuesAndBindings(
      final TopicExchange domainEventsExchange,
      final TopicExchange retryDomainEventsExchange,
      final TopicExchange deadLetterDomainEventsExchange) {
    return domainEventSubscribersInformation.all().stream()
        .map(
            information -> {
              final String queueName = RabbitMqQueueNameFormatter.format(information);
              final String retryQueueName = RabbitMqQueueNameFormatter.formatRetry(information);
              final String deadLetterQueueName =
                  RabbitMqQueueNameFormatter.formatDeadLetter(information);

              final Queue queue = QueueBuilder.durable(queueName).build();
              final Queue retryQueue =
                  QueueBuilder.durable(retryQueueName)
                      .withArguments(retryQueueArguments(domainEventsExchange, queueName))
                      .build();
              final Queue deadLetterQueue = QueueBuilder.durable(deadLetterQueueName).build();

              final Binding fromExchangeSameQueueBinding =
                  BindingBuilder.bind(queue).to(domainEventsExchange).with(queueName);

              final Binding fromRetryExchangeSameQueueBinding =
                  BindingBuilder.bind(retryQueue).to(retryDomainEventsExchange).with(queueName);

              final Binding fromDeadLetterExchangeSameQueueBinding =
                  BindingBuilder.bind(deadLetterQueue)
                      .to(deadLetterDomainEventsExchange)
                      .with(queueName);

              final List<Binding> fromExchangeDomainEventsBindings =
                  information.subscribedEvents().stream()
                      .map(
                          domainEventClass -> {
                            final String eventName =
                                domainEventsInformation.forClass(domainEventClass);
                            return BindingBuilder.bind(queue)
                                .to(domainEventsExchange)
                                .with(eventName);
                          })
                      .toList();

              final List<Declarable> queuesAndBindings = new ArrayList<>();
              queuesAndBindings.add(queue);
              queuesAndBindings.add(fromExchangeSameQueueBinding);
              queuesAndBindings.addAll(fromExchangeDomainEventsBindings);

              queuesAndBindings.add(retryQueue);
              queuesAndBindings.add(fromRetryExchangeSameQueueBinding);

              queuesAndBindings.add(deadLetterQueue);
              queuesAndBindings.add(fromDeadLetterExchangeSameQueueBinding);

              return queuesAndBindings;
            })
        .collect(Collectors.toList());
  }

  private HashMap<String, Object> retryQueueArguments(
      final TopicExchange exchange, final String routingKey) {
    return new HashMap<>() {
      {
        put("x-dead-letter-exchange", exchange.getName());
        put("x-dead-letter-routing-key", routingKey);
        put("x-message-ttl", 1000);
      }
    };
  }
}
