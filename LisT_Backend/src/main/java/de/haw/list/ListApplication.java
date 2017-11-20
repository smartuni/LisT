package de.haw.list;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import de.haw.list.adapter.MqttConsumer;
import de.haw.list.adapter.SimpleMqttCallback;
import de.haw.list.sensorcomponent.model.Log;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.LogRepository;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.LocationType;
import de.haw.list.sensorcomponent.util.SensorType;

@SpringBootApplication
@ComponentScan(basePackageClasses = ListApplication.class)
public class ListApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private SensorRepository sensorRepo;
	
	@Autowired
	private SensorValueRepository sensorValueRepo;
	
	@Autowired
	private LogRepository logRepo;
	
//	@Autowired
//	private SimpleMqttClient simpleMqttClient;
	
	public static void main(String[] args) {
		SpringApplication.run(ListApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
//		sensorValueRepo.deleteAll();
//		sensorRepo.deleteAll();
		
		Sensor sensor1 = new Sensor("s1", SensorType.TEMPERATURE, "TemperatureSensor", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor1);
		
		Sensor sensor2 = new Sensor("s2", SensorType.TEMPERATURE, "TemperatureSensor2", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor2);
		
		SensorValue sv1 = new SensorValue(sensor1, 10, LocalDateTime.now());
		
		SensorValue sv2 = new SensorValue(sensor2, 12, LocalDateTime.now());
		SensorValue sv3 = new SensorValue(sensor2, 13, LocalDateTime.now());
		SensorValue sv4 = new SensorValue(sensor2, 14, LocalDateTime.now());
		
		sensorValueRepo.save(Arrays.asList(sv1, sv2, sv3, sv4));
		
		Log log1 = new Log("test");
		Log log2 = new Log("test2");
		
		logRepo.save(Arrays.asList(log1, log2));
		
		MqttConsumer c = new MqttConsumer(logRepo);
        c.consume();
		
		MemoryPersistence persistence = new MemoryPersistence();
//		MqttClient client=new MqttClient("tcp://localhost:1883", MqttClient.generateClientId(), persistence);
//		client.setCallback( new SimpleMqttCallback(logRepo));
//		client.connect();
		
//		MqttClient client = new MqttClient("tcp://localhost:1883", "1", persistence);
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
//				logRepo.save(new Log(t));
//				logRepo.save(new Log(new String(m.getPayload())));
//			}
//
//			@Override
//			public void deliveryComplete(IMqttDeliveryToken t) {
//			}
//		});
//
//		client.connect();
		
		
		logRepo.save(new Log("Connection erstellt"));
		
		
		MqttClient client2 = new MqttClient("tcp://localhost:1883", "2", persistence);

		client2.connect();

		MqttMessage message = new MqttMessage("Hallo Welt".getBytes());
		client2.publish("test", message);
		
		logRepo.save(new Log("message publish: Hallo Welt"));
		logRepo.save(new Log("topic publish: test/begruessung"));

		client2.disconnect();
		
		logRepo.save(new Log("Nachricht abgeschickt"));
		
		System.out.println("Fertig!");
		


		//		simpleMqttClient = new SimpleMqttClient();
		
//		simpleMqttClient.publishMessage();
//		simpleMqttClient.subscribeMessage();
	}
	
	@Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(ListApplication.class);
    }
}
