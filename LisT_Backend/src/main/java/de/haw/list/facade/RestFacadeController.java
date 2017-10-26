package de.haw.list.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.haw.list.sensorcomponent.SensorComponentService;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.NoValueAvailableException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Rest-Fassade fuer Controller.
 * 
 * @author Lydia Pflug
 * 09.10.2017
 */
@Controller
public class RestFacadeController {
	
	@Autowired
	private SensorComponentService sensorComponentService;
	
	/**
	 * Erstellt neuen Sensor.
	 * 
	 * @param sensor Sensor, der erstellt werden soll
	 * @return Sensor, der erstellt worden ist
	 */
	@RequestMapping(value = "/api/sensors", method = RequestMethod.POST)
	public ResponseEntity<?> createSensor(@RequestBody Sensor sensor) {
		try {
			return new ResponseEntity<Sensor>(sensorComponentService.createSensor(sensor), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/**
	 * Fuegt neuen Sensorwert hinzu.
	 * 
	 * @param sensorId ID des Sensors
	 * @param value Wert des Sensors
	 * @return Sensorwert
	 * @throws SensorNotFoundException 
	 */
	@RequestMapping(value = "/api/sensors/{id}/values", method = RequestMethod.POST)
	public ResponseEntity<?> addSensorValue(@PathVariable("id") int sensorId, @RequestBody double value, @RequestBody LocalDateTime timestamp) throws SensorNotFoundException {
		try {
			return new ResponseEntity<SensorValue>(sensorComponentService.addSensorValue(sensorId, value, timestamp), HttpStatus.CREATED);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gibt Sensor zurueck.
	 * 
	 * @param sensorId ID des Sensors
	 * @return Sensor
	 * @throws SensorNotFoundException 
	 */
	@RequestMapping(value = "/api/sensors/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSensor(@PathVariable("id") int sensorId) throws SensorNotFoundException {
		try {
			return new ResponseEntity<Sensor>(sensorComponentService.getSensor(sensorId), HttpStatus.OK);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch ( Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * Gibt alle Sensoren zurueck.
	 * 
	 * @return Liste mit Sensoren
	 */
	@RequestMapping(value = "/api/sensors", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> getSensors() {
		return new ResponseEntity<List<Sensor>>(sensorComponentService.getSensors(), HttpStatus.OK);
	}
	
	/**
	 * Gibt letzten Wert des Sensors zurueck.
	 * @param sensorId ID des Sensors
	 * @return Wert des Sensors
	 * @throws NoValueAvailableException 
	 * @throws SensorNotFoundException 
	 */
	@RequestMapping(value="/api/sensors/{id}/values/latest", method= RequestMethod.GET)
	public ResponseEntity<?> getLastValueFromSensor(@PathVariable("id") int sensorId) throws SensorNotFoundException, NoValueAvailableException {
		try {
			return new ResponseEntity<SensorValue>(sensorComponentService.getLastValueFromSensor(sensorId), HttpStatus.OK);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (NoValueAvailableException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Gibt alle Werte des Sensors zurueck.
	 * @param sensorId ID des Sensors
	 * @return Liste mit Werten
	 * @throws SensorNotFoundException 
	 */
	@RequestMapping(value="/api/sensors/{id}/values",method=RequestMethod.GET)
	public ResponseEntity<?> getValuesFromSensor(@PathVariable("id") int sensorId) throws SensorNotFoundException {
		try {
			return new ResponseEntity<List<SensorValue>>(sensorComponentService.getValuesFromSensor(sensorId), HttpStatus.OK);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


}