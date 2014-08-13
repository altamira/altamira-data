package br.com.altamira.data.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NullCollectionSerializer extends JsonSerializer<Object> {

	public NullCollectionSerializer() {
		
	}
	
	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if (value == null) {
			jsonGenerator.writeString("[]");
		} else {
			jsonGenerator.writeObject(value);
		}
		
	}
}
