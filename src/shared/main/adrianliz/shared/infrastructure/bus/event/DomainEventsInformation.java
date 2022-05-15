package adrianliz.shared.infrastructure.bus.event;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.event.DomainEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.reflections.Reflections;

@Service
public final class DomainEventsInformation {
  HashMap<String, Class<? extends DomainEvent>> indexedDomainEvents;

  public DomainEventsInformation() {
    final Reflections reflections = new Reflections(Utils.ORGANIZATION_NAME);
    final Set<Class<? extends DomainEvent>> classes = reflections.getSubTypesOf(DomainEvent.class);

    try {
      indexedDomainEvents = formatEvents(classes);
    } catch (final NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public Class<? extends DomainEvent> forName(final String name) {
    return indexedDomainEvents.get(name);
  }

  public String forClass(final Class<? extends DomainEvent> domainEventClass) {
    return indexedDomainEvents.entrySet().stream()
        .filter(entry -> Objects.equals(entry.getValue(), domainEventClass))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse("");
  }

  private HashMap<String, Class<? extends DomainEvent>> formatEvents(
      final Set<Class<? extends DomainEvent>> domainEvents)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {

    final HashMap<String, Class<? extends DomainEvent>> events = new HashMap<>();

    for (final Class<? extends DomainEvent> domainEvent : domainEvents) {
      final DomainEvent nullInstance = domainEvent.getConstructor().newInstance();

      events.put((String) domainEvent.getMethod("eventName").invoke(nullInstance), domainEvent);
    }

    return events;
  }
}
