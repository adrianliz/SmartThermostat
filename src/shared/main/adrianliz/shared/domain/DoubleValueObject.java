package adrianliz.shared.domain;

import java.util.Objects;

public class DoubleValueObject {

  private final Double value;

  public DoubleValueObject(final Double value) {
    this.value = value;
  }

  public Double value() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final DoubleValueObject that = (DoubleValueObject) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
