package adrianliz.backoffice.temperatures.application.search_last;

import adrianliz.backoffice.temperatures.application.TemperatureResponse;
import adrianliz.backoffice.temperatures.domain.TemperatureNotExists;
import adrianliz.backoffice.temperatures.domain.TemperaturesRepository;
import adrianliz.shared.domain.Service;

@Service
public final class LastTemperatureSearcher {

  private final TemperaturesRepository repository;

  public LastTemperatureSearcher(final TemperaturesRepository repository) {
    this.repository = repository;
  }

  public TemperatureResponse search() throws TemperatureNotExists {
    return repository
        .searchLast()
        .map(TemperatureResponse::fromAggregate)
        .orElseThrow(TemperatureNotExists::new);
  }
}
