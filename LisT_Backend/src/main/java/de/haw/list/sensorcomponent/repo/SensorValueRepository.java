package de.haw.list.sensorcomponent.repo;

import java.time.LocalDateTime;
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
 * @author Lydia Pflug
 * 18.10.2017
 */
@Repository
public interface SensorValueRepository extends JpaRepository<SensorValue, Integer> {

	public List<SensorValue> findAllBySensor(Sensor sensor);
	
	public Optional<SensorValue> findById(int sensorId);

	@Query(value = "select * from sensorvalue where sensorvalue.sensor_id = :id and sensorvalue.timestamp = (Select max(sensorvalue.timestamp) from sensorvalue)", nativeQuery = true)
	Optional<SensorValue> findLatestValueBySensor(@Param("id") int sensorId);
	
	@Query(value = "select * from sensorvalue", nativeQuery = true)
	List<SensorValue> findValueBySensor();
	
	@Query(value = "select * from sensorvalue where sensorvalue.sensor_id = :id", nativeQuery = true)
	List<SensorValue> findValueBySensor(@Param("id") int sensorId);
	
	@Query(value = "select * from sensorvalue where sensorvalue.timestamp = :ts", nativeQuery = true)
	List<SensorValue> findValueByTimestamp(@Param("ts") LocalDateTime ts);

}
