/**
 * 
 */
package de.haw.list.adapter;

/**
 * Client fuer MQTT Uebertragung.
 * @author Lydia Pflug
 * 30.10.2017
 */
public final class MqttClient {

	public MqttClient() {
		// TODO Auto-generated constructor stub
	}

	public void publish(String message) {
//		MqttClient client = new MqttClient("tcp://broker.mqttdashboard.com", generateClientId());
//
//		client.connect();
//
//		MqttMessage message = new MqttMessage("Hallo Welt".getBytes());
//		client.publish("javamagazin/mqttarticle", message);
//
//		client.disconnect();
	}
	
	public void subscribe() {
//		 MqttClient client = new MqttClient(
//				 "tcp://broker.mqttdashboard.com", generateClientId());
//				  
//				 client.setCallback(new MqttCallback() {
//				        @Override
//				        public void connectionLost(Throwable throwable) { }
//				  
//				        @Override
//				        public void messageArrived(String t, MqttMessage m) throws Exception {
//				          System.out.println(new String(mqttMessage.getPayload()));
//				        }
//				  
//				        @Override
//				        public void deliveryComplete(IMqttDeliveryToken t) { }
//				        });
//				  
//				 client.connect();
//				  
//				 client.subscribe("javamagazin/mqttarticle");
	}

}
