package de.haw.list.sensorcomponent;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.NoValueAvailableException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implements {@link SensorComponentService}.
 * 
 * @author Lydia Pflug
 * 25.10.2017
 */
public class SensorComponentServiceImpl implements SensorComponentService{

	private SensorRepository sensorRepo;
	
	private SensorValueRepository sensorValueRepo;
	
	private SensorPersistenceService sensorPersistenceService;
	
	private SensorViewService sensorViewService;
	
	@Autowired
	public SensorComponentServiceImpl(SensorRepository sensorRepo, SensorValueRepository sensorValueRepo) {
		this.sensorRepo = sensorRepo;
		this.sensorValueRepo = sensorValueRepo;
	}
	
	@Autowired
	public SensorComponentServiceImpl(SensorPersistenceService sensorPersistenceService, SensorViewService sensorViewService) {
		this.sensorPersistenceService = sensorPersistenceService;
		this.sensorViewService = sensorViewService;
	}

	@Override
	public Sensor createSensor(Sensor sensor) {
		return sensorPersistenceService.createSensor(sensor);
	}

	@Override
	public SensorValue addSensorValue(int sensorId, double value, LocalDateTime timestamp)
			throws SensorNotFoundException {
		return sensorPersistenceService.addSensorValue(sensorId, value, timestamp);
	}

	@Override
	public Sensor getSensor(int sensorId) throws SensorNotFoundException {
		return sensorViewService.getSensor(sensorId);
	}

	@Override
	public List<Sensor> getSensors() {
		return sensorViewService.getSensors();
	}

	@Override
	public SensorValue getLastValueFromSensor(int sensorId) throws SensorNotFoundException, NoValueAvailableException {
		return sensorViewService.getLastValueFromSensor(sensorId);
	}

	@Override
	public List<SensorValue> getValuesFromSensor(int sensorId) throws SensorNotFoundException {
		return sensorViewService.getValuesFromSensor(sensorId);
	}

}
