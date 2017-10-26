package de.haw.list.sensorcomponent.repo;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.haw.list.ListApplication;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.LocationType;
import de.haw.list.sensorcomponent.util.SensorType;

/**
 * Testet {@link SensorValueRepository}.
 * 
 * @author Lydia Pflug
 * 26.10.2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = ListApplication.class)
@ActiveProfiles("test")
public class SensorValueRepositoryTest {

	private Sensor sensor1;
	
	private Sensor sensor2;
	
	private SensorValue sv1;
	
	private SensorValue sv2;
	
	@Autowired
	private SensorValueRepository sensorValueRepo;
	
	@Autowired
	private SensorRepository sensorRepo;
	
	@Before
	public void setUp() {
		
		sensorValueRepo.deleteAll();
		sensorRepo.deleteAll();
		
		sensor1 = new Sensor("s1", SensorType.TEMPERATURE, "TemperatureSensor", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor1);
		
		sensor2 = new Sensor("s2", SensorType.TEMPERATURE, "TemperatureSensor2", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor2);
		
		sv1 = new SensorValue(sensor1, 10, LocalDateTime.now());
		
		sv2 = new SensorValue(sensor1, 12, LocalDateTime.now().minusDays(1));
		
		sensorValueRepo.save(sv1);
		sensorValueRepo.save(sv2);
		
	}
	
	@Test
	public void testFindLatestValueBySensor() {
		
	}
	
}
