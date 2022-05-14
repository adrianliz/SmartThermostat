package adrianliz.apps.backoffice.backend.controller.temperatures;

import adrianliz.backoffice.temperatures.application.registrar.RegistrarTemperatureCommand;
import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.command.CommandBus;
import adrianliz.shared.infrastructure.config.Parameter;
import adrianliz.shared.infrastructure.config.ParameterNotExist;
import com.google.gson.Gson;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.*;

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
