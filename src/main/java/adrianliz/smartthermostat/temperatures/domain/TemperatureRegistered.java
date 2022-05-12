package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public final class TemperatureRegistered extends DomainEvent {

  private final double celsiusRegistered;
  private final long timestamp;

  public TemperatureRegistered() {
    super(null);

    celsiusRegistered = 0;
    timestamp = 0;
  }

  public TemperatureRegistered(
      final String aggregateId, final double celsiusRegistered, final long timestamp) {
    super(aggregateId);
    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  public TemperatureRegistered(
      final String aggregateId,
      final String eventId,
      final String occurredOn,
      final double celsiusRegistered,
      final long timestamp) {
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
    return new HashMap<>() {
      {
        put("celsiusRegistered", celsiusRegistered);
        put("timestamp", timestamp);
      }
    };
  }

  @Override
  public DomainEvent fromPrimitives(
      final String aggregateId,
      final HashMap<String, Serializable> body,
      final String eventId,
      final String occurredOn) {
    return new TemperatureRegistered(
        aggregateId,
        eventId,
        occurredOn,
        (double) body.get("celsiusRegistered"),
        (int) body.get("timestamp"));
  }

  public double celsisusRegistered() {
    return celsiusRegistered;
  }

  public long timestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TemperatureRegistered that = (TemperatureRegistered) o;
    return (Double.compare(that.celsiusRegistered, celsiusRegistered) == 0
        && timestamp == that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(celsiusRegistered, timestamp);
  }
}
