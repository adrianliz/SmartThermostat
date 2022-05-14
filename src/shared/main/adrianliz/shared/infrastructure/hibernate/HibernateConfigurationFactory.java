package adrianliz.shared.infrastructure.hibernate;

import adrianliz.shared.domain.Service;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public final class HibernateConfigurationFactory {

  private final ResourcePatternResolver resourceResolver;

  public HibernateConfigurationFactory(final ResourcePatternResolver resourceResolver) {
    this.resourceResolver = resourceResolver;
  }

  public PlatformTransactionManager hibernateTransactionManager(
      final LocalSessionFactoryBean sessionFactory) {
    final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory.getObject());

    return transactionManager;
  }

  public LocalSessionFactoryBean sessionFactory(
      final String contextName, final DataSource dataSource) throws IOException {
    final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setHibernateProperties(hibernateProperties());

    final List<Resource> mappingFiles = searchMappingFiles(contextName);
    sessionFactory.setMappingLocations(mappingFiles.toArray(new Resource[0]));

    return sessionFactory;
  }

  private List<Resource> searchMappingFiles(final String contextName) {
    final List<String> modules = subdirectoriesFor(contextName);
    final List<String> goodPaths = new ArrayList<>();

    for (final String module : modules) {
      final String[] files = mappingFilesIn(module + "/infrastructure/persistence/hibernate/");

      for (final String file : files) {
        goodPaths.add(module + "/infrastructure/persistence/hibernate/" + file);
      }
    }

    return goodPaths.stream().map(FileSystemResource::new).collect(Collectors.toList());
  }

  private List<String> subdirectoriesFor(final String contextName) {
    String path = "./src/" + contextName + "/main/adrianliz/" + contextName + "/";

    String[] files = new File(path).list((current, name) -> new File(current, name).isDirectory());

    if (null == files) {
      path = "./main/adrianliz/" + contextName + "/";
      files = new File(path).list((current, name) -> new File(current, name).isDirectory());
    }

    if (null == files) {
      return Collections.emptyList();
    }

    final String finalPath = path;

    return Arrays.stream(files).map(file -> finalPath + file).collect(Collectors.toList());
  }

  private String[] mappingFilesIn(final String path) {
    final String[] files =
        new File(path)
            .list((current, name) -> new File(current, name).getName().contains(".hbm.xml"));

    if (null == files) {
      return new String[0];
    }

    return files;
  }

  public DataSource dataSource(
      final String host,
      final Integer port,
      final String databaseName,
      final String username,
      final String password)
      throws IOException {
    final BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(
        String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&"
                + "useLegacyDatetimeCode=false&serverTimezone=UTC",
            host, port, databaseName));
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    final Resource mysqlResource =
        resourceResolver.getResource(String.format("classpath:database/%s.sql", databaseName));
    final String mysqlSentences =
        new Scanner(mysqlResource.getInputStream(), StandardCharsets.UTF_8)
            .useDelimiter("\\A")
            .next();

    dataSource.setConnectionInitSqls(new ArrayList<>(Arrays.asList(mysqlSentences.split(";"))));

    return dataSource;
  }

  private Properties hibernateProperties() {
    final Properties hibernateProperties = new Properties();
    hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "none");
    hibernateProperties.put(AvailableSettings.SHOW_SQL, "false");
    hibernateProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");

    return hibernateProperties;
  }
}
