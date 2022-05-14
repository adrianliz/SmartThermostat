package adrianliz.apps.backoffice.backend;

import adrianliz.apps.backoffice.backend.command.ConsumeRabbitMqDomainEventsCommand;
import adrianliz.shared.domain.Service;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
    value = {"adrianliz.backoffice", "adrianliz.shared", "adrianliz.apps.backoffice.backend"})
public class BackofficeBackendApplication {
  public static HashMap<String, Class<?>> commands() {
    return new HashMap<>() {
      {
        put("domain-events:rabbitmq:consume", ConsumeRabbitMqDomainEventsCommand.class);
      }
    };
  }
}
