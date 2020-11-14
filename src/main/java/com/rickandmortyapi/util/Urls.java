package com.rickandmortyapi.util;

import com.google.common.base.Strings;

public class Urls {

	public static int asId(String url) {
		if (Strings.isNullOrEmpty(url))
			return -1;
		return Integer.parseInt(url.substring(url.lastIndexOf('/') + 1));
	}
}
