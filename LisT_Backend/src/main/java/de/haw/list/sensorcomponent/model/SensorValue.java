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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Modell fuer Sensorwert.
 * 
 * @author Lydia Pflug
 * 19.10.2017
 */
@Entity
@Table(name = "sensorvalue")
public class SensorValue {
	
	@Id
	@GeneratedValue
	private int id;
	
	//@OneToMany
	@ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
//	@Column(name = "sensorid")
//	@JoinColumn(name = "sensor_id")
	private Sensor sensor;
	
	private double value;
	
    @Column(name = "`timestamp`")
	private LocalDateTime timestamp;
	
	public SensorValue(Sensor sensor, double value, LocalDateTime timestamp) {
		this.sensor = sensor;
		this.value = value;
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}
	
	
	

}
