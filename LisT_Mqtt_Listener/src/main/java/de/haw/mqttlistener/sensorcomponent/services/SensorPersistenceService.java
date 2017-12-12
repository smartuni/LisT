/**
 * 
 */
package de.haw.mqttlistener.sensorcomponent.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.json.JSONObject;

import de.haw.mqttlistener.util.JsonMalFormedException;


/**
 * Persistiert Sensoren und Sensordaten.
 * @author Lydia Pflug
 * 18.10.2017
 */
public interface SensorPersistenceService {
	
	
	/**
	 * Fuegt neuen Sensorwert hinzu.
	 * 
	 * @param sensorId ID des Sensors
	 * @param value Wert des Sensors
	 * @return Sensorwert
	 * @throws SensorNotFoundException 
	 * @throws JsonMalFormedException 
	 * @throws de.haw.mqttlistener.util.JsonMalFormedException 
	 * @throws ProtocolException 
	 * @throws UnsupportedEncodingException 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 */
	public void addSensorValue(JSONObject jsonObject) throws JsonMalFormedException, ProtocolException, UnsupportedEncodingException, MalformedURLException, IOException;

}		
