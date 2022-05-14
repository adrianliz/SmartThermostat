package adrianliz.shared.domain.bus.command;

public interface CommandBus {
  void dispatch(final Command command) throws CommandHandlerExecutionError;
}
