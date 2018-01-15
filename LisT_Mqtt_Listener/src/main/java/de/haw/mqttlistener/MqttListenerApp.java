package de.haw.mqttlistener;

import de.haw.mqttlistener.consumercomponent.MqttConsumer;

/**
 * Startet Anwendung.
 * @author Lydia Pflug
 * 27.11.2017
 */
public class MqttListenerApp {
	
	private static MqttConsumer mqttConsumer;
	
	public static void main(String[] args) {
		
		mqttConsumer = new MqttConsumer();
		mqttConsumer.consume();
	}

}
