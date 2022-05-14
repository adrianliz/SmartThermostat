package adrianliz.apps;

import adrianliz.apps.backoffice.backend.BackofficeBackendApplication;
import adrianliz.shared.infrastructure.cli.ConsoleCommand;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

public class Starter {
  public static void main(final String[] args) {
    if (args.length < 2) {
      throw new RuntimeException("There are not enough arguments");
    }

    final String applicationName = args[0];
    final String commandName = args[1];
    final boolean isServerCommand = commandName.equals("server");

    ensureApplicationExist(applicationName);
    ensureCommandExist(applicationName, commandName);

    final Class<?> applicationClass = applications().get(applicationName);

    final SpringApplication app = new SpringApplication(applicationClass);

    if (!isServerCommand) {
      app.setWebApplicationType(WebApplicationType.NONE);
    }

    final ConfigurableApplicationContext context = app.run(args);

    if (!isServerCommand) {
      final ConsoleCommand command =
          (ConsoleCommand) context.getBean(commands().get(applicationName).get(commandName));

      command.execute(Arrays.copyOfRange(args, 2, args.length));
    }
  }

  private static void ensureApplicationExist(final String applicationName) {
    if (!applications().containsKey(applicationName)) {
      throw new RuntimeException(
          String.format(
              "The application <%s> doesn't exist. Valids:\n- %s",
              applicationName, String.join("\n- ", applications().keySet())));
    }
  }

  private static void ensureCommandExist(final String applicationName, final String commandName) {
    if (!"server".equals(commandName) && !existCommand(applicationName, commandName)) {
      throw new RuntimeException(
          String.format(
              "The command <%s> for application <%s> doesn't exist. Valids (application.command):\n- api\n- %s",
              commandName,
              applicationName,
              String.join("\n- ", commands().get(applicationName).keySet())));
    }
  }

  private static HashMap<String, Class<?>> applications() {
    final HashMap<String, Class<?>> applications = new HashMap<>();
    applications.put("backoffice_backend", BackofficeBackendApplication.class);

    return applications;
  }

  private static HashMap<String, HashMap<String, Class<?>>> commands() {
    final HashMap<String, HashMap<String, Class<?>>> commands = new HashMap<>();
    commands.put("backoffice_backend", BackofficeBackendApplication.commands());

    return commands;
  }

  private static Boolean existCommand(final String applicationName, final String commandName) {
    final HashMap<String, HashMap<String, Class<?>>> commands = commands();

    return commands.containsKey(applicationName)
        && commands.get(applicationName).containsKey(commandName);
  }
}
