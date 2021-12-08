package adrianliz.smartthermostat.temperatures.domain;

import adrianliz.smartthermostat.shared.domain.LongMother;

public final class TimestampMother {
	public static Timestamp create(Long value) {
		return new Timestamp(value);
	}

	public static Timestamp random() {
		return create(LongMother.random());
	}
}
