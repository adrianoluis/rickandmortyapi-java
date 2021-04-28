package com.rickandmortyapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.rickandmortyapi.util.Urls.asId;

public class Location extends ApiModel<Integer, Location> {

	private final static long serialVersionUID = -4097193502681534337L;

	@Getter
	@Expose(serialize = false)
	String name;

	@Getter
	@Expose(serialize = false)
	String type;

	@Getter
	@Expose(serialize = false)
	String dimension;

	@Expose(serialize = false)
	String url;

	@Expose(serialize = false)
	@SerializedName("residents")
	private List<String> residentsUrl;

	@Getter
	List<Character> residents;

	void copy(final Location other) {
		super.copy(other);
		this.name = other.name;
		this.type = other.type;
		this.dimension = other.dimension;
		this.residents = other.residents;
	}

	public Location() {
		super(Location.class);
	}

	public Location(int id) {
		this();
		setId(id);
	}

	public Location withName(String name) {
		addFilter("name", name);
		return this;
	}

	public Location withType(String type) {
		addFilter("type", type);
		return this;
	}

	public Location withDimension(String dimension) {
		addFilter("dimension", dimension);
		return this;
	}

	@PostConstruct
	public void postConstruct() {
		setId(asId(url));
		if (null != residentsUrl && !residentsUrl.isEmpty()) {
			residents = new ArrayList<>(residentsUrl.size());
			for (String url : residentsUrl) {
				final Character character = new Character();
				character.setId(asId(url));
				residents.add(character);
			}
			residentsUrl = null;
		}
		url = null;
	}

	@Override
	Type getTypeToken() {
		return new CollectionTypeToken().getType();
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Location>> {
	}
}
