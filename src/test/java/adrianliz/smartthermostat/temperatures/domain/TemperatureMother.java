package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.temperatures.application.registrar.RegistrarTemperatureCommand;

public final class TemperatureMother {

  public static Temperature create(
    TemperatureId id,
    SensorId sensorId,
    Celsius celsius,
    Timestamp timestamp
  ) {
    return new Temperature(id, sensorId, celsius, timestamp);
  }

  public static Temperature fromCommand(RegistrarTemperatureCommand command) {
    return create(
      TemperatureIdMother.create(command.id()),
      SensorIdMother.create(command.sensorId()),
      CelsiusMother.create(command.celsiusRegistered()),
      TimestampMother.create(command.timestamp())
    );
  }

  public static Temperature random() {
    return create(
      TemperatureIdMother.random(),
      SensorIdMother.random(),
      CelsiusMother.random(),
      TimestampMother.random()
    );
  }
}
