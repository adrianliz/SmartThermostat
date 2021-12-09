package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.temperatures.domain.*;

public final class RegistrarTemperatureCommandMother {
  public static RegistrarTemperatureCommand create(
    TemperatureId id,
    SensorId sensorId,
    Celsius celsius,
    Timestamp timestamp) {

    return new RegistrarTemperatureCommand(id.value(), sensorId.value(), celsius.value(), timestamp.value());
  }

  public static RegistrarTemperatureCommand random() {
    return create(
      TemperatureIdMother.random(),
      SensorIdMother.random(),
      CelsiusMother.random(),
      TimestampMother.random());
  }
}
