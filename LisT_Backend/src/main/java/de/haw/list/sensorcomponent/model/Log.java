/**
 * 
 */
package de.haw.list.sensorcomponent.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Klasse fuer MQTT Log.
 * @author Lydia Pflug
 * 06.11.2017
 */
@Entity
public class Log {

	@Id
	@GeneratedValue
	public Integer id;
	
	public String entry;
	
	public LocalDateTime timestamp;
	
	public Log(String entry) {
		this.entry = entry;
		this.timestamp = LocalDateTime.now();
	}

	public Log() {	}

}
