package adrianliz.smartthermostat.shared.domain.bus.query;

public interface QueryBus {
  <R> R ask(final Query query) throws QueryHandlerExecutionError;
}
