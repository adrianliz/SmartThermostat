package adrianliz.smartthermostat.apps.temperatures.backend.controller;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandBus;
import adrianliz.smartthermostat.shared.infrastructure.config.Parameter;
import adrianliz.smartthermostat.shared.infrastructure.config.ParameterNotExist;
import adrianliz.smartthermostat.temperatures.application.registrar.RegistrarTemperatureCommand;
import com.google.gson.Gson;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.*;

@Service
public final class TemperaturesMqttController implements MqttCallback {

  private final CommandBus commandBus;
  private final IMqttClient client;
  private final MqttConnectOptions options;
  private final TemperaturesWebSocketController temperaturesWebSocketController;
  private final Gson jsonClient;
  private String defaultTopic;

  public TemperaturesMqttController(CommandBus commandBus,
                                    TemperaturesWebSocketController temperaturesWebSocketController,
                                    Parameter config) throws ParameterNotExist, MqttException {
    this.commandBus = commandBus;
    this.temperaturesWebSocketController = temperaturesWebSocketController;

    this.client = createClient(config);
    this.client.setCallback(this);

    this.options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setAutomaticReconnect(true);

    this.jsonClient = new Gson();
  }

  private IMqttClient createClient(Parameter config) throws ParameterNotExist, MqttException {
    this.defaultTopic = config.get("TEMPERATURES_MQTT_TOPIC");
    return new MqttClient(config.get("TEMPERATURES_MQTT_BROKER_URI"), config.get("TEMPERATURES_MQTT_CLIENT_ID"));
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
  public void messageArrived(String s, MqttMessage message) throws IOException {
    RegistrarTemperatureCommand command =
      jsonClient.fromJson(new String(message.getPayload()), RegistrarTemperatureCommand.class);

    commandBus.dispatch(command);
    //TODO do with event publisher
    temperaturesWebSocketController.sendLastTemperature();
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
  }
}
