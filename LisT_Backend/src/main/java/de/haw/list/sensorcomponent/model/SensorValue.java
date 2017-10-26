/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
	
	private double value;
	
//	@JsonSerialize(using = LocalDateTimeSerializer.class)
//	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "`timestamp`")
	private LocalDateTime timestamp;
	
	public SensorValue(Sensor sensor, double value, LocalDateTime timestamp) {
		this.sensor = sensor;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	
	

}
