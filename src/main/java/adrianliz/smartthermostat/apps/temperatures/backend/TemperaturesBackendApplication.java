package adrianliz.smartthermostat.apps.temperatures.backend;

import adrianliz.smartthermostat.apps.temperatures.backend.command.ConsumeRabbitMqDomainEventsCommand;
import adrianliz.smartthermostat.shared.domain.Service;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
    value = {
      "adrianliz.smartthermostat.shared",
      "adrianliz.smartthermostat.temperatures",
      "adrianliz.smartthermostat.apps.temperatures.backend"
    })
public class TemperaturesBackendApplication {
  public static HashMap<String, Class<?>> commands() {
    return new HashMap<>() {
      {
        put("domain-events:rabbitmq:consume", ConsumeRabbitMqDomainEventsCommand.class);
      }
    };
  }
}
