package adrianliz.shared.infrastructure.bus.event;

import adrianliz.shared.domain.Service;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.event.DomainEvent;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@Service
public final class DomainEventJsonDeserializer {
  private final DomainEventsInformation information;

  public DomainEventJsonDeserializer(final DomainEventsInformation information) {
    this.information = information;
  }

  public DomainEvent deserialize(final String body)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException,
          InstantiationException {
    final HashMap<String, Serializable> eventData = Utils.jsonDecode(body);
    final HashMap<String, Serializable> data =
        (HashMap<String, Serializable>) eventData.get("data");
    final HashMap<String, Serializable> attributes =
        (HashMap<String, Serializable>) data.get("attributes");
    final Class<? extends DomainEvent> domainEventClass =
        information.forName((String) data.get("type"));

    final DomainEvent nullInstance = domainEventClass.getConstructor().newInstance();

    final Method fromPrimitivesMethod =
        domainEventClass.getMethod(
            "fromPrimitives", String.class, HashMap.class, String.class, String.class);

    final Object domainEvent =
        fromPrimitivesMethod.invoke(
            nullInstance,
            attributes.get("id"),
            attributes,
            data.get("id"),
            data.get("occurred_on"));

    return (DomainEvent) domainEvent;
  }
}
