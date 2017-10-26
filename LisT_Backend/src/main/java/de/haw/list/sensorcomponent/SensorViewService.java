package de.haw.list.sensorcomponent;

import java.util.List;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.NoValueAvailableException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Gibt Sensoren und Sensorwerte zurueck.
 * 
 * @author Lydia Pflug
 * 25.10.2017
 */
public interface SensorViewService {

	/**
	 * Gibt Sensor zurueck.
	 * 
	 * @param sensorId ID des Sensors
	 * @return Sensor
	 * @throws SensorNotFoundException 
	 */
	public Sensor getSensor(int sensorId) throws SensorNotFoundException;
	
	/**
	 * Gibt alle Sensoren zurueck.
	 * 
	 * @return Liste mit Sensoren
	 */
	public List<Sensor> getSensors();
	
	/**
	 * Gibt letzten Wert des Sensors zurueck.
	 * @param sensorId ID des Sensors
	 * @return Wert des Sensors
	 * @throws SensorNotFoundException 
	 * @throws NoValueAvailableException 
	 */
	public SensorValue getLastValueFromSensor(int sensorId) throws SensorNotFoundException, NoValueAvailableException;
	
	/**
	 * Gibt alle Werte des Sensors zurueck.
	 * @param sensorId ID des Sensors
	 * @return Liste mit Werten
	 * @throws SensorNotFoundException 
	 */
	public List<SensorValue> getValuesFromSensor(int sensorId) throws SensorNotFoundException;
}
