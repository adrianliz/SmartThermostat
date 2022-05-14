package adrianliz.shared.infrastructure.bus.command;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.command.Command;
import adrianliz.shared.domain.bus.command.CommandBus;
import adrianliz.shared.domain.bus.command.CommandHandler;
import adrianliz.shared.domain.bus.command.CommandHandlerExecutionError;
import org.springframework.context.ApplicationContext;

@Service
public final class InMemoryCommandBus implements CommandBus {

  private final CommandHandlersInformation information;
  private final ApplicationContext context;

  public InMemoryCommandBus(
      final CommandHandlersInformation information, final ApplicationContext context) {
    this.information = information;
    this.context = context;
  }

  @Override
  public void dispatch(final Command command) throws CommandHandlerExecutionError {
    try {
      final Class<? extends CommandHandler> commandHandlerClass =
          information.search(command.getClass());

      final CommandHandler handler = context.getBean(commandHandlerClass);

      handler.handle(command);
    } catch (final Throwable error) {
      throw new CommandHandlerExecutionError(error);
    }
  }
}
