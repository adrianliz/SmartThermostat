package adrianliz.smartthermostat.apps.temperatures.backend.config;

import adrianliz.smartthermostat.shared.infrastructure.config.Parameter;
import adrianliz.smartthermostat.shared.infrastructure.config.ParameterNotExist;
import adrianliz.smartthermostat.shared.infrastructure.spring.AssignPrincipalHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class TemperaturesWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
  final Parameter config;

  public TemperaturesWebSocketConfiguration(Parameter config) {
    this.config = config;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    try {
      registry
          .addEndpoint(config.get("TEMPERATURES_STOMP_ENDPOINT"))
          .setHandshakeHandler(new AssignPrincipalHandshakeHandler())
          .setAllowedOriginPatterns("*")
          .withSockJS();
    } catch (ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    try {
      registry.enableSimpleBroker(config.get("TEMPERATURES_STOMP_TOPIC"));
      registry.setApplicationDestinationPrefixes(config.get("TEMPERATURES_STOMP_APP_PREFIX"));
      registry.setUserDestinationPrefix(config.get("TEMPERATURES_STOMP_USER_PREFIX"));
    } catch (ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }
}
