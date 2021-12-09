package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.UuidMother;

public final class SensorIdMother {
  public static SensorId create(String value) {
    return new SensorId(value);
  }

  public static SensorId random() {
    return create(UuidMother.random());
  }
}
