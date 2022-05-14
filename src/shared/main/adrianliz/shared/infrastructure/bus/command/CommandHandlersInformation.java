package adrianliz.shared.infrastructure.bus.command;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.command.Command;
import adrianliz.shared.domain.bus.command.CommandHandler;
import adrianliz.shared.domain.bus.command.CommandNotRegisteredError;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;

@Service
public final class CommandHandlersInformation {

  HashMap<Class<? extends Command>, Class<? extends CommandHandler>> indexedCommandHandlers;

  public CommandHandlersInformation() {
    final Reflections reflections = new Reflections("adrianliz.smartthermostat");
    final Set<Class<? extends CommandHandler>> classes =
        reflections.getSubTypesOf(CommandHandler.class);

    indexedCommandHandlers = formatHandlers(classes);
  }

  public Class<? extends CommandHandler> search(final Class<? extends Command> commandClass)
      throws CommandNotRegisteredError {
    final Class<? extends CommandHandler> commandHandlerClass =
        indexedCommandHandlers.get(commandClass);

    if (null == commandHandlerClass) {
      throw new CommandNotRegisteredError(commandClass);
    }

    return commandHandlerClass;
  }

  private HashMap<Class<? extends Command>, Class<? extends CommandHandler>> formatHandlers(
      final Set<Class<? extends CommandHandler>> commandHandlers) {
    final HashMap<Class<? extends Command>, Class<? extends CommandHandler>> handlers =
        new HashMap<>();

    for (final Class<? extends CommandHandler> handler : commandHandlers) {
      final ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];
      final Class<? extends Command> commandClass =
          (Class<? extends Command>) paramType.getActualTypeArguments()[0];

      handlers.put(commandClass, handler);
    }

    return handlers;
  }
}
