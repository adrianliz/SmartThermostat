package adrianliz.backoffice.temperatures.application.search_last;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import adrianliz.backoffice.temperatures.TemperaturesModuleUnitTestCase;
import adrianliz.backoffice.temperatures.application.TemperatureResponse;
import adrianliz.backoffice.temperatures.application.TemperatureResponseMother;
import adrianliz.backoffice.temperatures.domain.Temperature;
import adrianliz.backoffice.temperatures.domain.TemperatureMother;
import adrianliz.backoffice.temperatures.domain.TemperatureNotExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class SearchLastTemperatureQueryHandlerShould extends TemperaturesModuleUnitTestCase {

  private SearchLastTemperatureQueryHandler handler;

  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();

    handler = new SearchLastTemperatureQueryHandler(new LastTemperatureSearcher(repository));
  }

  @Test
  void search_last_temperature() {
    final Temperature lastTemperature = TemperatureMother.random();
    final SearchLastTemperatureQuery query = SearchLastTemperatureQueryMother.random();
    final TemperatureResponse response =
        TemperatureResponseMother.create(
            lastTemperature.id(),
            lastTemperature.sensorId(),
            lastTemperature.celsiusRegistered(),
            lastTemperature.timestamp());

    shouldSearchLastTemperature(lastTemperature);

    assertEquals(response, handler.handle(query));
  }

  @Test
  void throw_temperature_not_exists_when_there_arent_temperatures() {
    final SearchLastTemperatureQuery query = SearchLastTemperatureQueryMother.random();

    assertThrows(TemperatureNotExists.class, () -> handler.handle(query));
  }
}
