package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.LongValueObject;

public final class Timestamp extends LongValueObject {

  public Timestamp(final Long value) {
    super(value);
  }

  private Timestamp() {
    super(null);
  }
}
