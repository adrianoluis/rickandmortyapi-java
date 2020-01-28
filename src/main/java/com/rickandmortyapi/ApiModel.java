package com.rickandmortyapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.ws.rs.HttpMethod;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

abstract class ApiModel<PK extends Serializable> {

	@Expose(serialize = false)
	@Getter
	@Setter
	@SerializedName("id")
	private PK id;

	@Expose(serialize = false)
	@Getter
	@SerializedName("created")
	private ZonedDateTime created;

	@Getter
	private transient String className;

	private transient final Map<String, Object> filters = new HashMap<>();

	public ApiModel() {
		className = getClass().getSimpleName().toLowerCase();
	}

	void resetFilters() {
		filters.clear();
	}

	void copy(final ApiModel<PK> other) {
		this.created = other.created;
	}

	protected void validateId() {
		if (getId() == null) {
			throw new IllegalArgumentException("The Object ID must be set in order to use this method.");
		}
	}

	protected void validateIdFilters() {
		if (filters == null || filters.isEmpty()) {
			throw new IllegalArgumentException("No filter criteria defined.");
		}
	}

	void addFilter(@NonNull String query, @NonNull Object value) {
		filters.put(query, value);
	}

	protected JsonObject refreshModel() throws ApiException {
		return get(this.id);
	}

	protected JsonObject get(final PK id) throws ApiException {
		validateId();
		return new ApiRequest(HttpMethod.GET, String.format("/%s/%s", className, id)).execute();
	}

	protected JsonArray get(List<PK> ids) throws ApiException {
		final String strIds = ids.stream()
				.map(Objects::toString)
				.collect(Collectors.joining(","));
		final String path = String.format("/%s/%s", className, strIds);
		return new ApiRequest(HttpMethod.GET, path).execute();
	}

	protected JsonArray query() throws ApiException {
		validateIdFilters();
		final String path = String.format("/%s", className);

		try {
			final ApiRequest request = new ApiRequest(HttpMethod.GET, path);
			request.setParameters(filters);

			final JsonObject response = request.execute();
			return response.getAsJsonArray("results");
		} catch (ApiException e) {
			return new JsonArray();
		}
	}

	protected JsonArray next(Integer page) throws ApiException {
		if (null == page || 0 >= page) {
			page = 1;
		}

		filters.put("page", page);

		try {
			final ApiRequest request = new ApiRequest(HttpMethod.GET, String.format("/%s", className));
			request.setParameters(filters);

			final JsonObject response = request.execute();
			return response.getAsJsonArray("results");
		} catch (ApiException e) {
			return new JsonArray();
		}
	}
}
