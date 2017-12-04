/**
 * 
 */
package de.haw.mqttlistener.dto;

import java.util.List;

/**
 * DTO fuer SensorValue ueber MQTT.
 * @author Lydia Pflug
 * 20.11.2017
 */
public class SensorValueMqttDto {
	
	
	
	public String techId;
	
	public String sensorType;
	
	public List<Double> values;
	
	public String timestamp;

	
	
	

}
