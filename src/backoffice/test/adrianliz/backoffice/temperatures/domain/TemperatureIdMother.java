package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.UuidMother;

public final class TemperatureIdMother {

  public static TemperatureId create(final String value) {
    return new TemperatureId(value);
  }

  public static TemperatureId random() {
    return create(UuidMother.random());
  }
}
