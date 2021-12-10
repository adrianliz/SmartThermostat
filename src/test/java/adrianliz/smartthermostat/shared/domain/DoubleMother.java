package adrianliz.smartthermostat.shared.domain;

public final class DoubleMother {

  public static Double random() {
    return MotherCreator.random().random().nextDouble();
  }

  public static Double minusTwenty() {
    return -20.0;
  }

  public static Double fifty() {
    return 50.0;
  }
}
