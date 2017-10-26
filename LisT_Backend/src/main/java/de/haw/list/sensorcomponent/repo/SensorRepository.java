/**
 * 
 */
package de.haw.list.sensorcomponent.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.haw.list.sensorcomponent.model.Sensor;

/**
 * Repository fuer Sensor.
 * 
 * @author Lydia Pflug
 * 18.10.2017
 */
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

	public Optional<Sensor> findById(int sensorId);

}
