package adrianliz.smartthermostat.apps.temperatures.backend.controller;

import adrianliz.smartthermostat.shared.domain.DomainError;
import adrianliz.smartthermostat.shared.domain.Utils;
import adrianliz.smartthermostat.shared.domain.bus.query.QueryBus;
import adrianliz.smartthermostat.shared.domain.bus.query.QueryHandlerExecutionError;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;
import adrianliz.smartthermostat.temperatures.application.search_last.SearchLastTemperatureQuery;
import adrianliz.smartthermostat.temperatures.domain.TemperatureRegistered;
import java.io.Serializable;
import java.util.HashMap;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
@adrianliz.smartthermostat.shared.domain.bus.event.DomainEventSubscriber({
  TemperatureRegistered.class
})
public final class TemperaturesWebSocketController {

  private final QueryBus queryBus;
  private final SimpMessagingTemplate template;

  public TemperaturesWebSocketController(QueryBus queryBus, SimpMessagingTemplate template) {
    this.queryBus = queryBus;
    this.template = template;
  }

  private HashMap<String, Serializable> getLastTemperature() {
    try {
      TemperatureResponse temperature = queryBus.ask(new SearchLastTemperatureQuery());

      return new HashMap<>() {
        {
          put("id", temperature.id());
          put("sensorId", temperature.sensorId());
          put("celsiusRegistered", temperature.celsiusRegistered());
          put("timestamp", temperature.timestamp());
        }
      };
    } catch (QueryHandlerExecutionError ex) {
      Throwable throwable = ex.getCause();

      if (throwable instanceof DomainError) {
        DomainError error = (DomainError) throwable;

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
  public void handleSubscribeEvent(SessionSubscribeEvent event) {
    template.convertAndSendToUser(
        event.getUser().getName(), "/temperatures/last", getLastTemperature());
  }

  public void on(TemperatureRegistered event) {
    template.convertAndSend("/temperatures/last", event.celsisusRegistered());
  }
}
