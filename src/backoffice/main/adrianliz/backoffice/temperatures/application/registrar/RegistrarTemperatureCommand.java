package adrianliz.backoffice.temperatures.application.registrar;

import adrianliz.shared.domain.bus.command.Command;

public final class RegistrarTemperatureCommand implements Command {

  private final String id;
  private final String sensorId;
  private final double celsiusRegistered;
  private final long timestamp;

  public RegistrarTemperatureCommand(
      final String id,
      final String sensorId,
      final double celsiusRegistered,
      final long timestamp) {
    this.id = id;
    this.sensorId = sensorId;
    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  public String id() {
    return id;
  }

  public String sensorId() {
    return sensorId;
  }

  public double celsiusRegistered() {
    return celsiusRegistered;
  }

  public long timestamp() {
    return timestamp;
  }
}
