package com.softtek.assetworx_api.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StringTrimModule extends SimpleModule {

	private static final long serialVersionUID = -8681863228709359775L;

	public StringTrimModule() {
		addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
			private static final long serialVersionUID = -5239258984349087289L;

			@Override
			public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException,
			JsonProcessingException {
				return jsonParser.getValueAsString()
						.replaceAll("\\s{2,}", " ")
						.trim();
			}
		});
	}
}
