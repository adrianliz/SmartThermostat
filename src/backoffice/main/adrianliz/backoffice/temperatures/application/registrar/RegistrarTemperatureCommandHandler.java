package adrianliz.backoffice.temperatures.application.registrar;

import adrianliz.backoffice.temperatures.domain.Celsius;
import adrianliz.backoffice.temperatures.domain.SensorId;
import adrianliz.backoffice.temperatures.domain.TemperatureId;
import adrianliz.backoffice.temperatures.domain.Timestamp;
import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.command.CommandHandler;

@Service
public final class RegistrarTemperatureCommandHandler
    implements CommandHandler<RegistrarTemperatureCommand> {

  private final TemperatureRegistrar register;

  public RegistrarTemperatureCommandHandler(final TemperatureRegistrar temperatureRegistrar) {
    register = temperatureRegistrar;
  }

  @Override
  public void handle(final RegistrarTemperatureCommand command) {
    final TemperatureId id = new TemperatureId(command.id());
    final SensorId sensorId = new SensorId(command.sensorId());
    final Celsius celsiusRegistered = new Celsius(command.celsiusRegistered());
    final Timestamp timestamp = new Timestamp(command.timestamp());

    register.registrar(id, sensorId, celsiusRegistered, timestamp);
  }
}
