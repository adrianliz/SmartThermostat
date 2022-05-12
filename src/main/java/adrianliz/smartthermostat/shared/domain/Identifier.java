package adrianliz.smartthermostat.shared.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Identifier implements Serializable {

  protected final String value;

  public Identifier(final String value) {
    ensureValidUuid(value);

    this.value = value;
  }

  protected Identifier() {
    value = null;
  }

  public String value() {
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
    final Identifier that = (Identifier) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  private void ensureValidUuid(final String value) throws IllegalArgumentException {
    UUID.fromString(value);
  }
}
