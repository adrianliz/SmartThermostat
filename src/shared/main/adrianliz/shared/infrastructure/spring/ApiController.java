package adrianliz.shared.infrastructure.spring;

import adrianliz.shared.domain.DomainError;
import adrianliz.shared.domain.bus.command.Command;
import adrianliz.shared.domain.bus.command.CommandBus;
import adrianliz.shared.domain.bus.command.CommandHandlerExecutionError;
import adrianliz.shared.domain.bus.query.Query;
import adrianliz.shared.domain.bus.query.QueryBus;
import adrianliz.shared.domain.bus.query.QueryHandlerExecutionError;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

public abstract class ApiController {

  private final QueryBus queryBus;
  private final CommandBus commandBus;

  public ApiController(final QueryBus queryBus, final CommandBus commandBus) {
    this.queryBus = queryBus;
    this.commandBus = commandBus;
  }

  protected void dispatch(final Command command) throws CommandHandlerExecutionError {
    commandBus.dispatch(command);
  }

  protected <R> R ask(final Query query) throws QueryHandlerExecutionError {
    return queryBus.ask(query);
  }

  public abstract HashMap<Class<? extends DomainError>, HttpStatus> errorMapping();
}
