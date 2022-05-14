package adrianliz.shared.infrastructure.config;

import adrianliz.shared.domain.Service;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public final class Parameter {

  private final Dotenv dotenv;

  public Parameter(final Dotenv dotenv) {
    this.dotenv = dotenv;
  }

  public String get(final String key) throws ParameterNotExist {
    final String value = dotenv.get(key);

    if (null == value) {
      throw new ParameterNotExist(key);
    }

    return value;
  }

  public Integer getInt(final String key) throws ParameterNotExist {
    final String value = get(key);

    return Integer.parseInt(value);
  }
}
