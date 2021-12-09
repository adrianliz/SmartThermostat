package adrianliz.smartthermostat.temperatures.application;

import adrianliz.smartthermostat.temperatures.domain.*;

public final class TemperatureResponseMother {

  public static TemperatureResponse create(
    TemperatureId id,
    SensorId sensorId,
    Celsius celsiusRegistered,
    Timestamp timestamp
  ) {
    return new TemperatureResponse(id.value(), sensorId.value(), celsiusRegistered.value(), timestamp.value());
  }

  public static TemperatureResponse random() {
    return create(
      TemperatureIdMother.random(),
      SensorIdMother.random(),
      CelsiusMother.random(),
      TimestampMother.random()
    );
  }
}
