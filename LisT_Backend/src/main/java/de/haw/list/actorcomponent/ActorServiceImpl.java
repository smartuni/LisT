/**
 * 
 */
package de.haw.list.actorcomponent;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import de.haw.list.actorcomponent.dto.ActorMqttDto;
import de.haw.list.sensorcomponent.model.Log;
import de.haw.list.sensorcomponent.repo.LogRepository;
import de.haw.list.sensorcomponent.util.JsonMalFormedException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implementiert {@link ActorService}.
 * @author Lydia Pflug
 * 20.11.2017
 */
public class ActorServiceImpl implements ActorService{
	
	@Autowired
	private LogRepository logRepo;

	@Override
	public void setActor(ActorMqttDto actorDto) {
		try {
			MemoryPersistence persistence = new MemoryPersistence();
			
			MqttClient client2 = new MqttClient("tcp://localhost:1883", "2", persistence);
	
			client2.connect();
			
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("actor", actorDto.getTechId());
			jsonObject.put("typ", actorDto.getType());
			jsonObject.put("value", actorDto.getValue());
			jsonObject.put("timestamp", actorDto.getTimestamp());

			MqttMessage message = new MqttMessage(jsonObject.toString().getBytes());
			
			client2.publish("actor", message);
			
		} catch (MqttException | JSONException e) {
			e.printStackTrace();
			logRepo.save(new Log(e.getMessage()));
		}
		
	}

}
