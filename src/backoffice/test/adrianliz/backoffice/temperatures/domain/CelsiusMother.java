package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.DoubleMother;

public final class CelsiusMother {

  public static Celsius create(final Double value) {
    return new Celsius(value);
  }

  public static Celsius random() {
    return create(DoubleMother.random());
  }
}
