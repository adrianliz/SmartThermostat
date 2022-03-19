package adrianliz.smartthermostat.apps.temperatures.backend.config;

import adrianliz.smartthermostat.shared.infrastructure.config.Parameter;
import adrianliz.smartthermostat.shared.infrastructure.config.ParameterNotExist;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemperaturesPortCustomizer
    implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

  private final Parameter param;

  public TemperaturesPortCustomizer(Parameter param) {
    this.param = param;
  }

  @Override
  public void customize(ConfigurableWebServerFactory factory) {
    try {
      factory.setPort(param.getInt("TEMPERATURES_SERVER_PORT"));
    } catch (ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }
}
