package adrianliz.backoffice.temperatures.application.registrar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import adrianliz.backoffice.temperatures.TemperaturesModuleUnitTestCase;
import adrianliz.backoffice.temperatures.domain.*;
import adrianliz.shared.domain.DoubleMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class RegistrarTemperatureCommandHandlerShould extends TemperaturesModuleUnitTestCase {

  private RegistrarTemperatureCommandHandler handler;

  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();

    handler =
        new RegistrarTemperatureCommandHandler(new TemperatureRegistrar(repository, eventBus));
  }

  @Test
  void registrar_a_valid_temperature() {
    final RegistrarTemperatureCommand command = RegistrarTemperatureCommandMother.random();
    final Temperature temperature = TemperatureMother.fromCommand(command);
    final TemperatureRegistered domainEvent =
        TemperatureRegisteredMother.fromTemperature(temperature);

    handler.handle(command);

    shouldHaveSaved(temperature);
    shouldHavePublished(domainEvent);
  }

  @Test
  void throw_invalid_celsius_if_celsius_lower_than_minus_20() {
    final RegistrarTemperatureCommand command =
        RegistrarTemperatureCommandMother.withCelsiusLowerThan(DoubleMother.minusTwenty());

    assertThrows(InvalidCelsius.class, () -> handler.handle(command));
  }

  @Test
  void throw_invalid_celsius_if_celsius_higher_than_50() {
    final RegistrarTemperatureCommand command =
        RegistrarTemperatureCommandMother.withCelsiusHigherThan(DoubleMother.fifty());

    assertThrows(InvalidCelsius.class, () -> handler.handle(command));
  }
}
