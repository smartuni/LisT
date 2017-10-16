/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.haw.list.sensorcomponent.util.SensorType;

/**
 * Modelliert Sensor.
 * 
 * @author Lydia Pflug 09.10.2017
 */
@Entity
public class Sensor {

	@Id
	@GeneratedValue
	private int id;
	
	private SensorType sensorType;
	
	private String Name;
	

}
