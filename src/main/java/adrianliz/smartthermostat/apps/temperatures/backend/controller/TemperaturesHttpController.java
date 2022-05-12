package adrianliz.smartthermostat.apps.temperatures.backend.controller;

import adrianliz.smartthermostat.shared.domain.DomainError;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandBus;
import adrianliz.smartthermostat.shared.domain.bus.query.QueryBus;
import adrianliz.smartthermostat.shared.infrastructure.spring.ApiController;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;
import adrianliz.smartthermostat.temperatures.application.registrar.RegistrarTemperatureCommand;
import adrianliz.smartthermostat.temperatures.application.search_last.SearchLastTemperatureQuery;
import adrianliz.smartthermostat.temperatures.domain.TemperatureNotExists;
import java.io.Serializable;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public final class TemperaturesHttpController extends ApiController {

  private final TemperaturesMqttController mqttController;

  public TemperaturesHttpController(
      final TemperaturesMqttController mqttController,
      final QueryBus queryBus,
      final CommandBus commandBus) {
    super(queryBus, commandBus);
    this.mqttController = mqttController;
  }

  @PostMapping(
      value = "/temperatures/enable-registrar",
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> enableRegistrar(
      @RequestParam(value = "topic", required = false) final String topic) {
    try {
      mqttController.subscribe(topic);
      return ResponseEntity.ok().build();
    } catch (final MqttException ex) {
      return ResponseEntity.internalServerError().body(ex.getMessage());
    }
  }

  @PostMapping(value = "/temperatures/", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> registrar(
      @RequestBody final RegistrarTemperatureCommand registrarTemperatureCommand) {

    super.dispatch(registrarTemperatureCommand);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/temperatures/last", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<HashMap<String, Serializable>> getLastTemperature() {
    final TemperatureResponse temperature = ask(new SearchLastTemperatureQuery());

    return ResponseEntity.ok()
        .body(
            new HashMap<>() {
              {
                put("id", temperature.id());
                put("sensorId", temperature.sensorId());
                put("celsiusRegistered", temperature.celsiusRegistered());
                put("timestamp", temperature.timestamp());
              }
            });
  }

  @Override
  public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return new HashMap<>() {
      {
        put(TemperatureNotExists.class, HttpStatus.NOT_FOUND);
      }
    };
  }
}
