package adrianliz.shared.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class EnvironmentConfig {

  ResourceLoader resourceLoader;

  public EnvironmentConfig(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public Dotenv dotenv() {
    final Resource resource = resourceLoader.getResource("classpath:/.env.dev");

    return Dotenv.configure()
        .directory("/")
        .filename(resource.exists() ? ".env.dev" : ".env")
        .load();
  }
}
