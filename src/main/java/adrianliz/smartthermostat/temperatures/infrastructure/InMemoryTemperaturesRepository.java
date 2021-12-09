package adrianliz.smartthermostat.temperatures.infrastructure;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperatureId;
import adrianliz.smartthermostat.temperatures.domain.TemperaturesRepository;
import adrianliz.smartthermostat.temperatures.domain.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public final class InMemoryTemperaturesRepository implements TemperaturesRepository {

  private final List<Temperature> temperatures = new ArrayList<>();

  @Override
  public void save(Temperature temperature) {
    temperatures.add(temperature);
  }

  @Override
  public Optional<Temperature> searchLast() {
    if (temperatures.isEmpty()) return Optional.empty();

    return Optional.of(temperatures.get(temperatures.size() - 1));
  }

  @Override
  public List<Temperature> getBetween(Timestamp start, Timestamp end) {
    return temperatures
      .stream()
      .filter(temperature ->
        temperature.timestamp().value() >= start.value() && temperature.timestamp().value() <= end.value()
      )
      .collect(Collectors.toList());
  }

  @Override
  public Optional<Temperature> search(TemperatureId id) {
    for (Temperature temperature : temperatures) {
      if (temperature.id().equals(id)) {
        return Optional.of(temperature);
      }
    }
    return Optional.empty();
  }
}
