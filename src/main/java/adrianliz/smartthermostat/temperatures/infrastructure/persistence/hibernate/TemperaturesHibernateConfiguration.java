package adrianliz.smartthermostat.temperatures.infrastructure.persistence.hibernate;

import adrianliz.smartthermostat.shared.infrastructure.config.Parameter;
import adrianliz.smartthermostat.shared.infrastructure.config.ParameterNotExist;
import adrianliz.smartthermostat.shared.infrastructure.hibernate.HibernateConfigurationFactory;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TemperaturesHibernateConfiguration {

  private static final String CONTEXT_NAME = "temperatures";
  private final HibernateConfigurationFactory factory;
  private final Parameter config;

  public TemperaturesHibernateConfiguration(
      final HibernateConfigurationFactory factory, final Parameter config) {
    this.factory = factory;
    this.config = config;
  }

  @Bean("temperatures-transaction_manager")
  public PlatformTransactionManager hibernateTransactionManager()
      throws IOException, ParameterNotExist {
    return factory.hibernateTransactionManager(sessionFactory());
  }

  @Bean("temperatures-session_factory")
  public LocalSessionFactoryBean sessionFactory() throws IOException, ParameterNotExist {
    return factory.sessionFactory(CONTEXT_NAME, dataSource());
  }

  @Bean("temperatures-data_source")
  public DataSource dataSource() throws IOException, ParameterNotExist {
    return factory.dataSource(
        config.get("TEMPERATURES_DATABASE_HOST"),
        config.getInt("TEMPERATURES_DATABASE_PORT"),
        config.get("TEMPERATURES_DATABASE_NAME"),
        config.get("TEMPERATURES_DATABASE_USER"),
        config.get("TEMPERATURES_DATABASE_PASSWORD"));
  }
}
