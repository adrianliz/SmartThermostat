package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.Identifier;

public final class TemperatureId extends Identifier {

  public TemperatureId(final String value) {
    super(value);
  }

  private TemperatureId() {}
}
