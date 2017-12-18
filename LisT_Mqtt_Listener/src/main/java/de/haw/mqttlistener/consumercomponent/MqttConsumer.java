package de.haw.mqttlistener.consumercomponent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import de.haw.mqttlistener.sensorcomponent.model.Log;
import de.haw.mqttlistener.sensorcomponent.repo.LogRepository;
import de.haw.mqttlistener.sensorcomponent.services.SensorPersistenceService;
import de.haw.mqttlistener.util.JsonMalFormedException;



/**
 * Consumer fuer MQTT-Nachrichten mit Topic 'sensor'.
 * @author Lydia Pflug
 * 20.11.2017
 */
//@Component
public class MqttConsumer {
	
	private SensorPersistenceService sensorPersistenceService;
	private LogRepository logRepo;
	private MqttClient client;
    private String server = "localhost";
    private String port = "1883";
    private String broker = "tcp://" + server + ":" + port;
    private String topic = "sensor/#";
    private String clientId = MqttClient.generateClientId();
    private MemoryPersistence persistence = new MemoryPersistence();
    private boolean connected = false;
    private RestTemplate restClient;
    
    
   // @Autowired
//	public MqttConsumer(LogRepository logRepo, SensorPersistenceService sensorPersistenceService) {
//		this.logRepo = logRepo;
//		this.sensorPersistenceService = sensorPersistenceService;
//	}
    
    public MqttConsumer() {
    	restClient = new RestTemplate();
    	
    	
    }
	
	/**
     * This method is the overridden callback on receiving messages.
     * @ It is event-driven. You don't call it in your code.
     * @ It prints the message topic and payload on console.
     * @ There're other callback functions provided by this library.
     */
    private class onMessage implements MqttCallback {

    	@Override
        public void messageArrived(String topic, MqttMessage message) throws JsonMalFormedException, ProtocolException, UnsupportedEncodingException, MalformedURLException, IOException {     
    		
    		System.out.println("Topic: " + topic + ", Message: " + (new String(message.getPayload())));
            sendSensorValue(message.toString());
            //logRepo.save(new Log(topic + "/" + message.toString()));
    		//persistMessage(message.toString());
        }

    	@Override
        public void connectionLost(Throwable cause) {
            System.out.printf("Exception handled, reconnecting...\nDetail:\n%s\n", cause.getMessage());
            connected = false; //reconnect on exception
            System.out.println("Verbindung abgebrochen");
            logRepo.save(new Log("Verbindung abgebrochen"));
        }

    	@Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }

	public void consume() {
        while (true) {
            try {
                client = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setKeepAliveInterval(60);
                connOpts.setCleanSession(true);
                client.connect(connOpts);
                onMessage callback = new onMessage();
                client.setCallback(callback);
                client.subscribe(topic, 1); //qos=1
                connected = true;
                while (connected) { //check connection status
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {}
                } 
            } catch(MqttException me) {
                //reconnect on exception
                System.out.printf("Exception handled, reconnecting...\nDetail:\n%s\n", me); 
                try {
                    Thread.sleep(5000); 
                } catch(Exception e) {}
            }
        }
    }
    
    private void persistMessage(String message) throws JsonMalFormedException, ProtocolException, UnsupportedEncodingException, MalformedURLException, IOException {
    	
    	JSONObject jsonObject = null;
    	
    	try {
    		jsonObject = new JSONObject(message);
    		logRepo.save(new Log("Konvertiert: " + message));
            logRepo.save(new Log("Json: " + jsonObject.toString()));
    	} catch(JSONException e) {
    		e.printStackTrace();
    		logRepo.save(new Log(e.toString())); 
    		throw new JsonMalFormedException(message);
    	}
    	
    	sensorPersistenceService.addSensorValue(jsonObject);
    	
    }
    
    private void sendSensorValue(String message) throws JsonMalFormedException {

    	JSONObject jsonObject = null;
    	
    	try {
    		jsonObject = new JSONObject(message);
    		System.out.println("Konvertiert: " + message);
    		System.out.println("Json: " + jsonObject.toString());
    		//logRepo.save(new Log("Konvertiert: " + message));
            //logRepo.save(new Log("Json: " + jsonObject.toString()));
    	} catch(JSONException e) {
    		e.printStackTrace();
    		//logRepo.save(new Log(e.toString())); 
    		throw new JsonMalFormedException(message);
    	}
    	
    	HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObject);
    	
    	System.out.println("ausserhalb des try catch Blockes");
    	ResponseEntity<?> response = restClient.exchange("http://127.0.0.1:80/api/sensors/value", HttpMethod.POST, httpEntity, ResponseEntity.class);
    	System.out.println("Response " + response.toString());
    	System.out.println("an Rest-Facade geschickt");
    }
	

}
