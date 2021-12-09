package adrianliz.smartthermostat.temperatures.application;

import adrianliz.smartthermostat.shared.domain.bus.query.Response;
import adrianliz.smartthermostat.temperatures.domain.Temperature;

import java.util.Objects;

public final class TemperatureResponse implements Response {
  private final String id;
  private final String sensorId;
  private final Double celsiusRegistered;
  private final long timestamp;

  public TemperatureResponse(String id, String sensorId, Double celsiusRegistered, long timestamp) {
    this.id = id;
    this.sensorId = sensorId;
    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  public static TemperatureResponse fromAggregate(Temperature temperature) {
    return new TemperatureResponse(
      temperature.id().value(),
      temperature.sensorId().value(),
      temperature.celsiusRegistered().value(),
      temperature.timestamp().value());
  }

  public String id() {
    return id;
  }

  public String sensorId() {
    return sensorId;
  }

  public Double celsiusRegistered() {
    return celsiusRegistered;
  }

  public long timestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TemperatureResponse response = (TemperatureResponse) o;
    return timestamp == response.timestamp && Objects.equals(id, response.id) && Objects.equals(sensorId, response.sensorId) && Objects.equals(celsiusRegistered, response.celsiusRegistered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sensorId, celsiusRegistered, timestamp);
  }
}
