/**
 * 
 */
package de.haw.list.actorcomponent.dto;

import java.util.List;

import de.haw.list.sensorcomponent.util.SensorType;

/**
 * DTO fuer Actor-Daten ueber MQTT.
 * @author Lydia Pflug
 * 20.11.2017
 */
public class ActorMqttDto {

	private String techId;
	
	private SensorType type;
	
	private List<Double> value;
	
	private String timestamp;

	public ActorMqttDto(String techId, SensorType type, List<Double> value, String timestamp) {
		this.techId = techId;
		this.type = type;
		this.value = value;
		this.timestamp = timestamp;
	}

	/**
	 * Getter fuer techId
	 * @return the techId
	 */
	public String getTechId() {
		return techId;
	}

	/**
	 * Getter fuer type
	 * @return the type
	 */
	public SensorType getType() {
		return type;
	}

	/**
	 * Getter fuer value
	 * @return the value
	 */
	public List<Double> getValue() {
		return value;
	}

	/**
	 * Getter fuer timestamp
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "ActorMqttDto [techId=" 
				+ techId 
				+ ", type=" 
				+ type 
				+ ", value=" 
				+ value 
				+ ", timestamp=" 
				+ timestamp
				+ "]";
	}
	
	
	
	
	
	
	
	
	

}
