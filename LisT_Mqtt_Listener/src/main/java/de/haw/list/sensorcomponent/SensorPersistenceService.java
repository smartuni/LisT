/**
 * 
 */
package de.haw.list.sensorcomponent;

import java.time.LocalDateTime;

import org.json.JSONObject;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.JsonMalFormedException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

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
	 * @throws SensorNotFoundException 
	 * @throws JsonMalFormedException 
	 */
	public SensorValue addSensorValue(JSONObject jsonObject) throws SensorNotFoundException, JsonMalFormedException;

}		
