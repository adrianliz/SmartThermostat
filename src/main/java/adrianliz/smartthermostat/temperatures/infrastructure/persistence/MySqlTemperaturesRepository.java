package adrianliz.smartthermostat.temperatures.infrastructure.persistence;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.criteria.Criteria;
import adrianliz.smartthermostat.shared.domain.criteria.Filters;
import adrianliz.smartthermostat.shared.domain.criteria.Order;
import adrianliz.smartthermostat.shared.infrastructure.hibernate.HibernateRepository;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperatureId;
import adrianliz.smartthermostat.temperatures.domain.TemperaturesRepository;
import adrianliz.smartthermostat.temperatures.domain.Timestamp;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("temperatures-transaction_manager")
public class MySqlTemperaturesRepository extends HibernateRepository<Temperature> implements TemperaturesRepository {

  public MySqlTemperaturesRepository(@Qualifier("temperatures-session_factory") SessionFactory sessionFactory) {
    super(sessionFactory, Temperature.class);
  }

  @Override
  public void save(Temperature temperature) {
    persist(temperature);
  }

  @Override
  public Optional<Temperature> searchLast() {
    Criteria criteria = new Criteria(Filters.none(), Order.desc("timestamp"), Optional.of(1), Optional.empty());
    return Optional.ofNullable(byCriteria(criteria).get(0));
  }

  @Override
  public List<Temperature> getBetween(Timestamp start, Timestamp end) {
    return null;
  }

  @Override
  public Optional<Temperature> search(TemperatureId id) {
    return Optional.empty();
  }
}