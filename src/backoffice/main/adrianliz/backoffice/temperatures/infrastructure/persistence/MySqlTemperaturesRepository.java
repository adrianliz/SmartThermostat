package adrianliz.backoffice.temperatures.infrastructure.persistence;

import adrianliz.backoffice.temperatures.domain.Temperature;
import adrianliz.backoffice.temperatures.domain.TemperatureId;
import adrianliz.backoffice.temperatures.domain.TemperaturesRepository;
import adrianliz.backoffice.temperatures.domain.Timestamp;
import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.criteria.Criteria;
import adrianliz.shared.domain.criteria.Filters;
import adrianliz.shared.domain.criteria.Order;
import adrianliz.shared.infrastructure.hibernate.HibernateRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("backoffice-transaction_manager")
public class MySqlTemperaturesRepository extends HibernateRepository<Temperature>
    implements TemperaturesRepository {

  public MySqlTemperaturesRepository(
      @Qualifier("backoffice-session_factory") final SessionFactory sessionFactory) {
    super(sessionFactory, Temperature.class);
  }

  @Override
  public void save(final Temperature temperature) {
    persist(temperature);
  }

  @Override
  public Optional<Temperature> searchLast() {
    final Criteria criteria =
        new Criteria(Filters.none(), Order.desc("timestamp"), Optional.of(1), Optional.empty());
    return byCriteria(criteria).stream().findFirst();
  }

  @Override
  public List<Temperature> getBetween(final Timestamp start, final Timestamp end) {
    return null;
  }

  @Override
  public Optional<Temperature> search(final TemperatureId id) {
    return Optional.empty();
  }
}
