package com.rickandmortyapi.util;

import com.google.gson.*;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.Type;
import java.util.Collection;

public class Jsons {

	private static final Gson GSON_DATA_PROVIDER;

	static {
		GSON_DATA_PROVIDER = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				.registerTypeAdapterFactory(new PostConstructAdapterFactory())
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.setLongSerializationPolicy(LongSerializationPolicy.STRING)
				.create();
	}

	public static Gson provider() {
		return GSON_DATA_PROVIDER;
	}

	public static <T> T asObject(JsonElement json, Class<T> clazz) {
		return GSON_DATA_PROVIDER.fromJson(json, clazz);
	}

	public static <T> Collection<T> asCollection(JsonElement json, Type typeOf) {
		return GSON_DATA_PROVIDER.fromJson(json, typeOf);
	}
}
