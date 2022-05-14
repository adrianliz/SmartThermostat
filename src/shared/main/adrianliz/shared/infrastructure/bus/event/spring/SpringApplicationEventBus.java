package adrianliz.shared.infrastructure.bus.event.spring;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.event.DomainEvent;
import adrianliz.shared.domain.bus.event.EventBus;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class SpringApplicationEventBus implements EventBus {

  private final ApplicationEventPublisher publisher;

  public SpringApplicationEventBus(final ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(final List<DomainEvent> events) {
    events.forEach(this::publish);
  }

  private void publish(final DomainEvent event) {
    publisher.publishEvent(event);
  }
}
