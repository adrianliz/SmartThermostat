package adrianliz.smartthermostat.apps.temperatures.backend.controller;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandBus;
import adrianliz.smartthermostat.temperatures.application.registrar.RegistrarTemperatureCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Service
public final class TemperaturesMqttController implements MqttCallback {

  private static final String BROKER_URI = "BROKER_URI";
  private static final String CLIENT_ID = "CLIENT_ID";
  private static final String TOPIC = "TOPIC";

  private final CommandBus commandBus;
  private final IMqttClient client;
  private final MqttConnectOptions options;
  private String defaultTopic;

  public TemperaturesMqttController(CommandBus commandBus, @Value("${mqtt_config}") Resource mqttConfig) throws IOException, MqttException {
    this.commandBus = commandBus;
    this.client = createClient(mqttConfig);
    this.client.setCallback(this);

    this.options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setAutomaticReconnect(true);
  }

  private IMqttClient createClient(Resource mqttConfig) throws IOException, MqttException {
    Gson gson = new Gson();
    Map<String, String> json = gson.fromJson(
      new InputStreamReader(mqttConfig.getInputStream(), StandardCharsets.UTF_8),
      new TypeToken<Map<String, String>>() {}.getType()
    );

    this.defaultTopic = json.get(TOPIC);
    return new MqttClient(json.get(BROKER_URI), json.get(CLIENT_ID));
  }

  private void connect() throws MqttException {
    if (!client.isConnected()) {
      client.connect(options);
    }
  }

  void subscribe(String topic) throws MqttException {
    this.connect();
    client.subscribe(topic == null ? defaultTopic : topic);
  }

  @Override
  public void connectionLost(Throwable throwable) {
    System.err.println("Connection to MQTT broker was lost");
    System.err.println("Cause -->" + throwable.getCause().getMessage());
  }

  @Override
  public void messageArrived(String s, MqttMessage message) {
    RegistrarTemperatureCommand command = new Gson().fromJson(new String(message.getPayload()), RegistrarTemperatureCommand.class);

    commandBus.dispatch(command);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
}
