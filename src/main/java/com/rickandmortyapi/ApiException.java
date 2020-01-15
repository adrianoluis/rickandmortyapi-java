package com.rickandmortyapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class ApiException extends Exception {

	@Getter
	private int returnCode;

	@Getter
	private String url;

	@Getter
	private String method;

	private String parameterMap;

	private String type;

	@Getter
	private Collection<String> errors = new ArrayList<>();

	public static ApiException buildWithError(final Exception error) {
		return new ApiException(error.getMessage(), null);
	}

	@SuppressWarnings("unchecked")
	public static ApiException buildWithError(final ApiResponse response) {

		if (null == response)
			return null;

		final JsonObject responseError = Jsons.provider().fromJson(response.getBody(), JsonObject.class);

		ApiException exception = null;
		if (responseError.has("error")) {
			exception = new ApiException(responseError.get("error").getAsString(), responseError);
			exception.returnCode = response.getCode();
		}

		if (responseError.has("errors")) {
			final JsonArray errors = responseError.getAsJsonArray("errors");
			final StringBuilder joinedMessages = new StringBuilder();

			for (int i = 0; i < errors.size(); i++) {
				final JsonObject error = errors.get(i).getAsJsonObject();
				joinedMessages
						.append(error.get("message").getAsString())
						.append("\n");
			}

			exception = new ApiException(joinedMessages.toString(), responseError);
			exception.returnCode = response.getCode();
		}

		return exception;
	}

	public ApiException(int returnCode, String url, String method, String message) {
		super(message);
		this.returnCode = returnCode;
		this.url = url;
		this.method = method;
	}

	public ApiException(final String message) {
		this(message, null);
	}

	@SuppressWarnings("unchecked")
	public ApiException(final String message, final JsonObject responseError) {
		super(message);

		if (null == responseError || !responseError.has("errors"))
			return;

		this.url = responseError.get("url").getAsString();
		this.method = responseError.get("method").getAsString();

		final JsonArray errors = responseError.get("errors").getAsJsonArray();

		for (int i = 0; i < errors.size(); i++) {
			final JsonObject error = errors.get(i).getAsJsonObject();
			this.errors.add(error.getAsString());
		}
	}
}
