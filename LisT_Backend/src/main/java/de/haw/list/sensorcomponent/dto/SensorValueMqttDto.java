/**
 * 
 */
package de.haw.list.sensorcomponent.dto;

import java.util.List;

/** 
 * DTO fuer SensorValue ueber MQTT. 
 * @author Lydia Pflug 
 * 20.11.2017 
 */ 
public final class SensorValueMqttDto { 
   
  public String techId; 
   
  public String sensorType; 
   
  public List<Double> values; 
   
  public String timestamp; 
 
}
