package adrianliz.smartthermostat.shared.domain.bus.command;

public final class CommandHandlerExecutionError extends RuntimeException {

  public CommandHandlerExecutionError(final Throwable cause) {
    super(cause);
  }
}
