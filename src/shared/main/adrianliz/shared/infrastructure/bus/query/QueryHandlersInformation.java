package adrianliz.shared.infrastructure.bus.query;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.query.Query;
import adrianliz.shared.domain.bus.query.QueryHandler;
import adrianliz.shared.domain.bus.query.QueryNotRegisteredError;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;

@Service
public final class QueryHandlersInformation {

  HashMap<Class<? extends Query>, Class<? extends QueryHandler>> indexedQueryHandlers;

  public QueryHandlersInformation() {
    final Reflections reflections = new Reflections(Utils.ORGANIZATION_NAME);
    final Set<Class<? extends QueryHandler>> classes =
        reflections.getSubTypesOf(QueryHandler.class);

    indexedQueryHandlers = formatHandlers(classes);
  }

  public Class<? extends QueryHandler> search(final Class<? extends Query> queryClass)
      throws QueryNotRegisteredError {
    final Class<? extends QueryHandler> queryHandlerClass = indexedQueryHandlers.get(queryClass);

    if (null == queryHandlerClass) {
      throw new QueryNotRegisteredError(queryClass);
    }

    return queryHandlerClass;
  }

  private HashMap<Class<? extends Query>, Class<? extends QueryHandler>> formatHandlers(
      final Set<Class<? extends QueryHandler>> queryHandlers) {
    final HashMap<Class<? extends Query>, Class<? extends QueryHandler>> handlers = new HashMap<>();

    for (final Class<? extends QueryHandler> handler : queryHandlers) {
      final ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];
      final Class<? extends Query> queryClass =
          (Class<? extends Query>) paramType.getActualTypeArguments()[0];

      handlers.put(queryClass, handler);
    }

    return handlers;
  }
}
