/**
 * 
 */
package de.haw.list.sensorcomponent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import de.haw.list.sensorcomponent.model.Sensor;
import de.haw.list.sensorcomponent.model.SensorValue;
import de.haw.list.sensorcomponent.repo.SensorRepository;
import de.haw.list.sensorcomponent.repo.SensorValueRepository;
import de.haw.list.sensorcomponent.util.JsonMalFormedException;
import de.haw.list.sensorcomponent.util.SensorNotFoundException;

/**
 * Implementiert {@link SensorPersistenceService}.
 * 
 * @author Lydia Pflug
 * 24.10.2017
 */
@Component
public final class SensorPersistenceServiceImpl implements SensorPersistenceService {

	private SensorRepository sensorRepo;
	
	private SensorValueRepository sensorValueRepo;
	
	public SensorPersistenceServiceImpl(SensorRepository sensorRepo, SensorValueRepository sensorValueRepo) {
		this.sensorRepo = sensorRepo;
		this.sensorValueRepo = sensorValueRepo;
	}
	
	@Override
	public Sensor createSensor(Sensor sensor) {
		sensorRepo.save(sensor);
		
		return sensor;
	}


	  @Override 
	  public SensorValue addSensorValue(JSONObject jsonObject) throws SensorNotFoundException, JsonMalFormedException { 
	 
	    Optional<Sensor> sensorOptional = null; 
	    List<Double> values = new ArrayList<>(); 
	    String timestamp = ""; 
	 
	    try { 
	 
	      String techId = (String) jsonObject.get("sensor"); 
	 
	      if (techId == null) { 
	        throw new JsonMalFormedException(jsonObject.toString()); 
	      } 
	 
	      // Erstmal nicht relevant, da mit techId bereits Sensor bestimmt werden kann 
	      // String type = (String) jsonObject.get("type"); 
	      // 
	      // if (type == null) { 
	      // throw new JsonMalFormedException(jsonObject.toString()); 
	      // } 
	 
	      sensorOptional = sensorRepo.findByTechId(techId); 
	 
	      if (!sensorOptional.isPresent()) { 
	        throw new SensorNotFoundException(String.valueOf(techId)); 
	      } 
	      
	      JSONArray jsonArray = jsonObject.getJSONArray("value"); 
	      
	      if (jsonArray == null) { 
	        throw new JsonMalFormedException(jsonObject.toString()); 
	      } 
	 
	      for (int i = 0; i < jsonArray.length(); i++) { 
	        values.add(jsonArray.getJSONObject(i).getDouble("values")); 
	      } 
	 
	      timestamp = (String) jsonObject.get("timestamp"); 
	 
	      if (timestamp == null) { 
	        throw new JsonMalFormedException(jsonObject.toString()); 
	      } 
	 
	    } catch (JSONException e) { 
	      throw new JsonMalFormedException(jsonObject.toString()); 
	    } 
	 
	    SensorValue sensorValue = new SensorValue(sensorOptional.get(), values, LocalDateTime.parse(timestamp)); 
	 
	    sensorValueRepo.save(sensorValue); 
	 
	    return sensorValue; 
	  } 
	      

}
