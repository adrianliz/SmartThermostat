package adrianliz.shared.infrastructure.bus.event.rabbit;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.event.DomainEvent;
import adrianliz.shared.infrastructure.bus.event.DomainEventJsonDeserializer;
import adrianliz.shared.infrastructure.bus.event.DomainEventSubscribersInformation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.ApplicationContext;

@Service
public final class RabbitMqDomainEventsConsumer {
  private static final String CONSUMER_NAME = "domain_events_consumer";
  private static final int MAX_RETRIES = 2;

  private final DomainEventJsonDeserializer deserializer;
  private final ApplicationContext context;
  private final RabbitMqPublisher publisher;
  private final HashMap<String, Object> domainEventSubscribers = new HashMap<>();
  RabbitListenerEndpointRegistry registry;
  private DomainEventSubscribersInformation information;

  public RabbitMqDomainEventsConsumer(
      final RabbitListenerEndpointRegistry registry,
      final DomainEventSubscribersInformation information,
      final DomainEventJsonDeserializer deserializer,
      final ApplicationContext context,
      final RabbitMqPublisher publisher) {
    this.registry = registry;
    this.information = information;
    this.deserializer = deserializer;
    this.context = context;
    this.publisher = publisher;
  }

  public void consume() {
    final AbstractMessageListenerContainer container =
        (AbstractMessageListenerContainer) registry.getListenerContainer(CONSUMER_NAME);

    container.addQueueNames(information.rabbitMqFormattedNames());

    container.start();
  }

  @RabbitListener(id = CONSUMER_NAME, autoStartup = "false")
  public void consumer(final Message message) throws Exception {
    final String serializedMessage = new String(message.getBody());
    final DomainEvent domainEvent = deserializer.deserialize(serializedMessage);

    final String queue = message.getMessageProperties().getConsumerQueue();

    final Object subscriber =
        domainEventSubscribers.containsKey(queue)
            ? domainEventSubscribers.get(queue)
            : subscriberFor(queue);

    final Method subscriberOnMethod = subscriber.getClass().getMethod("on", domainEvent.getClass());

    try {
      subscriberOnMethod.invoke(subscriber, domainEvent);
    } catch (final IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException error) {
      throw new Exception(
          String.format(
              "The subscriber <%s> should implement a method `on` listening the domain event <%s>",
              queue, domainEvent.eventName()));
    } catch (final Exception error) {
      handleConsumptionError(message, queue);
    }
  }

  public void withSubscribersInformation(final DomainEventSubscribersInformation information) {
    this.information = information;
  }

  private void handleConsumptionError(final Message message, final String queue) {
    if (hasBeenRedeliveredTooMuch(message)) {
      sendToDeadLetter(message, queue);
    } else {
      sendToRetry(message, queue);
    }
  }

  private void sendToRetry(final Message message, final String queue) {
    sendMessageTo(RabbitMqExchangeNameFormatter.retry("domain_events"), message, queue);
  }

  private void sendToDeadLetter(final Message message, final String queue) {
    sendMessageTo(RabbitMqExchangeNameFormatter.deadLetter("domain_events"), message, queue);
  }

  private void sendMessageTo(final String exchange, final Message message, final String queue) {
    final Map<String, Object> headers = message.getMessageProperties().getHeaders();

    headers.put("redelivery_count", (int) headers.getOrDefault("redelivery_count", 0) + 1);

    MessageBuilder.fromMessage(message)
        .andProperties(
            MessagePropertiesBuilder.newInstance()
                .setContentEncoding("utf-8")
                .setContentType("application/json")
                .copyHeaders(headers)
                .build());

    publisher.publish(message, exchange, queue);
  }

  private boolean hasBeenRedeliveredTooMuch(final Message message) {
    return (int) message.getMessageProperties().getHeaders().getOrDefault("redelivery_count", 0)
        >= MAX_RETRIES;
  }

  private Object subscriberFor(final String queue) throws Exception {
    final String[] queueParts = queue.split("\\.");
    final String subscriberName = Utils.toCamelFirstLower(queueParts[queueParts.length - 1]);

    try {
      final Object subscriber = context.getBean(subscriberName);
      domainEventSubscribers.put(queue, subscriber);

      return subscriber;
    } catch (final Exception e) {
      throw new Exception(
          String.format("There are not registered subscribers for <%s> queue", queue));
    }
  }
}
