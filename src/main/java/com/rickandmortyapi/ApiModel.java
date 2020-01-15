package com.rickandmortyapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.HttpMethod;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class ApiModel<PK extends Serializable> {

	/**
	 * Model identification number
	 */
	@Expose(serialize = false)
	@Getter
	@Setter
	@SerializedName("id")
	private PK id;

	/**
	 * Time at which the model was created in the database.
	 */
	@Expose(serialize = false)
	@Getter
	@SerializedName("created")
	private ZonedDateTime created;

	/**
	 * Lowercase class name
	 */
	@Getter
	private transient String className;

	public ApiModel() {
		className = getClass().getSimpleName().toLowerCase();
	}

	void copy(final ApiModel<PK> other) {
		this.created = other.created;
	}

	/**
	 * Valida se o atributo {@link #id} foi preenchido.
	 */
	protected void validateId() {
		if (getId() == null) {
			throw new IllegalArgumentException("The Object ID must be set in order to use this method.");
		}
	}

	/**
	 * Refresh model state
	 *
	 * @return A representação atualizada do estado do modelo
	 * @throws ApiException
	 */
	protected JsonObject refreshModel() throws ApiException {
		return get(this.id);
	}

	/**
	 * Gets a model by it's ID.
	 *
	 * @param id Model ID
	 * @return Model state
	 * @throws ApiException
	 */
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

	/**
	 * @param page
	 * @return
	 * @throws ApiException
	 */
	protected JsonArray next(Integer page) throws ApiException {
		final Map<String, Object> parameters = new HashMap<>();

		if (null == page || 0 >= page) {
			page = 1;
		}

		parameters.put("page", page);

		try {
			final ApiRequest request = new ApiRequest(HttpMethod.GET, String.format("/%s", className));
			request.getParameters().putAll(parameters);

			final JsonObject response = request.execute();
			return response.getAsJsonArray("results");
		} catch (ApiException e) {
			return new JsonArray();
		}
	}
}
