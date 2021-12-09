package adrianliz.smartthermostat.apps;

import adrianliz.smartthermostat.shared.domain.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
  includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
  value = {
    "adrianliz.smartthermostat.apps", "adrianliz.smartthermostat.shared", "adrianliz.smartthermostat.temperatures",
  }
)
public class Starter {

  public static void main(String[] args) {
    SpringApplication.run(Starter.class, args);
  }
}
