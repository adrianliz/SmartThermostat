package adrianliz.smartthermostat.temperatures.application.registrar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import adrianliz.smartthermostat.shared.domain.DoubleMother;
import adrianliz.smartthermostat.temperatures.TemperaturesModuleUnitTestCase;
import adrianliz.smartthermostat.temperatures.domain.*;
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

  @Test
  void throw_invalid_celsius_if_celsius_lower_than_minus_20() {
    RegistrarTemperatureCommand command = RegistrarTemperatureCommandMother.withCelsiusLowerThan(
      DoubleMother.minusTwenty()
    );

    assertThrows(InvalidCelsius.class, () -> handler.handle(command));
  }

  @Test
  void throw_invalid_celsius_if_celsius_higher_than_50() {
    RegistrarTemperatureCommand command = RegistrarTemperatureCommandMother.withCelsiusHigherThan(DoubleMother.fifty());

    assertThrows(InvalidCelsius.class, () -> handler.handle(command));
  }
}
