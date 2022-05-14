package adrianliz.backoffice.temperatures.application.registrar;

import adrianliz.backoffice.temperatures.domain.*;

public final class RegistrarTemperatureCommandMother {

  public static RegistrarTemperatureCommand create(
      final TemperatureId id,
      final SensorId sensorId,
      final Celsius celsius,
      final Timestamp timestamp) {
    return new RegistrarTemperatureCommand(
        id.value(), sensorId.value(), celsius.value(), timestamp.value());
  }

  public static RegistrarTemperatureCommand random() {
    return create(
        TemperatureIdMother.random(),
        SensorIdMother.random(),
        CelsiusMother.random(),
        TimestampMother.random());
  }

  public static RegistrarTemperatureCommand withCelsiusLowerThan(final Double celsius) {
    return new RegistrarTemperatureCommand(
        TemperatureIdMother.random().value(),
        SensorIdMother.random().value(),
        celsius - 1,
        TimestampMother.random().value());
  }

  public static RegistrarTemperatureCommand withCelsiusHigherThan(final Double celsius) {
    return new RegistrarTemperatureCommand(
        TemperatureIdMother.random().value(),
        SensorIdMother.random().value(),
        celsius + 1,
        TimestampMother.random().value());
  }
}
