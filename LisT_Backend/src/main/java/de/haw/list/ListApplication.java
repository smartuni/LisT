package de.haw.list;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import de.haw.list.sensorcomponent.model.Sensor;
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
	
	public static void main(String[] args) {
		SpringApplication.run(ListApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		sensorValueRepo.deleteAll();
		sensorRepo.deleteAll();
		
		Sensor sensor1 = new Sensor("s1", SensorType.TEMPERATURE, "TemperatureSensor", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor1);
		
		Sensor sensor2 = new Sensor("s2", SensorType.TEMPERATURE, "TemperatureSensor2", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor2);
	}
	
	@Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(ListApplication.class);
    }
}
