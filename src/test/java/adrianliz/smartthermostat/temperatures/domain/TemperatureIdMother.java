package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.UuidMother;

public final class TemperatureIdMother {
  public static TemperatureId create(String value) {
    return new TemperatureId(value);
  }

  public static TemperatureId random() {
    return create(UuidMother.random());
  }
}
