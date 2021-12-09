package adrianliz.smartthermostat.temperatures.application.search_last;

import adrianliz.smartthermostat.shared.domain.Service;
import adrianliz.smartthermostat.shared.domain.bus.query.QueryHandler;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;

@Service
public final class SearchLastTemperatureQueryHandler
  implements QueryHandler<SearchLastTemperatureQuery, TemperatureResponse> {

  private final LastTemperatureSearcher searcher;

  public SearchLastTemperatureQueryHandler(LastTemperatureSearcher searcher) {
    this.searcher = searcher;
  }

  @Override
  public TemperatureResponse handle(SearchLastTemperatureQuery query) {
    return searcher.search();
  }
}
