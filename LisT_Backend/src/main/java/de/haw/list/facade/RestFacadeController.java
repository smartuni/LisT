package de.haw.list.facade;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.haw.list.actorcomponent.ActorService;
import de.haw.list.actorcomponent.dto.ActorMqttDto;
import de.haw.list.sensorcomponent.SensorPersistenceService;
import de.haw.list.sensorcomponent.SensorViewService;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.util.NoValueAvailableException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Rest-Fassade fuer Controller.
 * 
 * @author Lydia Pflug 09.10.2017
 */
@RestController
public class RestFacadeController {

	@Autowired
	private SensorPersistenceService sensorPersistenceService;

	@Autowired
	private SensorViewService sensorViewService;

	@Autowired
	private ActorService actorService;

	@RequestMapping("/home")
	public String handler() {
		return "home";
	}

	@RequestMapping("/test")
	public void handler2() {
		throw new RuntimeException("exception from handler2");
	}

	/**
	 * Erstellt neuen Sensor.
	 * 
	 * @param sensor
	 *            Sensor, der erstellt werden soll
	 * @return Sensor, der erstellt worden ist
	 */
	@RequestMapping(value = "/api/sensors", method = RequestMethod.POST)
	public ResponseEntity<?> createSensor(@RequestBody Sensor sensor) {
		try {
			return new ResponseEntity<Sensor>(sensorPersistenceService.createSensor(sensor), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// /**
	// * Fuegt neuen Sensorwert hinzu.
	// *
	// * @param sensorId ID des Sensors
	// * @param value Wert des Sensors
	// * @return Sensorwert
	// * @throws SensorNotFoundException
	// */
	// @RequestMapping(value = "/api/sensors/{id}/values", method =
	// RequestMethod.POST)
	// public ResponseEntity<?> addSensorValue(@PathVariable("id") int sensorId,
	// @RequestBody double value, @RequestBody LocalDateTime timestamp) throws
	// SensorNotFoundException {
	// try {
	// return new
	// ResponseEntity<SensorValue>(sensorPersistenceService.addSensorValue(sensorId,
	// value, timestamp), HttpStatus.CREATED);
	// } catch (SensorNotFoundException e) {
	// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// } catch (Exception e) {
	// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	// }
	// }

	/**
	 * Fuegt neuen Sensorwert hinzu.
	 * 
	 * @param sensorValue
	 *            als JSON
	 * @return Sensorwert
	 * @throws SensorNotFoundException
	 */
	@RequestMapping(value = "/api/sensors/values", method = RequestMethod.POST)
	public ResponseEntity<?> addSensorValue(@RequestBody String value) throws SensorNotFoundException {
		try {
			System.out.println("Restfacade erreicht");
			return new ResponseEntity<SensorValue>(sensorPersistenceService.addSensorValue(new JSONObject(value)),
					HttpStatus.CREATED);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gibt Sensor zurueck.
	 * 
	 * @param sensorId
	 *            ID des Sensors
	 * @return Sensor
	 * @throws SensorNotFoundException
	 */
	@RequestMapping(value = "/api/sensors/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSensor(@PathVariable("id") Integer sensorId) throws SensorNotFoundException {
		try {
			return new ResponseEntity<Sensor>(sensorViewService.getSensor(sensorId), HttpStatus.OK);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
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
		return new ResponseEntity<List<Sensor>>(sensorViewService.getSensors(), HttpStatus.OK);
	}

	/**
	 * Gibt letzten Wert des Sensors zurueck.
	 * 
	 * @param sensorId
	 *            ID des Sensors
	 * @return Werte des Sensors
	 * @throws NoValueAvailableException
	 * @throws SensorNotFoundException
	 */
	@RequestMapping(value = "/api/sensors/{id}/values/latest", method = RequestMethod.GET)
	public ResponseEntity<?> getLatestValueFromSensor(@PathVariable("id") Integer sensorId)
			throws SensorNotFoundException, NoValueAvailableException {
		try {
			return new ResponseEntity<List<Double>>(sensorViewService.getLatestValueFromSensor(sensorId),
					HttpStatus.OK);
			// return new ResponseEntity<SensorValue>(new SensorValue(new Sensor(), 10.0,
			// LocalDateTime.now()), HttpStatus.OK);
			// return new ResponseEntity<Double>(10.0, HttpStatus.OK);
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
	 * 
	 * @param sensorId
	 *            ID des Sensors
	 * @return Liste mit Werten
	 * @throws SensorNotFoundException
	 */
	@RequestMapping(value = "/api/sensors/{id}/values", method = RequestMethod.GET)
	public ResponseEntity<?> getValuesFromSensor(@PathVariable("id") Integer sensorId) throws SensorNotFoundException {
		try {
			return new ResponseEntity<List<SensorValue>>(sensorViewService.getValuesFromSensor(sensorId),
					HttpStatus.OK);
		} catch (SensorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Setzt neuen Aktor.
	 * 
	 * @param actorDto
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/api/actor", method = RequestMethod.PUT)
	public ResponseEntity<?> setActor(@RequestBody ActorMqttDto actorDto) {
		try {
			actorService.setActor(actorDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}