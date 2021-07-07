package com.rickandmortyapi.util;

import com.google.common.base.Strings;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter implements JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final String dateTime = json.getAsString();
		return Strings.isNullOrEmpty(dateTime) ? null : LocalDate.parse(dateTime,
				DateTimeFormatter.ofPattern("MMMM d, yyyy"));
	}
}
