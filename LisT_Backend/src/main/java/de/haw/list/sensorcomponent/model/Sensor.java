/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.haw.list.sensorcomponent.util.LocationType;
import de.haw.list.sensorcomponent.util.SensorType;

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
	
	//TODO lyp 18.10.2017 ggf. auch als Enum anlegen
	private LocationType location;
	
	private float max;
	
	private float min;
	
	@OneToMany(fetch = FetchType.EAGER)
	//	@JoinColumn(name = "person_id")
	private List<SensorValue> sensorValues;

	public Sensor() {}

	public Sensor(String techId, SensorType sensorType, String name, LocationType location, float max, float min) {
		this.techId = techId;
		this.sensorType = sensorType;
		this.name = name;
		this.location = location;
		this.max = max;
		this.min = min;
		this.sensorValues = new ArrayList<>();
	}
	
	


}
