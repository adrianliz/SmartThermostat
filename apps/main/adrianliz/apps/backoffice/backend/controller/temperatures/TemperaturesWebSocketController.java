package adrianliz.apps.backoffice.backend.controller.temperatures;

import adrianliz.backoffice.temperatures.application.TemperatureResponse;
import adrianliz.backoffice.temperatures.application.search_last.SearchLastTemperatureQuery;
import adrianliz.backoffice.temperatures.domain.TemperatureRegistered;
import adrianliz.shared.domain.DomainError;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.event.DomainEventSubscriber;
import adrianliz.shared.domain.bus.query.QueryBus;
import adrianliz.shared.domain.bus.query.QueryHandlerExecutionError;
import java.io.Serializable;
import java.util.HashMap;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
@DomainEventSubscriber({TemperatureRegistered.class})
public final class TemperaturesWebSocketController {

  private final QueryBus queryBus;
  private final SimpMessagingTemplate template;

  public TemperaturesWebSocketController(
      final QueryBus queryBus, final SimpMessagingTemplate template) {
    this.queryBus = queryBus;
    this.template = template;
  }

  private HashMap<String, Serializable> getLastTemperature() {
    try {
      final TemperatureResponse temperature = queryBus.ask(new SearchLastTemperatureQuery());

      return new HashMap<>() {
        {
          put("id", temperature.id());
          put("sensorId", temperature.sensorId());
          put("celsiusRegistered", temperature.celsiusRegistered());
          put("timestamp", temperature.timestamp());
        }
      };
    } catch (final QueryHandlerExecutionError ex) {
      final Throwable throwable = ex.getCause();

      if (throwable instanceof DomainError) {
        final DomainError error = (DomainError) throwable;

        return new HashMap<>() {
          {
            put("error_code", error.errorCode());
            put("message", error.errorMessage());
          }
        };
      }
      return new HashMap<>() {
        {
          put("error_code", Utils.toSnake(ex.getClass().toString()));
          put("message", ex.getMessage());
        }
      };
    }
  }

  @EventListener
  public void handleSubscribeEvent(final SessionSubscribeEvent event) {
    template.convertAndSendToUser(
        event.getUser().getName(), "/temperatures/last", getLastTemperature());
  }

  @EventListener
  public void on(final TemperatureRegistered event) {
    template.convertAndSend("/temperatures/last", event.toPrimitives());
  }
}
