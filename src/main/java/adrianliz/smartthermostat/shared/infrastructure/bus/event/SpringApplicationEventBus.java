package adrianliz.smartthermostat.shared.infrastructure.bus.event;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;
import adrianliz.smartthermostat.shared.domain.bus.event.EventBus;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Service
public class SpringApplicationEventBus implements EventBus {
  private final ApplicationEventPublisher publisher;

  public SpringApplicationEventBus(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(final List<DomainEvent> events) {
    events.forEach(this::publish);
  }

  private void publish(final DomainEvent event) {
    this.publisher.publishEvent(event);
  }
}
