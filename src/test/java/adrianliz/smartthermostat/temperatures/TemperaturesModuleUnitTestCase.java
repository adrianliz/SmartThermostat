package adrianliz.smartthermostat.temperatures;

import adrianliz.smartthermostat.shared.infrastructure.UnitTestCase;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperaturesRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

public abstract class TemperaturesModuleUnitTestCase extends UnitTestCase {
	protected TemperaturesRepository repository;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		repository = mock(TemperaturesRepository.class);
	}

	public void shouldHaveSaved(Temperature temperature) {
		verify(repository, atLeastOnce()).save(temperature);
	}
}
