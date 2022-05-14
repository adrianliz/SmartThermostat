package adrianliz.shared.domain.bus.command;

public interface CommandHandler<T extends Command> {
  void handle(final T command);
}
