package de.haw.list.adapter;

import java.io.PrintWriter;
import java.io.StringWriter;

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

import de.haw.list.sensorcomponent.SensorPersistenceService;
import de.haw.list.sensorcomponent.model.Log;
import de.haw.list.sensorcomponent.repo.LogRepository;
import de.haw.list.sensorcomponent.util.JsonMalFormedException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Consumer fuer MQTT-Nachrichten.
 * @author Lydia Pflug
 * 20.11.2017
 */
@Component
public class MqttConsumer {
	
	private LogRepository logRepo;
	private MqttClient client;
    private String server = "localhost";
    private String port = "1883";
    private String broker = "tcp://" + server + ":" + port;
//    private String vhost = "yourvhost";
//    private String username = "username";
//    private String password = "password";
    private String topic = "sensor/#";
    private String clientId = MqttClient.generateClientId();
    private MemoryPersistence persistence = new MemoryPersistence();
    private boolean connected = false;

    private SensorPersistenceService sensorPersistenceService; 
   
    
	@Autowired
	public MqttConsumer(LogRepository logRepo, SensorPersistenceService sensorPersistenceService) {
		this.logRepo = logRepo;
		this.sensorPersistenceService = sensorPersistenceService;
	}
	
	/**
     * This method is the overridden callback on receiving messages.
     * @ It is event-driven. You don't call it in your code.
     * @ It prints the message topic and payload on console.
     * @ There're other callback functions provided by this library.
     */
    private class onMessage implements MqttCallback {

        public void messageArrived(String topic, MqttMessage message) throws JsonMalFormedException, SensorNotFoundException {     
            System.out.println("Topic: " + topic + ", Message: " + (new String(message.getPayload())));
            System.out.println("Hilfe!!!");
    		logRepo.save(new Log(topic + "/" + message.toString()));
    		persistMessage(message.toString());
        }

        public void connectionLost(Throwable cause) {
            System.out.printf("Exception handled, reconnecting...\nDetail:\n%s\n", cause.getMessage());
            StringWriter sw = new StringWriter();
            cause.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.out.printf(exceptionAsString);
            connected = false; //reconnect on exception
            System.out.println("Verbindung abgebrochen");
    		logRepo.save(new Log("Verbindung abgebrochen"));
        }

        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }

    public void consume() {
        while (true) {
            try {
                client = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                //connOpts.setUserName(vhost + ":" + username);
                //connOpts.setPassword(password.toCharArray());
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

    private void persistMessage(String message) throws JsonMalFormedException, SensorNotFoundException { 
        
        JSONObject jsonObject = null; 
         
        try { 
          jsonObject = new JSONObject(message); 
          logRepo.save(new Log("Konvertiert: " + message));
          logRepo.save(new Log("Json: " + jsonObject.toString()));
        } catch(JSONException e) { 
          logRepo.save(new Log(e.toString())); 
          throw new JsonMalFormedException(message); 
        } 
        
        try {
        logRepo.save(new Log("ausserhalb des try-catch-Blockes"));
        sensorPersistenceService.addSensorValue(jsonObject);
        } catch(Exception e) {
        	System.out.println("wir sind hier");
        	e.printStackTrace();
        }
         
      } 

}
