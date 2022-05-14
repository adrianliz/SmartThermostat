package adrianliz.backoffice.temperatures.application;

import adrianliz.backoffice.temperatures.domain.*;

public final class TemperatureResponseMother {

  public static TemperatureResponse create(
      final TemperatureId id,
      final SensorId sensorId,
      final Celsius celsiusRegistered,
      final Timestamp timestamp) {
    return new TemperatureResponse(
        id.value(), sensorId.value(), celsiusRegistered.value(), timestamp.value());
  }

  public static TemperatureResponse random() {
    return create(
        TemperatureIdMother.random(),
        SensorIdMother.random(),
        CelsiusMother.random(),
        TimestampMother.random());
  }
}
