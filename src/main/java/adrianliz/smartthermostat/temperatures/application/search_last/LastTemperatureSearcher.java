package adrianliz.smartthermostat.temperatures.application.search_last;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;
import adrianliz.smartthermostat.temperatures.domain.TemperatureNotExists;
import adrianliz.smartthermostat.temperatures.domain.TemperaturesRepository;

@Service
public final class LastTemperatureSearcher {

  private final TemperaturesRepository repository;

  public LastTemperatureSearcher(TemperaturesRepository repository) {
    this.repository = repository;
  }

  public TemperatureResponse search() throws TemperatureNotExists {
    return repository.searchLast().map(TemperatureResponse::fromAggregate).orElseThrow(TemperatureNotExists::new);
  }
}
