package adrianliz.apps.backoffice.backend.config;

import adrianliz.shared.infrastructure.spring.ApiExceptionMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class BackofficeBackendConfiguration {
  private final RequestMappingHandlerMapping mapping;

  public BackofficeBackendConfiguration(
      @Autowired(required = false) final RequestMappingHandlerMapping mapping) {
    this.mapping = mapping;
  }

  @Bean
  public FilterRegistrationBean<ApiExceptionMiddleware> basicHttpAuthMiddleware() {
    final FilterRegistrationBean<ApiExceptionMiddleware> registrationBean =
        new FilterRegistrationBean<>();

    registrationBean.setFilter(new ApiExceptionMiddleware(mapping));

    return registrationBean;
  }
}
