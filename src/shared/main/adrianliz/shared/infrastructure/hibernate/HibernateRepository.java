package adrianliz.shared.infrastructure.hibernate;

import adrianliz.shared.domain.Identifier;
import adrianliz.shared.domain.criteria.Criteria;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.SessionFactory;

public abstract class HibernateRepository<T> {

  protected final SessionFactory sessionFactory;
  protected final Class<T> aggregateClass;
  protected final HibernateCriteriaConverter<T> criteriaConverter;

  public HibernateRepository(final SessionFactory sessionFactory, final Class<T> aggregateClass) {
    this.sessionFactory = sessionFactory;
    this.aggregateClass = aggregateClass;
    criteriaConverter = new HibernateCriteriaConverter<T>(sessionFactory.getCriteriaBuilder());
  }

  protected void persist(final T entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();
  }

  protected Optional<T> byId(final Identifier id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession().byId(aggregateClass).load(id));
  }

  protected List<T> byCriteria(final Criteria criteria) {
    final CriteriaQuery<T> hibernateCriteria = criteriaConverter.convert(criteria, aggregateClass);

    return sessionFactory.getCurrentSession().createQuery(hibernateCriteria).getResultList();
  }

  protected List<T> all() {
    final CriteriaQuery<T> criteria =
        sessionFactory.getCriteriaBuilder().createQuery(aggregateClass);
    criteria.from(aggregateClass);

    return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
  }
}
