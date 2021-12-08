package adrianliz.smartthermostat.temperatures.domain;

import java.util.List;
import java.util.Optional;

public interface TemperaturesRepository {
	void save(Temperature temperature);

	Temperature getLast();

	List<Temperature> getBetween(Timestamp start, Timestamp end);

	Optional<Temperature> search(TemperatureId id);
}
