package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.UuidMother;

public final class SensorIdMother {

  public static SensorId create(final String value) {
    return new SensorId(value);
  }

  public static SensorId random() {
    return create(UuidMother.random());
  }
}
