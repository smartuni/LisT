package de.haw.list;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

//import de.haw.list.adapter.MqttConsumer;
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
	
//	@Autowired
//	private MqttConsumer mqttConsumer;
	
	@Autowired
	private LogRepository logRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(ListApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		sensorValueRepo.deleteAll();
		sensorRepo.deleteAll();
		logRepo.deleteAll();
		
		Sensor sensor1 = new Sensor("s1", SensorType.TEMP, "TemperatureSensor", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor1);
		
		Sensor sensor2 = new Sensor("s2", SensorType.TEMP, "TemperatureSensor2", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor2);
		
//		List<Double> values1 = new ArrayList<>();
//		values1.add(10.0);
//		List<Double> values2 = new ArrayList<>();
//		values1.add(12.0);
//		List<Double> values3 = new ArrayList<>();
//		values1.add(13.0);
//		List<Double> values4 = new ArrayList<>();
//		values1.add(14.0);
		
		
		SensorValue sv1 = new SensorValue(sensor1, 10.0, 0.0, 0.0, LocalDateTime.now());
		
		SensorValue sv2 = new SensorValue(sensor2, 12.0, 0.0, 0.0, LocalDateTime.now());
		SensorValue sv3 = new SensorValue(sensor2, 13.0, 0.0, 0.0, LocalDateTime.now());
		SensorValue sv4 = new SensorValue(sensor2, 14.0, 0.0, 0.0, LocalDateTime.now());
		
		sensorValueRepo.save(Arrays.asList(sv1, sv2, sv3, sv4));
		
		Log log1 = new Log("test");
		Log log2 = new Log("test2");
		
		logRepo.save(Arrays.asList(log1, log2));
		
//		MqttConsumer c = new MqttConsumer(logRepo);
//        c.consume();
		//mqttConsumer.consume();
		
//		MemoryPersistence persistence = new MemoryPersistence();
		
		
//		logRepo.save(new Log("Connection erstellt"));
		
		
//		MqttClient client2 = new MqttClient("tcp://localhost:1883", "2", persistence);


		//		simpleMqttClient = new SimpleMqttClient();
		
//		simpleMqttClient.publishMessage();
//		simpleMqttClient.subscribeMessage();
	}
	
	@Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(ListApplication.class);
    }
}
