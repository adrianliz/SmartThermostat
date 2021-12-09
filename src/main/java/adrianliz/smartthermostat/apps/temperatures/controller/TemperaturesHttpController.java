package adrianliz.smartthermostat.apps.temperatures.controller;

import adrianliz.smartthermostat.shared.domain.bus.query.QueryBus;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;
import adrianliz.smartthermostat.temperatures.application.search_last.SearchLastTemperatureQuery;
import java.io.Serializable;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public final class TemperaturesHttpController {

  private final TemperaturesMqttController mqttController;
  private final QueryBus queryBus;

  public TemperaturesHttpController(TemperaturesMqttController mqttController, QueryBus queryBus) {
    this.mqttController = mqttController;
    this.queryBus = queryBus;
  }

  @PostMapping(value = "/temperatures/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> subscribe(@RequestParam(value = "topic", required = false) String topic) {
    try {
      this.mqttController.subscribe(topic);
      return ResponseEntity.ok().build();
    } catch (MqttException ex) {
      return ResponseEntity.internalServerError().body(ex.getMessage());
    }
  }

  @GetMapping(value = "/temperatures/last", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<HashMap<String, Serializable>> getLast() {
    TemperatureResponse temperature = queryBus.ask(new SearchLastTemperatureQuery());

    return ResponseEntity
      .ok()
      .body(
        new HashMap<>() {
          {
            put("id", temperature.id());
            put("sensorId", temperature.sensorId());
            put("celsiusRegistered", temperature.celsiusRegistered());
            put("timestamp", temperature.timestamp());
          }
        }
      );
  }
}
