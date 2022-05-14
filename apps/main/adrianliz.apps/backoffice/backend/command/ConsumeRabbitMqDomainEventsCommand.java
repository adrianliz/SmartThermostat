package adrianliz.apps.backoffice.backend.command;

import adrianliz.shared.infrastructure.bus.event.rabbit.RabbitMqDomainEventsConsumer;
import adrianliz.shared.infrastructure.cli.ConsoleCommand;

public final class ConsumeRabbitMqDomainEventsCommand extends ConsoleCommand {
  private final RabbitMqDomainEventsConsumer consumer;

  public ConsumeRabbitMqDomainEventsCommand(final RabbitMqDomainEventsConsumer consumer) {
    this.consumer = consumer;
  }

  @Override
  public void execute(final String[] args) {
    consumer.consume();
  }
}
