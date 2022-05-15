package adrianliz.apps.backoffice.backend;

import adrianliz.shared.infrastructure.config.Parameter;
import adrianliz.shared.infrastructure.config.ParameterNotExist;
import adrianliz.shared.infrastructure.hibernate.HibernateConfigurationFactory;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@Profile("test")
@EnableTransactionManagement
public class BackofficeHibernateTestConfiguration {
  private static final String CONTEXT_NAME = "backoffice";
  private static final String JDBC_DRIVER = "org.testcontainers.jdbc.ContainerDatabaseDriver";
  private static final String JDBC_URL = "jdbc:tc:mysql:8.0:///";

  private final HibernateConfigurationFactory factory;
  private final Parameter config;

  public BackofficeHibernateTestConfiguration(
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
  public DataSource dataSource() throws ParameterNotExist, IOException {
    final String databaseName = config.get("BACKOFFICE_DATABASE_NAME");

    return factory.dataSource(
        JDBC_DRIVER,
        String.format(JDBC_URL + "%s", databaseName),
        databaseName,
        config.get("BACKOFFICE_DATABASE_USER"),
        config.get("BACKOFFICE_DATABASE_PASSWORD"));
  }
}
