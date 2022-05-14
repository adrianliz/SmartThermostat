package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.AggregateRoot;
import java.util.Objects;

public final class Temperature extends AggregateRoot {

  private final TemperatureId id;
  private final SensorId sensorId;
  private final Celsius celsiusRegistered;
  private final Timestamp timestamp;

  public Temperature(
      final TemperatureId id,
      final SensorId sensorId,
      final Celsius celsiusRegistered,
      final Timestamp timestamp) {
    this.id = id;
    this.sensorId = sensorId;
    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  private Temperature() {
    id = null;
    sensorId = null;
    celsiusRegistered = null;
    timestamp = null;
  }

  public static Temperature create(
      final TemperatureId temperatureId,
      final SensorId sensorId,
      final Celsius celsiusRegistered,
      final Timestamp timestamp) {
    final Temperature temperature =
        new Temperature(temperatureId, sensorId, celsiusRegistered, timestamp);

    temperature.record(
        new TemperatureRegistered(
            temperatureId.value(), celsiusRegistered.value(), timestamp.value()));

    return temperature;
  }

  public TemperatureId id() {
    return id;
  }

  public SensorId sensorId() {
    return sensorId;
  }

  public Celsius celsiusRegistered() {
    return celsiusRegistered;
  }

  public Timestamp timestamp() {
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
    final Temperature that = (Temperature) o;
    return (Objects.equals(id, that.id)
        && Objects.equals(sensorId, that.sensorId)
        && Objects.equals(celsiusRegistered, that.celsiusRegistered)
        && Objects.equals(timestamp, that.timestamp));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sensorId, celsiusRegistered, timestamp);
  }
}
