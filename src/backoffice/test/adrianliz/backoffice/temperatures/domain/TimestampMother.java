package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.LongMother;

public final class TimestampMother {

  public static Timestamp create(final Long value) {
    return new Timestamp(value);
  }

  public static Timestamp random() {
    return create(LongMother.random());
  }
}
