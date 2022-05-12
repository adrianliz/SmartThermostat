package adrianliz.smartthermostat.apps.temperatures.backend.command;

import adrianliz.smartthermostat.shared.infrastructure.bus.event.rabbit.RabbitMqDomainEventsConsumer;
import adrianliz.smartthermostat.shared.infrastructure.cli.ConsoleCommand;

public final class ConsumeRabbitMqDomainEventsCommand extends ConsoleCommand {
  private final RabbitMqDomainEventsConsumer consumer;

  public ConsumeRabbitMqDomainEventsCommand(RabbitMqDomainEventsConsumer consumer) {
    this.consumer = consumer;
  }

  @Override
  public void execute(String[] args) {
    consumer.consume();
  }
}
