package com.rickandmortyapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;

public class ApiResponse {

	@Getter
	private int code = -1;

	@Getter
	private final JsonElement body;

	public ApiResponse(final JsonElement body) {
		this.body = body;
	}

	public ApiResponse(final int code, final JsonElement body) {
		this.code = code;
		this.body = body;
	}

	public ApiResponse(final JsonObject object) {
		this.code = object.get("code").getAsInt();
		this.body = object.get("body");
	}
}
