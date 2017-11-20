/**
 * 
 */
package de.haw.list.adapter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import de.haw.list.sensorcomponent.model.Log;
import de.haw.list.sensorcomponent.repo.LogRepository;

/**
 * Client fuer MQTT Uebertragung.
 * 
 * @author Lydia Pflug
 * 30.10.2017
 */
//@Component
public final class SimpleMqttCallback implements MqttCallback {

//	private final static String URL = "tcp://localhost:1883";
//
//	private final static String CLIENT_ID = "id";
	
	private LogRepository logRepo;

	@Autowired
	public SimpleMqttCallback(LogRepository logRepo) {
		this.logRepo = logRepo;
	}

//	public void publishMessage() throws MqttException {
//
//		MqttClient client = new MqttClient(URL, CLIENT_ID);
//
//		client.connect();
//
//		MqttMessage message = new MqttMessage("Hallo Welt".getBytes());
//		client.publish("test/begruessung", message);
//		
//		logRepo.save(new Log("message publish: Hallo Welt"));
//		logRepo.save(new Log("topic publish: test/begruessung"));
//
//		client.disconnect();
//
//	}

//	public void subscribeMessage() throws MqttException {
//
//		MqttClient client = new MqttClient(URL, CLIENT_ID);
//
//		client.setCallback(new MqttCallback() {
//			@Override
//			public void connectionLost(Throwable throwable) {
//				System.out.println("Connection to MQTT broker lost!");
//				logRepo.save(new Log("Connection to MQTT broker lost!"));
//			}
//
//			@Override
//			public void messageArrived(String t, MqttMessage m) throws Exception {
//				System.out.println(new String(m.getPayload()));
//				logRepo.save(new Log(new String(m.getPayload())));
//			}
//
//			@Override
//			public void deliveryComplete(IMqttDeliveryToken t) {
//			}
//		});
//
//		client.connect();
//
//		client.subscribe("test/begruessung");
//		
//		logRepo.save(new Log("subscribe: test/begruessung"));
//
//	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Verbindung abgebrochen");
		logRepo.save(new Log("Verbindung abgebrochen"));
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Hilfe!!!");
		logRepo.save(new Log(topic + "/" + message.toString()));
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	

}
