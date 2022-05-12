package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.LongValueObject;

public final class Timestamp extends LongValueObject {

  public Timestamp(final Long value) {
    super(value);
  }

  private Timestamp() {
    super(null);
  }
}
