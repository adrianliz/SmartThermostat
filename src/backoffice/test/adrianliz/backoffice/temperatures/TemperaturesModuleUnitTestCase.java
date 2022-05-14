package adrianliz.backoffice.temperatures;

import static org.mockito.Mockito.*;

import adrianliz.backoffice.temperatures.domain.Temperature;
import adrianliz.backoffice.temperatures.domain.TemperaturesRepository;
import adrianliz.shared.infrastructure.UnitTestCase;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public abstract class TemperaturesModuleUnitTestCase extends UnitTestCase {

  protected TemperaturesRepository repository;

  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();

    repository = mock(TemperaturesRepository.class);
  }

  public void shouldHaveSaved(final Temperature temperature) {
    verify(repository, atLeastOnce()).save(temperature);
  }

  public void shouldSearchLastTemperature(final Temperature temperature) {
    Mockito.when(repository.searchLast()).thenReturn(Optional.of(temperature));
  }
}
