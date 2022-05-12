package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.Identifier;

public final class SensorId extends Identifier {

  public SensorId(final String value) {
    super(value);
  }

  private SensorId() {}
}
