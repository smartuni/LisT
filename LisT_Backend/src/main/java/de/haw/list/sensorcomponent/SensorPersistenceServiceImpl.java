/**
 * 
 */
package de.haw.list.sensorcomponent;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implementiert {@link SensorPersistenceService}.
 * 
 * @author Lydia Pflug
 * 24.10.2017
 */
@Component
public final class SensorPersistenceServiceImpl implements SensorPersistenceService {

	private SensorRepository sensorRepo;
	
	private SensorValueRepository sensorValueRepo;
	
	public SensorPersistenceServiceImpl(SensorRepository sensorRepo, SensorValueRepository sensorValueRepo) {
		this.sensorRepo = sensorRepo;
		this.sensorValueRepo = sensorValueRepo;
	}
	
	@Override
	public Sensor createSensor(Sensor sensor) {
		sensorRepo.save(sensor);
		
		return sensor;
	}

	@Override
	public SensorValue addSensorValue(int sensorId, double value, LocalDateTime timestamp) throws SensorNotFoundException {

		Optional<Sensor> sensorOptional = sensorRepo.findById(sensorId);
		
		if (!sensorOptional.isPresent()) {
			throw new SensorNotFoundException(String.valueOf(sensorId));
		}
		
		SensorValue sensorValue = new SensorValue(sensorOptional.get(), value, timestamp);
		
		sensorValueRepo.save(sensorValue);
		
		return sensorValue;
	}

}
