package com.rickandmortyapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.threeten.bp.ZonedDateTime;

import javax.ws.rs.HttpMethod;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

abstract class ApiModel<PK extends Serializable, T extends ApiModel> {

	private final transient Class<T> typeClass;

	@Getter
	private final transient String className;

	private transient final Map<String, Object> filters = new HashMap<String, Object>();

	@Expose(serialize = false)
	@Getter
	@Setter
	@SerializedName("id")
	private PK id;

	@Expose(serialize = false)
	@Getter
	@SerializedName("created")
	ZonedDateTime created;

	public ApiModel(Class<T> typeClass) {
		this.className = getClass().getSimpleName().toLowerCase();
		this.typeClass = typeClass;
	}

	void resetFilters() {
		filters.clear();
	}

	void copy(final T other) {
		this.created = other.created;
	}

	protected void validateId() {
		if (getId() == null) {
			throw new IllegalArgumentException("The Object ID must be set in order to use this method.");
		}
	}

	protected void validateFilters() {
		if (filters == null || filters.isEmpty()) {
			throw new IllegalArgumentException("No filter criteria defined.");
		}
	}

	void addFilter(@NonNull String query, @NonNull Object value) {
		filters.put(query, value);
	}

	protected JsonObject get(final PK id) {
		validateId();
		try {
		return new ApiRequest(HttpMethod.GET, String.format("/%s/%s", className, id)).execute();
		} catch (ApiException e) {
			return new JsonObject();
		}
	}

	protected JsonArray get(List<PK> ids) {
		final StringBuilder strIds = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			strIds.append(ids.get(i).toString());
			if (i != ids.size() - 1) {
				strIds.append(",");
			}
		}
		try {
			final String path = String.format("/%s/%s", className, strIds.toString());
			return new ApiRequest(HttpMethod.GET, path).execute();
		} catch (ApiException e) {
			return new JsonArray();
		}
	}

	protected JsonArray queryAll() {
		validateFilters();
		return nextAll();
	}

	protected JsonArray query(Integer page) {
		validateFilters();
		return next(page);
	}
	
	protected JsonArray nextAll() {
		Integer page = 1;

		JsonArray fullResponse = new JsonArray();
		JsonElement indicator = null;

		do {
			filters.put("page", page);

			try {
				final ApiRequest request = new ApiRequest(HttpMethod.GET, String.format("/%s", className));
				request.setParameters(filters);

				final JsonObject response = request.execute();

				fullResponse.addAll(response.getAsJsonArray("results"));
				indicator = response.get("info").getAsJsonObject().get("next");

				page++;

			} catch (ApiException e) {
				return new JsonArray();
			}
		} while (!indicator.isJsonNull());

		return fullResponse;
	}

	protected JsonArray next(Integer page) {
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

	public T refresh() {
		final T other = Jsons.asObject(get(id), typeClass);
		copy(other);
		return other;
	}

	public Collection<T> get(PK... ids) {
		return Jsons.asCollection(get(Arrays.asList(ids)), getTypeToken());
	}

	public Collection<T> filter() {
		return Jsons.asCollection(queryAll(), getTypeToken());
	}

	public Collection<T> filter(Integer page) {
		return Jsons.asCollection(query(page), getTypeToken());
	}

	public Collection<T> list() {
		return Jsons.asCollection(nextAll(), getTypeToken());
	}

	public Collection<T> list(Integer page) {
		return Jsons.asCollection(next(page), getTypeToken());
	}

	abstract Type getTypeToken();
}
