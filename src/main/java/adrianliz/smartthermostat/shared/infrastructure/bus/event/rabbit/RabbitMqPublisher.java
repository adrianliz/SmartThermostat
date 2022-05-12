package adrianliz.smartthermostat.shared.infrastructure.bus.event.rabbit;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;
import adrianliz.smartthermostat.shared.infrastructure.bus.event.DomainEventJsonSerializer;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public final class RabbitMqPublisher {
  private final RabbitTemplate rabbitTemplate;

  public RabbitMqPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publish(DomainEvent domainEvent, String exchangeName) throws AmqpException {
    String serializedDomainEvent = DomainEventJsonSerializer.serialize(domainEvent);

    Message message =
        new Message(
            serializedDomainEvent.getBytes(),
            MessagePropertiesBuilder.newInstance()
                .setContentEncoding("utf-8")
                .setContentType("application/json")
                .build());

    rabbitTemplate.send(exchangeName, domainEvent.eventName(), message);
  }

  public void publish(Message domainEvent, String exchangeName, String routingKey)
      throws AmqpException {
    rabbitTemplate.send(exchangeName, routingKey, domainEvent);
  }
}
