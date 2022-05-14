package adrianliz.shared.infrastructure;

import static org.mockito.Mockito.*;

import adrianliz.shared.domain.UuidGenerator;
import adrianliz.shared.domain.bus.event.DomainEvent;
import adrianliz.shared.domain.bus.event.EventBus;
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

  public void shouldHavePublished(final List<DomainEvent> domainEvents) {
    verify(eventBus, atLeastOnce()).publish(domainEvents);
  }

  public void shouldHavePublished(final DomainEvent domainEvent) {
    shouldHavePublished(Collections.singletonList(domainEvent));
  }

  public void shouldGenerateUuid(final String uuid) {
    when(uuidGenerator.generate()).thenReturn(uuid);
  }

  public void shouldGenerateUuids(final String uuid, final String... others) {
    when(uuidGenerator.generate()).thenReturn(uuid, others);
  }
}
