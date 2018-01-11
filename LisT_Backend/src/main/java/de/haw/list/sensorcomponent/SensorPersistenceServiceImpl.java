/**
 * 
 */
package de.haw.list.sensorcomponent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import de.haw.list.sensorcomponent.model.Log;
import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.LogRepository;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.JsonMalFormedException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implementiert {@link SensorPersistenceService}.
 * 
 * @author Lydia Pflug 24.10.2017
 */
@Component
public final class SensorPersistenceServiceImpl implements SensorPersistenceService {

	private SensorRepository sensorRepo;

	private SensorValueRepository sensorValueRepo;

	private LogRepository logRepo;

	public SensorPersistenceServiceImpl(SensorRepository sensorRepo, SensorValueRepository sensorValueRepo,
			LogRepository logRepo) {
		this.sensorRepo = sensorRepo;
		this.sensorValueRepo = sensorValueRepo;
		this.logRepo = logRepo;
	}

	@Override
	public Sensor createSensor(Sensor sensor) {
		sensorRepo.save(sensor);

		return sensor;
	}

	@Override
	public SensorValue addSensorValue(JSONObject jsonObject) throws SensorNotFoundException, JsonMalFormedException {

		logRepo.save(new Log("innerhalb des Services"));

		Optional<Sensor> sensorOptional = null;
		List<Double> values = new ArrayList<>();
		String timestamp = "";
		LocalDateTime dateTime = null;
		double value1 = 0.0;
		double value2 = 0.0;
		double value3 = 0.0;

		try {

			String techId = (String) jsonObject.get("sensor");

			if (techId == null) {
				logRepo.save(new Log("techId nicht heraus geparst"));
				throw new JsonMalFormedException(jsonObject.toString());
			}

			logRepo.save(new Log("techId: " + techId));

			// Erstmal nicht relevant, da mit techId bereits Sensor bestimmt werden kann
			// String type = (String) jsonObject.get("type");
			//
			// if (type == null) {
			// throw new JsonMalFormedException(jsonObject.toString());
			// }

			sensorOptional = sensorRepo.findByTechId(techId);

			if (!sensorOptional.isPresent()) {
				logRepo.save(new Log("sensor nicht gefunden"));
				throw new SensorNotFoundException(String.valueOf(techId));
			}

			logRepo.save(new Log("Sensor: " + sensorOptional.get().getName()));

			JSONArray jsonArray = jsonObject.getJSONArray("value");

			if (jsonArray == null) {
				logRepo.save(new Log("Wertarray nicht gefunden"));
				throw new JsonMalFormedException(jsonObject.toString());
			}

			System.out.println(jsonArray.toString());

			// TODO in eine Stringliste speichern
			for (int i = 0; i < jsonArray.length(); i++) {
				System.out.println(jsonArray.getDouble(i));
				values.add(jsonArray.getDouble(i));
				// value1 = jsonArray.getDouble(i);
				// values.add(jsonArray.getDouble(i)/*.getJSONObject(i).getDouble("values")*/);
			}

			// value1 = jsonArray.getDouble(0);
			// value2 = jsonArray.getDouble(1);
			// value3 = jsonArray.getDouble(2);

			logRepo.save(new Log("Werte: " + Arrays.asList(values).toString()));
			// logRepo.save(new Log("Wert: " + (value1)));
			// logRepo.save(new Log("Wert: " + (value2)));
			// logRepo.save(new Log("Wert: " + (value3)));

			timestamp = (String) jsonObject.get("timestamp");

			if (timestamp == null) {
				logRepo.save(new Log("Timestamp nicht gefunden"));
				throw new JsonMalFormedException(jsonObject.toString());
			}

			logRepo.save(new Log("Timestamp: " + timestamp));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			dateTime = LocalDateTime.parse(timestamp, formatter);

			logRepo.save(new Log("Timestamp als LocalDate: " + dateTime.toString()));

		} catch (JSONException e) {
			logRepo.save(new Log("irgendeine JSON Exception"));
			logRepo.save(new Log(e.toString()));
			throw new JsonMalFormedException(jsonObject.toString());
		}

		logRepo.save(new Log("alles geparst"));
		SensorValue sensorValue = null;

		if (values.size() == 1) {
			sensorValue = new SensorValue(sensorOptional.get(), value1, 0.0, 0.0, dateTime);
			logRepo.save(new Log("1 Value"));
		} else if (values.size() == 2) {
			sensorValue = new SensorValue(sensorOptional.get(), value1, value2, 0.0, dateTime);
			logRepo.save(new Log("2 Value"));
		} else if (values.size() == 3) {
			sensorValue = new SensorValue(sensorOptional.get(), value1, value2, value3, dateTime);
			logRepo.save(new Log("3 Value"));
		} else {
			logRepo.save(new Log("Fehler bei den Values"));
		}

		logRepo.save(new Log("fertig: " + sensorValue.toString()));

		logRepo.save(new Log("Value erzeugt"));
		sensorValueRepo.save(sensorValue);
		logRepo.save(new Log("Value gespeichert"));

		return sensorValue;
	}

}
