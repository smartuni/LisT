/**
 * 
 */
package de.haw.list.sensorcomponent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.haw.list.sensorcomponent.model.SensorValue;

/**
 * Repository fuer Sensorwert.
 * 
 * @author Lydia Pflug
 * 18.10.2017
 */
@Repository
public interface SensorValueRepository extends JpaRepository<SensorValue, Integer> {

	
}
