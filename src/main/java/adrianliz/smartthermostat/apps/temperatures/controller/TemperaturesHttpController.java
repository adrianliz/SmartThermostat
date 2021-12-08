package adrianliz.smartthermostat.apps.temperatures.controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/client")
@CrossOrigin(origins = "*")
public final class TemperaturesHttpController {
	private final TemperaturesMqttController mqttController;

	public TemperaturesHttpController(TemperaturesMqttController mqttController) {
		this.mqttController = mqttController;
	}

	@PostMapping(value = "/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> subscribe(@RequestParam("topic") String topic) {
		try {
			this.mqttController.subscribe(Optional.of(topic));
			return ResponseEntity.ok().build();
		} catch (MqttException ex) {
			return ResponseEntity.internalServerError().body(ex.getMessage());
		}
	}
}
