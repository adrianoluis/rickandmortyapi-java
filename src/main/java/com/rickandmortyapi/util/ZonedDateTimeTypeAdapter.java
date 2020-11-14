package com.rickandmortyapi.util;

import com.google.gson.*;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

class ZonedDateTimeTypeAdapter implements JsonDeserializer<ZonedDateTime> {

	@Override
	public ZonedDateTime deserialize(final JsonElement json, final Type typeOfT,
									 final JsonDeserializationContext context) throws JsonParseException {
		JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
		try {
			// if provided as String - '2011-12-03T10:15:30+01:00[Europe/Paris]'
			if (jsonPrimitive.isString()) {
				return ZonedDateTime.parse(jsonPrimitive.getAsString(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"));
			}
			// if provided as Long
			if (jsonPrimitive.isNumber()) {
				return ZonedDateTime.ofInstant(Instant.ofEpochMilli(jsonPrimitive.getAsLong()),
						ZoneId.systemDefault());
			}
		} catch (RuntimeException e) {
			throw new JsonParseException("Unable to parse ZonedDateTime", e);
		}
		throw new JsonParseException("Unable to parse ZonedDateTime");
	}
}
