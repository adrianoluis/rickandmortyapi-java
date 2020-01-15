package com.rickandmortyapi.util;

import com.google.common.base.Strings;

import java.util.function.Function;

public class Urls {

	public static <T> T asId(String url, Function<String, T> function) {
		if (Strings.isNullOrEmpty(url))
			return null;
		final String idStr = url.substring(url.lastIndexOf('/') + 1);
		return function.apply(idStr);
	}
}
