package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandHandler;
import adrianliz.smartthermostat.temperatures.domain.*;

@Service
public final class RegistrarTemperatureCommandHandler implements CommandHandler<RegistrarTemperatureCommand> {

  private final TemperatureRegistrar register;

  public RegistrarTemperatureCommandHandler(TemperatureRegistrar temperatureRegistrar) {
    this.register = temperatureRegistrar;
  }

  @Override
  public void handle(RegistrarTemperatureCommand command) throws InvalidCelsius {
    TemperatureId id = new TemperatureId(command.id());
    SensorId sensorId = new SensorId(command.sensorId());
    Celsius celsiusRegistered = new Celsius(command.celsiusRegistered());
    Timestamp timestamp = new Timestamp(command.timestamp());

    register.registrar(id, sensorId, celsiusRegistered, timestamp);
  }
}
