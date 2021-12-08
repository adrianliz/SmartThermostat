package adrianliz.smartthermostat.temperatures.application.create;

import adrianliz.smartthermostat.shared.domain.bus.command.Command;

public final class RegistrarTemperatureCommand implements Command {
	private final String id;
	private final String sensorId;
	private final double celsiusRegistered;
	private final long timestamp;

	public RegistrarTemperatureCommand(String id, String sensorId, double celsiusRegistered, long timestamp) {
		this.id = id;
		this.sensorId = sensorId;
		this.celsiusRegistered = celsiusRegistered;
		this.timestamp = timestamp;
	}

	public String id() {
		return id;
	}

	public String sensorId() {
		return sensorId;
	}

	public double celsiusRegistered() {
		return celsiusRegistered;
	}

	public long timestamp() {
		return timestamp;
	}
}
