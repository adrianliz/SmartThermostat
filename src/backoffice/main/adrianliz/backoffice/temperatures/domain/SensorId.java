package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.Identifier;

public final class SensorId extends Identifier {

  public SensorId(final String value) {
    super(value);
  }

  private SensorId() {}
}
