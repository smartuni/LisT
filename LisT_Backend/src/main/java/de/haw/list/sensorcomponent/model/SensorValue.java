/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	@ManyToOne//(cascade = CascadeType.ALL)
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
	private Sensor sensor;
	
	private double value1;
	
	private double value2;
	
	private double value3;
	// TODO Stringliste
//	@ElementCollection
//	private List<Double> values = new ArrayList<>(); 
	  
	private LocalDateTime timestamp;
	
//	public SensorValue(Sensor sensor, List<Double> values, LocalDateTime timestamp) {
//		this.sensor = sensor;
//		this.values = values;
//		this.timestamp = timestamp;
//	}
	
	
	
	public SensorValue() {}

	public SensorValue(Sensor sensor, double value1, double value2, double value3, LocalDateTime timestamp) {
	this.sensor = sensor;
	this.value1 = value1;
	this.value2 = value2;
	this.value3 = value3;
	this.timestamp = timestamp;
}

	public int getId() {
		return id;
	}

	/**
	 * Getter fuer sensor
	 * @return the sensor
	 */
	public Sensor getSensor() {
		return sensor;
	}

//	/**
//	 * Getter fuer value
//	 * @return the value
//	 */
//	public List<Double> getValue() {
//		return new ArrayList<>(values);
//	}

	/**
	 * Getter fuer timestamp
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Getter fuer value1
	 * @return the value1
	 */
	public double getValue1() {
		return value1;
	}

	/**
	 * Getter fuer value2
	 * @return the value2
	 */
	public double getValue2() {
		return value2;
	}

	/**
	 * Getter fuer value3
	 * @return the value3
	 */
	public double getValue3() {
		return value3;
	}

	@Override
	public String toString() {
		return "SensorValue [id=" + id + ", sensor=" + sensor + ", value1=" + value1 + ", value2=" + value2
				+ ", value3=" + value3 + ", timestamp=" + timestamp + "]";
	}
	
	
	
	
	
	
	

}
