/**
 * 
 */
package de.haw.mqttlistener.sensorcomponent.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import de.haw.mqttlistener.util.JsonMalFormedException;

/**
 * Implementiert {@link SensorPersistenceService}.
 * 
 * @author Lydia Pflug 24.10.2017
 */
public final class SensorPersistenceServiceImpl implements SensorPersistenceService {

	@Override
	public void addSensorValue(JSONObject jsonObject) throws JsonMalFormedException, IOException {

		byte[] out = jsonObject.toString().getBytes();
		int length = out.length;
		
		URL u = new URL("http://localhost:8080/api/sensors");
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");	
		
		conn.setFixedLengthStreamingMode(length);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.connect();
		try(OutputStream os = conn.getOutputStream()) {
			os.write(out);
		}

	}

}
