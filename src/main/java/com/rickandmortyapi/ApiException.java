package com.rickandmortyapi;

import com.google.gson.JsonObject;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;

public class ApiException extends Exception {

	@Getter
	private int returnCode;

	public ApiException() {
		super();
	}

	public ApiException(String message) {
		super(message);
	}

	public static ApiException buildWithError(final Exception error) {
		return new ApiException(error.getMessage());
	}

	public static ApiException buildWithError(final ApiResponse response) {

		if (null == response)
			return new ApiException();

		final JsonObject responseError = Jsons.provider().fromJson(response.getBody(), JsonObject.class);

		ApiException exception = null;
		if (responseError.has("error")) {
			exception = new ApiException(responseError.get("error").getAsString());
			exception.returnCode = response.getCode();
		}

		return exception;
	}
}
