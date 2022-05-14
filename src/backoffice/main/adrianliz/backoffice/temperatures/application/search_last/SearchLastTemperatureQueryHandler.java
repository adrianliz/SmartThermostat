package adrianliz.backoffice.temperatures.application.search_last;

import adrianliz.backoffice.temperatures.application.TemperatureResponse;
import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.bus.query.QueryHandler;

@Service
public final class SearchLastTemperatureQueryHandler
    implements QueryHandler<SearchLastTemperatureQuery, TemperatureResponse> {

  private final LastTemperatureSearcher searcher;

  public SearchLastTemperatureQueryHandler(final LastTemperatureSearcher searcher) {
    this.searcher = searcher;
  }

  @Override
  public TemperatureResponse handle(final SearchLastTemperatureQuery query) {
    return searcher.search();
  }
}
