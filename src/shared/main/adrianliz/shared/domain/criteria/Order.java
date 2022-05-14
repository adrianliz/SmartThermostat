package adrianliz.shared.domain.criteria;

import java.util.Optional;

public final class Order {

  private final OrderBy orderBy;
  private final OrderType orderType;

  public Order(final OrderBy orderBy, final OrderType orderType) {
    this.orderBy = orderBy;
    this.orderType = orderType;
  }

  public static Order fromValues(final Optional<String> orderBy, final Optional<String> orderType) {
    return orderBy
        .map(order -> new Order(new OrderBy(order), OrderType.valueOf(orderType.orElse("ASC"))))
        .orElseGet(Order::none);
  }

  public static Order none() {
    return new Order(new OrderBy(""), OrderType.NONE);
  }

  public static Order desc(final String orderBy) {
    return new Order(new OrderBy(orderBy), OrderType.DESC);
  }

  public static Order asc(final String orderBy) {
    return new Order(new OrderBy(orderBy), OrderType.ASC);
  }

  public OrderBy orderBy() {
    return orderBy;
  }

  public OrderType orderType() {
    return orderType;
  }

  public boolean hasOrder() {
    return !orderType.isNone();
  }

  public String serialize() {
    return String.format("%s.%s", orderBy.value(), orderType.value());
  }
}
