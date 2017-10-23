/**
 * 
 */
package de.haw.list.sensorcomponent.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.haw.list.sensorcomponent.model.Sensor;

/**
 * Repository fuer Sensor.
 * 
 * @author Lydia Pflug
 * 18.10.2017
 */
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

}
