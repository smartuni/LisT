package de.haw.mqttlistener;

import de.haw.mqttlistener.consumercomponent.MqttConsumer;
import de.haw.mqttlistener.sensorcomponent.SensorPersistenceService;
import de.haw.mqttlistener.sensorcomponent.SensorPersistenceServiceImpl;

/**
 * Startet Anwendung.
 * @author Lydia Pflug
 * 27.11.2017
 */
public class MqttListenerApp {
	
	private static MqttConsumer mqttConsumer;
	private static SensorPersistenceService sensorPersistenceService;
	
	public static void main(String[] args) {
		sensorPersistenceService = new SensorPersistenceServiceImpl();
		
		mqttConsumer = new MqttConsumer(sensorPersistenceService);
		mqttConsumer.consume();
	}

}
