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

public class Location extends ApiModel<Integer> {

	private final static long serialVersionUID = -4097193502681534337L;

	@Getter
	@Expose(serialize = false)
	private String name;

	@Getter
	@Expose(serialize = false)
	private String type;

	@Getter
	@Expose(serialize = false)
	private String dimension;

	@Expose(serialize = false)
	private String url;

	@Expose(serialize = false)
	@SerializedName("residents")
	private List<String> residentsUrl;

	@Getter
	private List<Character> residents;

	private void copy(Location other) {
		super.copy(other);
		this.name = other.name;
		this.type = other.type;
		this.dimension = other.dimension;
		this.residents = other.residents;
	}

	public Location() {
		super();
	}

	public Location(int id) {
		setId(id);
	}

	public Location refresh() throws ApiException {
		final Location other = Jsons.asObject(refreshModel(), Location.class);
		copy(other);
		return other;
	}

	public Collection<Location> get(Integer... ids) throws ApiException {
		return Jsons.asCollection(super.get(Arrays.asList(ids)), TYPE_TOKEN);
	}

	public Collection<Location> list() throws ApiException {
		return Jsons.asCollection(super.next(1), TYPE_TOKEN);
	}

	public Collection<Location> list(Integer page) throws ApiException {
		return Jsons.asCollection(super.next(page), TYPE_TOKEN);
	}

	@PostConstruct
	public void postConstruct() {
		setId(asId(url, Integer::new));
		if (null != residentsUrl && !residentsUrl.isEmpty()) {
			residents = new ArrayList<>(residentsUrl.size());
			for (String url : residentsUrl) {
				final Character character = new Character();
				character.setId(asId(url, Integer::new));
				residents.add(character);
			}
			residentsUrl = null;
		}
		url = null;
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Location>> {
	}

	private static final Type TYPE_TOKEN = new CollectionTypeToken().getType();
}
