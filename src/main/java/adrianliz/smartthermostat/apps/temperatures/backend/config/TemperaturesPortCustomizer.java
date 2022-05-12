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

  public TemperaturesPortCustomizer(final Parameter param) {
    this.param = param;
  }

  @Override
  public void customize(final ConfigurableWebServerFactory factory) {
    try {
      factory.setPort(param.getInt("TEMPERATURES_SERVER_PORT"));
    } catch (final ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }
}
