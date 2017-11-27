package de.haw.list.sensorcomponent.util;

/**
 * Exception fuer nicht verfuegbare Werte eines Sensors.
 * 
 * @author Lydia Pflug
 * 25.10.2017
 */
public final class NoValueAvailableException extends Exception {

	private static final long serialVersionUID = -8929334169175807469L;

	public NoValueAvailableException(String sensorId) {
		super("Es gibt keine Werte fuer den Sensor " + sensorId + " .");
	}

}
