/**
 * 
 */
package de.haw.list.sensorcomponent.util;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializer fuer LocalDateTime.
 * 
 * @author Lydia Pflug
 * 04.10.2017
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>{

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value.toString());
		
	}



}
