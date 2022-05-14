package adrianliz.shared.infrastructure.cli;

import adrianliz.shared.domain.Service;

@Service
public abstract class ConsoleCommand {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_GREEN = "\u001B[32m";

  public abstract void execute(String[] args);

  protected void log(final String text) {
    System.out.printf("%s%s%s%n", ANSI_GREEN, text, ANSI_RESET);
  }

  protected void info(final String text) {
    System.out.printf("%s%s%s%n", ANSI_CYAN, text, ANSI_RESET);
  }

  protected void error(final String text) {
    System.out.printf("%s%s%s%n", ANSI_RED, text, ANSI_RESET);
  }
}
