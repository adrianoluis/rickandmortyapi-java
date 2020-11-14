package com.rickandmortyapi;

import com.google.gson.JsonElement;
import com.rickandmortyapi.util.Jsons;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {

	private static final String ENDPOINT = "https://rickandmortyapi.com/api";

	private final String path;

	private final String method;

	private final Map<String, String> headers;

	private Map<String, Object> parameters;

	public ApiRequest(String method, String path) {
		this(method, path, null);
	}

	public ApiRequest(String method, String path, Map<String, String> headers) {
		this.path = path;
		this.method = method;
		this.headers = headers;
		this.parameters = new HashMap<String, Object>();
	}

	public static String fullApiUrl(final String path) {
		return ENDPOINT.concat(path);
	}

	@SuppressWarnings("unchecked")
	public <T> T execute() throws ApiException {
		final RestClient client = new RestClient(method, fullApiUrl(path), parameters, headers);
		final ApiResponse response = client.execute();

		final JsonElement decoded = Jsons.provider().fromJson(response.getBody(), JsonElement.class);

		if (response.getCode() == 200) {
			return (T) decoded;
		} else {
			throw ApiException.buildWithError(response);
		}

	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
