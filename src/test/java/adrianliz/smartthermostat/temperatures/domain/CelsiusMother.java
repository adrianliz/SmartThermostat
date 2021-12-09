package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.DoubleMother;

public final class CelsiusMother {
  public static Celsius create(Double value) {
    return new Celsius(value);
  }

  public static Celsius random() {
    return create(DoubleMother.random());
  }
}
