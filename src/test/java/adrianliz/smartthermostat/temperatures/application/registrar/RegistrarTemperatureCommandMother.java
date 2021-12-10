package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.temperatures.domain.*;

public final class RegistrarTemperatureCommandMother {

  public static RegistrarTemperatureCommand create(
    TemperatureId id,
    SensorId sensorId,
    Celsius celsius,
    Timestamp timestamp
  ) {
    return new RegistrarTemperatureCommand(id.value(), sensorId.value(), celsius.value(), timestamp.value());
  }

  public static RegistrarTemperatureCommand random() {
    return create(
      TemperatureIdMother.random(),
      SensorIdMother.random(),
      CelsiusMother.random(),
      TimestampMother.random()
    );
  }

  public static RegistrarTemperatureCommand withCelsiusLowerThan(Double celsius) {
    return new RegistrarTemperatureCommand(
      TemperatureIdMother.random().value(),
      SensorIdMother.random().value(),
      celsius - 1,
      TimestampMother.random().value()
    );
  }

  public static RegistrarTemperatureCommand withCelsiusHigherThan(Double celsius) {
    return new RegistrarTemperatureCommand(
      TemperatureIdMother.random().value(),
      SensorIdMother.random().value(),
      celsius + 1,
      TimestampMother.random().value()
    );
  }
}
