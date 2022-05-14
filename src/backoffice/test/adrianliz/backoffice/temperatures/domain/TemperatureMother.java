package adrianliz.backoffice.temperatures.domain;

import adrianliz.backoffice.temperatures.application.registrar.RegistrarTemperatureCommand;

public final class TemperatureMother {

  public static Temperature create(
      final TemperatureId id,
      final SensorId sensorId,
      final Celsius celsius,
      final Timestamp timestamp) {
    return new Temperature(id, sensorId, celsius, timestamp);
  }

  public static Temperature fromCommand(final RegistrarTemperatureCommand command) {
    return create(
        TemperatureIdMother.create(command.id()),
        SensorIdMother.create(command.sensorId()),
        CelsiusMother.create(command.celsiusRegistered()),
        TimestampMother.create(command.timestamp()));
  }

  public static Temperature random() {
    return create(
        TemperatureIdMother.random(),
        SensorIdMother.random(),
        CelsiusMother.random(),
        TimestampMother.random());
  }
}
