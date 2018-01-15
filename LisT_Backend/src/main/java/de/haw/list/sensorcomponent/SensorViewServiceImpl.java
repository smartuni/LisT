package de.haw.list.sensorcomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.NoValueAvailableException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implementiert {@link SensorViewService}.
 * 
 * @author Lydia Pflug 25.10.2017
 */
@Component
public class SensorViewServiceImpl implements SensorViewService {

	private SensorRepository sensorRepo;

	private SensorValueRepository sensorValueRepo;

	@Autowired
	public SensorViewServiceImpl(SensorRepository sensorRepo, SensorValueRepository sensorValueRepo) {
		this.sensorRepo = sensorRepo;
		this.sensorValueRepo = sensorValueRepo;
	}

	@Override
	public Sensor getSensor(int sensorId) throws SensorNotFoundException {
		Optional<Sensor> sensorOptional = sensorRepo.findById(sensorId);

		if (!sensorOptional.isPresent()) {
			throw new SensorNotFoundException(String.valueOf(sensorId));
		}

		return sensorOptional.get();
	}

	@Override
	public List<Sensor> getSensors() {
		return sensorRepo.findAll();
	}

	@Override
	public List<Double> getLatestValueFromSensor(int sensorId) throws SensorNotFoundException, NoValueAvailableException {
		Optional<Sensor> sensorOptional = sensorRepo.findById(sensorId);

		if (!sensorOptional.isPresent()) {
			throw new SensorNotFoundException(String.valueOf(sensorId));
		}

		Optional<SensorValue> sensorValueOptional = sensorValueRepo.findLatestValueBySensor(sensorId);
		
		if (!sensorValueOptional.isPresent()) {
			throw new NoValueAvailableException(String.valueOf(sensorId));
		}
		List<Double> result = new ArrayList<>();
		result.add(sensorValueOptional.get().getValue1());
		result.add(sensorValueOptional.get().getValue2());
		result.add(sensorValueOptional.get().getValue3());
//		return sensorValueOptional.get().getValue1();
		return result;
		
	}

	@Override
	public List<SensorValue> getValuesFromSensor(int sensorId) throws SensorNotFoundException {
		Optional<Sensor> sensorOptional = sensorRepo.findById(sensorId);

		if (!sensorOptional.isPresent()) {
			throw new SensorNotFoundException(String.valueOf(sensorId));
		}
		
		return sensorValueRepo.findAllBySensor(sensorOptional.get());
	}

}
