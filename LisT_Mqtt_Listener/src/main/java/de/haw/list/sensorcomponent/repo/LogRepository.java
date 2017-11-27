/**
 * 
 */
package de.haw.list.sensorcomponent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.haw.list.sensorcomponent.model.Log;

/**
 * Repository fuer Log.
 * @author Lydia Pflug
 * 06.11.2017
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

}
