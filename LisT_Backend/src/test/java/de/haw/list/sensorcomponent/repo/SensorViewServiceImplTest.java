/**
 * 
 */
package de.haw.list.sensorcomponent.repo;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.haw.list.ListApplication;
import de.haw.list.sensorcomponent.SensorViewServiceImpl;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.LocationType;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;
import de.haw.list.sensorcomponent.util.SensorType;

/**
 * Testet {@link SensorViewServiceImpl}.
 * @author Lydia Pflug
 * 13.11.2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = ListApplication.class)
@ActiveProfiles("test")
public class SensorViewServiceImplTest {

	private Sensor sensor1;
	
	private Sensor sensor2;
	
	private SensorValue sv1;
	
	private SensorValue sv2;
	private SensorValue sv3;
	
	private SensorValue sv4;
	private SensorValue sv5;
	
	private SensorValue sv6;
	
	private LocalDateTime ld;
	
	@Autowired
	private SensorValueRepository sensorValueRepo;
	
	@Autowired
	private SensorRepository sensorRepo;
	
	@Autowired 
	public SensorViewServiceImpl sensorViewServiceImpl;
	
	@Before
	public void setUp() {
		
		sensorValueRepo.deleteAll();
		sensorRepo.deleteAll();
		
		sensor1 = new Sensor("s1", SensorType.TEMPERATURE, "TemperatureSensor", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor1);
		
		sensor2 = new Sensor("s2", SensorType.TEMPERATURE, "TemperatureSensor2", LocationType.INSIDE, 20, 15);
		
		sensorRepo.save(sensor2);
		
		ld = LocalDateTime.now();
		
		sv1 = new SensorValue(sensor1, 10, ld);
		sv2 = new SensorValue(sensor1, 12, ld.minusDays(1));
		sv3 = new SensorValue(sensor2, 10, ld);
		sv4 = new SensorValue(sensor2, 12, ld.minusDays(1));
		sv5 = new SensorValue(sensor2, 10, ld.minusDays(2));
		sv6 = new SensorValue(sensor2, 12, ld.minusDays(3));
		
		sensorValueRepo.save(Arrays.asList(sv1, sv2, sv3, sv4, sv5, sv6));
		
	}
	
	@Test
	public void testGetValuesFromSensor() throws SensorNotFoundException {
		
		List<SensorValue> result = sensorViewServiceImpl.getValuesFromSensor(sensor2.getId());
		assertEquals(4, result.size());
	}
	

}
