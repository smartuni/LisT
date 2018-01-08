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
		
		Sensor sensor1 = new Sensor("s1", SensorType.TEMP, "TemperatureSensor", LocationType.INSIDE, 35, 15);
		Sensor sensor2 = new Sensor("s2", SensorType.TEMP, "TemperatureSensor2", LocationType.INSIDE, 35, 15);
		Sensor sensor3 = new Sensor("s3", SensorType.LIGHT, "LightSensor", LocationType.INSIDE, 0, 0);
		Sensor sensor4 = new Sensor("s4", SensorType.PH, "pHSensor", LocationType.INSIDE, 9, 6);
		
		sensorRepo.save(Arrays.asList(sensor1, sensor2, sensor3, sensor4));
		
		
		
		SensorValue sv1 = new SensorValue(sensor1, 10.0, 0.0, 0.0, LocalDateTime.now());
		
		SensorValue sv2 = new SensorValue(sensor2, 12.0, 0.0, 0.0, LocalDateTime.now());
		SensorValue sv3 = new SensorValue(sensor2, 13.0, 0.0, 0.0, LocalDateTime.now());
		SensorValue sv4 = new SensorValue(sensor2, 14.0, 0.0, 0.0, LocalDateTime.now());
		
		sensorValueRepo.save(Arrays.asList(sv1, sv2, sv3, sv4));
		
		Log log1 = new Log("test");
		Log log2 = new Log("test2");
		
		logRepo.save(Arrays.asList(log1, log2));
		
	}
	
	@Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(ListApplication.class);
    }
}
