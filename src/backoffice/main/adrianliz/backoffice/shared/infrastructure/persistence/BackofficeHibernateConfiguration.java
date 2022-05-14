package adrianliz.backoffice.shared.infrastructure.persistence;

import adrianliz.shared.infrastructure.config.Parameter;
import adrianliz.shared.infrastructure.config.ParameterNotExist;
import adrianliz.shared.infrastructure.hibernate.HibernateConfigurationFactory;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class BackofficeHibernateConfiguration {
  private static final String CONTEXT_NAME = "backoffice";
  private final HibernateConfigurationFactory factory;
  private final Parameter config;

  public BackofficeHibernateConfiguration(
      final HibernateConfigurationFactory factory, final Parameter config) {
    this.factory = factory;
    this.config = config;
  }

  @Bean("backoffice-transaction_manager")
  public PlatformTransactionManager hibernateTransactionManager()
      throws IOException, ParameterNotExist {
    return factory.hibernateTransactionManager(sessionFactory());
  }

  @Bean("backoffice-session_factory")
  public LocalSessionFactoryBean sessionFactory() throws IOException, ParameterNotExist {
    return factory.sessionFactory(CONTEXT_NAME, dataSource());
  }

  @Bean("backoffice-data_source")
  public DataSource dataSource() throws IOException, ParameterNotExist {
    return factory.dataSource(
        config.get("BACKOFFICE_DATABASE_HOST"),
        config.getInt("BACKOFFICE_DATABASE_PORT"),
        config.get("BACKOFFICE_DATABASE_NAME"),
        config.get("BACKOFFICE_DATABASE_USER"),
        config.get("BACKOFFICE_DATABASE_PASSWORD"));
  }
}
