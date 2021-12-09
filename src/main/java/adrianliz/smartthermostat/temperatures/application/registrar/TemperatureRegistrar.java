package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.event.EventBus;
import adrianliz.smartthermostat.temperatures.domain.*;

@Service
public final class TemperatureRegistrar {
	private final TemperaturesRepository repository;
	private final EventBus eventBus;

	public TemperatureRegistrar(TemperaturesRepository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void registrar(TemperatureId id, SensorId sensorId, Celsius celsiusRegistered, Timestamp timestamp) {
		Temperature temperature = Temperature.create(id, sensorId, celsiusRegistered, timestamp);

		repository.save(temperature);
		eventBus.publish(temperature.pullDomainEvents());
	}
}
