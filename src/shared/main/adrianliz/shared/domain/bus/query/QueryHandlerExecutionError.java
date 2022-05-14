package adrianliz.shared.domain.bus.query;

public final class QueryHandlerExecutionError extends RuntimeException {

  public QueryHandlerExecutionError(final Throwable cause) {
    super(cause);
  }
}
