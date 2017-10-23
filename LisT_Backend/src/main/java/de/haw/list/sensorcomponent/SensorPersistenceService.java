/**
 * 
 */
package de.haw.list.sensorcomponent;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;

/**
 * Persistiert Sensoren und Sensordaten.
 * @author Lydia Pflug
 * 18.10.2017
 */
public interface SensorPersistenceService {
	
	/**
	 * Erstellt neuen Sensor.
	 * 
	 * @param sensor Sensor, der erstellt werden soll
	 * @return Sensor, der erstellt worden ist
	 */
	public Sensor createSensor(Sensor sensor);
	
	/**
	 * Fuegt neuen Sensorwert hinzu.
	 * 
	 * @param sensorId ID des Sensors
	 * @param value Wert des Sensors
	 * @return Sensorwert
	 */
	public SensorValue addSensorValue(int sensorId, float value);

}		
