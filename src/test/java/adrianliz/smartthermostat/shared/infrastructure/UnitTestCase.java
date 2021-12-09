package adrianliz.smartthermostat.shared.infrastructure;

import static org.mockito.Mockito.*;

import adrianliz.smartthermostat.shared.domain.UuidGenerator;
import adrianliz.smartthermostat.shared.domain.bus.event.DomainEvent;
import adrianliz.smartthermostat.shared.domain.bus.event.EventBus;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public abstract class UnitTestCase {

  protected EventBus eventBus;
  protected UuidGenerator uuidGenerator;

  @BeforeEach
  protected void setUp() {
    eventBus = mock(EventBus.class);
    uuidGenerator = mock(UuidGenerator.class);
  }

  public void shouldHavePublished(List<DomainEvent> domainEvents) {
    verify(eventBus, atLeastOnce()).publish(domainEvents);
  }

  public void shouldHavePublished(DomainEvent domainEvent) {
    shouldHavePublished(Collections.singletonList(domainEvent));
  }

  public void shouldGenerateUuid(String uuid) {
    when(uuidGenerator.generate()).thenReturn(uuid);
  }

  public void shouldGenerateUuids(String uuid, String... others) {
    when(uuidGenerator.generate()).thenReturn(uuid, others);
  }
}
