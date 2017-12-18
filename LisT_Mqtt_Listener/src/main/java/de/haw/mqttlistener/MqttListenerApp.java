package de.haw.mqttlistener;

import org.springframework.beans.factory.annotation.Autowired;

import de.haw.mqttlistener.consumercomponent.MqttConsumer;
import de.haw.mqttlistener.sensorcomponent.repo.LogRepository;
import de.haw.mqttlistener.sensorcomponent.services.SensorPersistenceService;

/**
 * Startet Anwendung.
 * @author Lydia Pflug
 * 27.11.2017
 */
public class MqttListenerApp {
	
	private static MqttConsumer mqttConsumer;
	private static SensorPersistenceService sensorPersistenceService;
	
	@Autowired
	private static LogRepository logRepository;
	
//	@Autowired
//	private static MqttConsumer mqttConsumer;
	
	public static void main(String[] args) {
//		sensorPersistenceService = new SensorPersistenceServiceImpl();
		
		mqttConsumer = new MqttConsumer(logRepository, sensorPersistenceService);
		mqttConsumer.consume();
	}

}
