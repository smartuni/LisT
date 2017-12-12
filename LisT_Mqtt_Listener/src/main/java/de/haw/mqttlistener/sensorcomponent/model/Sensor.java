/**
 * 
 */
package de.haw.mqttlistener.sensorcomponent.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.haw.mqttlistener.util.LocationType;
import de.haw.mqttlistener.util.SensorType;


/**
 * Modelliert Sensor mit Grenzwerten.
 * 
 * @author Lydia Pflug 09.10.2017
 */
@Entity
public class Sensor {

	@Id
	@GeneratedValue
	private int id;
	
	private String techId;
	
	private SensorType sensorType;
	
	private String name;
	
	private LocationType location;
	
	private float max;
	
	private float min;
	
	public Sensor() {}

	public Sensor(String techId, SensorType sensorType, String name, LocationType location, float max, float min) {
		this.techId = techId;
		this.sensorType = sensorType;
		this.name = name;
		this.location = location;
		this.max = max;
		this.min = min;
	}

	public int getId() {
		return id;
	}

	/**
	 * Getter fuer techId
	 * @return the techId
	 */
	public String getTechId() {
		return techId;
	}

	/**
	 * Getter fuer sensorType
	 * @return the sensorType
	 */
	public SensorType getSensorType() {
		return sensorType;
	}

	/**
	 * Getter fuer name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter fuer location
	 * @return the location
	 */
	public LocationType getLocation() {
		return location;
	}

	/**
	 * Getter fuer max
	 * @return the max
	 */
	public float getMax() {
		return max;
	}

	/**
	 * Getter fuer min
	 * @return the min
	 */
	public float getMin() {
		return min;
	}
	
	
	
	
	
	


}
