package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.event.EventBus;
import adrianliz.smartthermostat.temperatures.domain.Celsius;
import adrianliz.smartthermostat.temperatures.domain.SensorId;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperatureId;
import adrianliz.smartthermostat.temperatures.domain.TemperaturesRepository;
import adrianliz.smartthermostat.temperatures.domain.Timestamp;

@Service
public final class TemperatureRegistrar {

  private final TemperaturesRepository repository;
  private final EventBus eventBus;

  public TemperatureRegistrar(final TemperaturesRepository repository, final EventBus eventBus) {
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void registrar(
      final TemperatureId id,
      final SensorId sensorId,
      final Celsius celsiusRegistered,
      final Timestamp timestamp) {
    final Temperature temperature = Temperature.create(id, sensorId, celsiusRegistered, timestamp);

    repository.save(temperature);
    eventBus.publish(temperature.pullDomainEvents());
  }
}
