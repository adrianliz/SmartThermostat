package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public final class TemperatureRegistered extends DomainEvent {
  private final double celsiusRegistered;
  private final long timestamp;

  public TemperatureRegistered(String aggregateId, double celsiusRegistered, long timestamp) {
    super(aggregateId);

    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  public TemperatureRegistered(
    String aggregateId,
    String eventId,
    String occurredOn,
    double celsiusRegistered,
    long timestamp
  ) {
    super(aggregateId, eventId, occurredOn);

    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  @Override
  public String eventName() {
    return "temperature.registered";
  }

  @Override
  public HashMap<String, Serializable> toPrimitives() {
    return new HashMap<String, Serializable>() {{
      put("celsiusRegistered", celsiusRegistered);
      put("timestamp", timestamp);
    }};
  }

  @Override
  public DomainEvent fromPrimitives(
    String aggregateId,
    HashMap<String, Serializable> body,
    String eventId,
    String occurredOn) {
    return new TemperatureRegistered(
      aggregateId,
      eventId,
      occurredOn,
      (double) body.get("celsiusRegistered"),
      (long) body.get("timestamp"));
  }

  public double celsisusRegistered() {
    return celsiusRegistered;
  }

  public long timestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TemperatureRegistered that = (TemperatureRegistered) o;
    return Double.compare(that.celsiusRegistered, celsiusRegistered) == 0 && timestamp == that.timestamp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(celsiusRegistered, timestamp);
  }
}
