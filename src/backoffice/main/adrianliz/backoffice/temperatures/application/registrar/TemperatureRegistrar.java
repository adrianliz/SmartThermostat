package adrianliz.backoffice.temperatures.application.registrar;

import adrianliz.backoffice.temperatures.domain.*;
import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.event.EventBus;

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
