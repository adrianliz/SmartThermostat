package adrianliz.smartthermostat.temperatures.application.registrar;

import adrianliz.smartthermostat.temperatures.TemperaturesModuleUnitTestCase;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperatureMother;
import adrianliz.smartthermostat.temperatures.domain.TemperatureRegistered;
import adrianliz.smartthermostat.temperatures.domain.TemperatureRegisteredMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class RegistrarTemperatureCommandHandlerShould extends TemperaturesModuleUnitTestCase {
	private RegistrarTemperatureCommandHandler handler;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new RegistrarTemperatureCommandHandler(new TemperatureRegistrar(repository, eventBus));
	}

	@Test
	void registrar_a_valid_temperature() {
		RegistrarTemperatureCommand command = RegistrarTemperatureCommandMother.random();
		Temperature temperature = TemperatureMother.fromCommand(command);
		TemperatureRegistered domainEvent = TemperatureRegisteredMother.fromTemperature(temperature);

		handler.handle(command);

		shouldHaveSaved(temperature);
		shouldHavePublished(domainEvent);
	}
}
