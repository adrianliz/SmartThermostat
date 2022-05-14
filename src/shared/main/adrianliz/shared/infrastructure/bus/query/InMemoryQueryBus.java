package adrianliz.shared.infrastructure.bus.query;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.query.*;
import org.springframework.context.ApplicationContext;

@Service
public final class InMemoryQueryBus implements QueryBus {

  private final QueryHandlersInformation information;
  private final ApplicationContext context;

  public InMemoryQueryBus(
      final QueryHandlersInformation information, final ApplicationContext context) {
    this.information = information;
    this.context = context;
  }

  @Override
  public Response ask(final Query query) throws QueryHandlerExecutionError {
    try {
      final Class<? extends QueryHandler> queryHandlerClass = information.search(query.getClass());

      final QueryHandler handler = context.getBean(queryHandlerClass);

      return handler.handle(query);
    } catch (final Throwable error) {
      throw new QueryHandlerExecutionError(error);
    }
  }
}
