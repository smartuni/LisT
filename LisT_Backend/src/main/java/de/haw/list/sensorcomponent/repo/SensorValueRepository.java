package de.haw.list.sensorcomponent.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;

/**
 * Repository fuer Sensorwert.
 * 
 * @author Lydia Pflug 18.10.2017
 */
@Repository
public interface SensorValueRepository extends JpaRepository<SensorValue, Integer> {

	public List<SensorValue> findAllBySensor(Sensor sensor);

	// TODO lyp 25.10.2017 Testen!!!
	@Query(value = "SELECT * FROM SENSORVALUE WHERE SENSOR_ID =:sensorId AND (SELECT MAX(TIMESTAMP) FROM SENSORVALUE)", nativeQuery = true)
	Optional<SensorValue> findLatestValueBySensor(@Param("sensorId") int sensorId);

}
