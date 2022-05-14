package adrianliz.backoffice.temperatures.domain;

public final class TemperatureRegisteredMother {

  public static TemperatureRegistered create(
      final TemperatureId id, final Celsius celsius, final Timestamp timestamp) {
    return new TemperatureRegistered(id.value(), celsius.value(), timestamp.value());
  }

  public static TemperatureRegistered fromTemperature(final Temperature temperature) {
    return create(temperature.id(), temperature.celsiusRegistered(), temperature.timestamp());
  }

  public static TemperatureRegistered random() {
    return create(TemperatureIdMother.random(), CelsiusMother.random(), TimestampMother.random());
  }
}
