package adrianliz.smartthermostat.apps.temperatures.backend.config;

import adrianliz.smartthermostat.shared.infrastructure.spring.ApiExceptionMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class TemperaturesBackendConfiguration {
  private final RequestMappingHandlerMapping mapping;

  public TemperaturesBackendConfiguration(
      @Autowired(required = false) RequestMappingHandlerMapping mapping) {
    this.mapping = mapping;
  }

  @Bean
  public FilterRegistrationBean<ApiExceptionMiddleware> basicHttpAuthMiddleware() {
    FilterRegistrationBean<ApiExceptionMiddleware> registrationBean =
        new FilterRegistrationBean<>();

    registrationBean.setFilter(new ApiExceptionMiddleware(mapping));

    return registrationBean;
  }
}
