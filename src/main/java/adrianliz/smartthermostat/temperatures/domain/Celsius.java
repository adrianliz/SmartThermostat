package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.DoubleValueObject;

public final class Celsius extends DoubleValueObject {

  private static final Double MAX_CELSIUS = 50.0;
  private static final Double MIN_CELSIUS = -20.0;

  public Celsius(Double value) throws InvalidCelsius {
    super(value);
    validateCelsius(value);
  }

  private void validateCelsius(Double value) throws InvalidCelsius {
    if (value == null || value > MAX_CELSIUS || value < MIN_CELSIUS) {
      throw new InvalidCelsius(value, MAX_CELSIUS, MIN_CELSIUS);
    }
  }
}
