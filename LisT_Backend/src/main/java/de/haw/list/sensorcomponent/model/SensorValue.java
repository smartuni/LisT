/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.haw.list.sensorcomponent.util.LocalDateTimeDeserializer;
import de.haw.list.sensorcomponent.util.LocalDateTimeSerializer;

/**
 * Modell fuer Sensorwert.
 * 
 * @author Lydia Pflug
 * 19.10.2017
 */
@Entity
public class SensorValue {
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
//	@JoinColumn(name = "sensor_id")
	private Sensor sensor;
	
	private double wert;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;
	
	public SensorValue() {};
	
	

}