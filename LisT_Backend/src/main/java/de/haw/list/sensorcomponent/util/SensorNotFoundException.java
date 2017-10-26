/**
 * 
 */
package de.haw.list.sensorcomponent.util;

/**
 * Exception fuer nicht gefundenen Sensor.
 * 
 * @author Lydia Pflug
 * 25.10.2017
 */
public final class SensorNotFoundException extends Exception {

	private static final long serialVersionUID = -5217298212072275081L;

	public SensorNotFoundException(String sensorId) {
		super("Sensor " + sensorId + "konnte nicht gefunden werden.");
	}

}
