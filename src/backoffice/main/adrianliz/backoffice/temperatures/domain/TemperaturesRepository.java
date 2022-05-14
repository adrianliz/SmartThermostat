package adrianliz.backoffice.temperatures.domain;

import java.util.List;
import java.util.Optional;

public interface TemperaturesRepository {
  void save(final Temperature temperature);

  Optional<Temperature> searchLast();

  List<Temperature> getBetween(final Timestamp start, final Timestamp end);

  Optional<Temperature> search(final TemperatureId id);
}
