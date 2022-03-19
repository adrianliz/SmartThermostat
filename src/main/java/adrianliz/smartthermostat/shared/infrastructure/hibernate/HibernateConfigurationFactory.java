package adrianliz.smartthermostat.shared.infrastructure.hibernate;

import adrianliz.smartthermostat.shared.domain.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public final class HibernateConfigurationFactory {

  private final ResourcePatternResolver resourceResolver;

  public HibernateConfigurationFactory(ResourcePatternResolver resourceResolver) {
    this.resourceResolver = resourceResolver;
  }

  public PlatformTransactionManager hibernateTransactionManager(
      LocalSessionFactoryBean sessionFactory) {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory.getObject());

    return transactionManager;
  }

  public LocalSessionFactoryBean sessionFactory(String contextName, DataSource dataSource)
      throws IOException {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setHibernateProperties(hibernateProperties());

    Resource[] mappingFiles = resourceResolver.getResources("classpath:database/mapping/*.hbm.xml");
    sessionFactory.setMappingLocations(mappingFiles);

    return sessionFactory;
  }

  public DataSource dataSource(
      String host, Integer port, String databaseName, String username, String password)
      throws IOException {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(
        String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&"
                + "useLegacyDatetimeCode=false&serverTimezone=UTC",
            host, port, databaseName));
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    Resource mysqlResource =
        resourceResolver.getResource(String.format("classpath:database/%s.sql", databaseName));
    String mysqlSentences =
        new Scanner(mysqlResource.getInputStream(), StandardCharsets.UTF_8)
            .useDelimiter("\\A")
            .next();

    dataSource.setConnectionInitSqls(new ArrayList<>(Arrays.asList(mysqlSentences.split(";"))));

    return dataSource;
  }

  private Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "none");
    hibernateProperties.put(AvailableSettings.SHOW_SQL, "false");
    hibernateProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");

    return hibernateProperties;
  }
}
