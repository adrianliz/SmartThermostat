package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.DomainError;

public final class InvalidCelsius extends DomainError {

  public InvalidCelsius(final Double celsius, final Double maxCelsius, final Double minCelsius) {
    super(
        "invalid_celsius",
        String.format(
            "Celsius <%s> couldn't be higher than <%s> and lower than <%s>",
            celsius, maxCelsius, minCelsius));
  }
}
