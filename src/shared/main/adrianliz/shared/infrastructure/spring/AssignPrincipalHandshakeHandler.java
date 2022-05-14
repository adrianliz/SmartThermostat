package adrianliz.shared.infrastructure.spring;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public final class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {
  private static final String ATTR_PRINCIPAL = "__principal__";

  @Override
  protected Principal determineUser(
      final ServerHttpRequest request,
      final WebSocketHandler wsHandler,
      final Map<String, Object> attributes) {
    final String name;
    if (!attributes.containsKey(ATTR_PRINCIPAL)) {
      name = generateRandomUsername();
      attributes.put(ATTR_PRINCIPAL, name);
    } else {
      name = (String) attributes.get(ATTR_PRINCIPAL);
    }
    return new Principal() {
      @Override
      public String getName() {
        return name;
      }
    };
  }

  private String generateRandomUsername() {
    return UUID.randomUUID().toString();
  }
}
