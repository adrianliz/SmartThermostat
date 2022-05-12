package adrianliz.smartthermostat.shared.domain.bus.event;

import adrianliz.smartthermostat.shared.domain.Utils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public abstract class DomainEvent {

  private String aggregateId;
  private String eventId;
  private String occurredOn;

  public DomainEvent(final String aggregateId) {
    this.aggregateId = aggregateId;
    eventId = UUID.randomUUID().toString();
    occurredOn = Utils.dateToString(LocalDateTime.now());
  }

  public DomainEvent(final String aggregateId, final String eventId, final String occurredOn) {
    this.aggregateId = aggregateId;
    this.eventId = eventId;
    this.occurredOn = occurredOn;
  }

  protected DomainEvent() {}

  public abstract String eventName();

  public abstract HashMap<String, Serializable> toPrimitives();

  public abstract DomainEvent fromPrimitives(
      String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn);

  public String aggregateId() {
    return aggregateId;
  }

  public String eventId() {
    return eventId;
  }

  public String occurredOn() {
    return occurredOn;
  }
}
