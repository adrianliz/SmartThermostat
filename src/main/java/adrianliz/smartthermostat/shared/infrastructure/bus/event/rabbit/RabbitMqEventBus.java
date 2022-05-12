package adrianliz.smartthermostat.shared.infrastructure.bus.event.rabbit;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;
import adrianliz.smartthermostat.shared.domain.bus.event.EventBus;
import java.util.List;
import org.springframework.amqp.AmqpException;
import org.springframework.context.annotation.Primary;

@Service
@Primary
public class RabbitMqEventBus implements EventBus {
  private final RabbitMqPublisher publisher;
  private final String exchangeName;

  public RabbitMqEventBus(RabbitMqPublisher publisher) {
    this.publisher = publisher;
    this.exchangeName = "domain_events";
  }

  @Override
  public void publish(List<DomainEvent> events) {
    events.forEach(this::publish);
  }

  private void publish(DomainEvent domainEvent) {
    try {
      this.publisher.publish(domainEvent, exchangeName);
    } catch (AmqpException error) {
      System.err.println("RabbitMqEventBus> Error publishing event: " + domainEvent);
      // failoverPublisher.publish(Collections.singletonList(domainEvent));
    }
  }
}
