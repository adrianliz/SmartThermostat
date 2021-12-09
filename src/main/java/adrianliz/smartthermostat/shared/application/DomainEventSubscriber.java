package adrianliz.smartthermostat.shared.application;

import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;

public interface DomainEventSubscriber<EventType extends DomainEvent> {
  Class<EventType> subscribedTo();

  void consume(EventType event);
}
