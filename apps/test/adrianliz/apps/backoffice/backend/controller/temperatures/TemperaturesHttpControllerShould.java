package adrianliz.apps.backoffice.backend.controller.temperatures;

import adrianliz.apps.backoffice.backend.BackofficeApplicationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class TemperaturesHttpControllerShould extends BackofficeApplicationTestCase {

  @Test
  void registrar_a_valid_temperature() throws Exception {
    final String temperature =
        "{\"id\": \"99ad55f5-6eab-4d73-b383-c63268e251e8\", \"sensorId\": \"99ad55f5-6eab-4d73-b383-c63268e251e8\","
            + "\"celsiusRegistered\": 20.0, \"timestamp\": 800000000}";

    assertRequestWithBody(
        HttpMethod.POST.name(), "/temperatures", temperature, HttpStatus.OK.value());
  }
}
