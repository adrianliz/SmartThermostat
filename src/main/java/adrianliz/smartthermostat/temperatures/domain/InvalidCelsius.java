package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.DomainError;

public final class InvalidCelsius extends DomainError {

  public InvalidCelsius(Double celsius, Double MAX_CELSIUS, Double MIN_CELSIUS) {
    super(
        "invalid_celsius",
        String.format(
            "Celsius <%s> couldn't be higher than <%s> and lower than <%s>",
            celsius, MAX_CELSIUS, MIN_CELSIUS));
  }
}
