package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.DomainError;

public final class TemperatureNotExists extends DomainError {

  public TemperatureNotExists() {
    super("temperature_not_exists", "The temperature doesn't exists");
  }

  public TemperatureNotExists(TemperatureId id) {
    super(
      "temperature_not_exists",
      String.format("The temperature <%s> doesn't exists", id)
    );
  }
}
