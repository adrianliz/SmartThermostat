package adrianliz.smartthermostat.shared.domain.bus.query;

public interface QueryHandler<Q extends Query, R extends Response> {
  R handle(final Q query);
}
