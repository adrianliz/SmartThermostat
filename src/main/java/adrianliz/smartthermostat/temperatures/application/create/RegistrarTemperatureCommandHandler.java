package adrianliz.smartthermostat.temperatures.application.create;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandHandler;
import adrianliz.smartthermostat.temperatures.domain.Celsius;
import adrianliz.smartthermostat.temperatures.domain.SensorId;
import adrianliz.smartthermostat.temperatures.domain.TemperatureId;
import adrianliz.smartthermostat.temperatures.domain.Timestamp;

@Service
public final class RegistrarTemperatureCommandHandler implements CommandHandler<RegistrarTemperatureCommand> {
	private final TemperatureRegistrar register;

	public RegistrarTemperatureCommandHandler(TemperatureRegistrar temperatureRegistrar) {
		this.register = temperatureRegistrar;
	}

	@Override
	public void handle(RegistrarTemperatureCommand command) {
		TemperatureId id = new TemperatureId(command.id());
		SensorId sensorId = new SensorId(command.sensorId());
		Celsius celsiusRegistered = new Celsius(command.celsiusRegistered());
		Timestamp timestamp = new Timestamp(command.timestamp());

		register.register(id, sensorId, celsiusRegistered, timestamp);
	}
}
