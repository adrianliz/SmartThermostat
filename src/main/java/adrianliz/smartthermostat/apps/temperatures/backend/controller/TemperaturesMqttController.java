package adrianliz.smartthermostat.apps.temperatures.backend.controller;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandBus;
import adrianliz.smartthermostat.shared.infrastructure.config.Parameter;
import adrianliz.smartthermostat.shared.infrastructure.config.ParameterNotExist;
import adrianliz.smartthermostat.temperatures.application.registrar.RegistrarTemperatureCommand;
import com.google.gson.Gson;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Service
public final class TemperaturesMqttController implements MqttCallback {

  private final CommandBus commandBus;
  private final IMqttClient client;
  private final MqttConnectOptions options;
  private final Gson jsonClient;
  private String defaultTopic;

  public TemperaturesMqttController(final CommandBus commandBus, final Parameter config)
      throws ParameterNotExist, MqttException {
    this.commandBus = commandBus;

    client = createClient(config);
    client.setCallback(this);

    options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setAutomaticReconnect(true);

    jsonClient = new Gson();
  }

  private IMqttClient createClient(final Parameter config) throws ParameterNotExist, MqttException {
    defaultTopic = config.get("TEMPERATURES_MQTT_TOPIC");
    return new MqttClient(
        config.get("TEMPERATURES_MQTT_BROKER_URI"), config.get("TEMPERATURES_MQTT_CLIENT_ID"));
  }

  private void connect() throws MqttException {
    if (!client.isConnected()) {
      client.connect(options);
    }
  }

  void subscribe(final String topic) throws MqttException {
    connect();
    client.subscribe(topic == null ? defaultTopic : topic);
  }

  @Override
  public void connectionLost(final Throwable throwable) {
    System.err.println("Connection to MQTT broker was lost");
    System.err.println("Cause -->" + throwable.getCause().getMessage());
  }

  @Override
  public void messageArrived(final String s, final MqttMessage message) throws IOException {
    final RegistrarTemperatureCommand command =
        jsonClient.fromJson(new String(message.getPayload()), RegistrarTemperatureCommand.class);

    commandBus.dispatch(command);
  }

  @Override
  public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken) {}
}
