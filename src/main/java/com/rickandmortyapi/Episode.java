package com.rickandmortyapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.rickandmortyapi.util.Urls.asId;

public class Episode extends ApiModel<Integer> {

	private final static long serialVersionUID = 1761212523288968116L;

	@Getter
	@Expose(serialize = false)
	private String name;

	@Getter
	@Expose(serialize = false)
	@SerializedName("air_date")
	private LocalDate showDate;

	@Getter
	@Expose(serialize = false)
	@SerializedName("episode")
	private String number;

	@Expose(serialize = false)
	private String url;

	@Expose(serialize = false)
	@SerializedName("characters")
	private List<String> charactersUrl;

	@Getter
	private List<Character> characters;

	private void copy(Episode other) {
		super.copy(other);
		this.name = other.name;
		this.showDate = other.showDate;
		this.number = other.number;
		this.characters = other.characters;
	}

	public Episode() {
		super();
	}

	public Episode(int id) {
		setId(id);
	}

	public Episode withName(String name) {
		addFilter("name", name);
		return this;
	}

	public Episode withNumber(String number) {
		addFilter("episode", number);
		return this;
	}

	public Episode refresh() throws ApiException {
		final Episode other = Jsons.asObject(refreshModel(), Episode.class);
		copy(other);
		return other;
	}

	public Collection<Episode> get(Integer... ids) throws ApiException {
		return Jsons.asCollection(super.get(Arrays.asList(ids)), TYPE_TOKEN);
	}

	public Collection<Episode> filter() throws ApiException {
		return Jsons.asCollection(super.query(), TYPE_TOKEN);
	}

	public Collection<Episode> list() throws ApiException {
		return Jsons.asCollection(super.next(1), TYPE_TOKEN);
	}

	public Collection<Episode> list(Integer page) throws ApiException {
		return Jsons.asCollection(super.next(page), TYPE_TOKEN);
	}

	@PostConstruct
	public void postConstruct() {
		setId(asId(url, Integer::new));
		if (null != charactersUrl && !charactersUrl.isEmpty()) {
			characters = new ArrayList<>(charactersUrl.size());
			for (String url : charactersUrl) {
				final Character character = new Character();
				character.setId(asId(url, Integer::new));
				characters.add(character);
			}
			charactersUrl = null;
		}
		url = null;
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Episode>> {
	}

	private static final Type TYPE_TOKEN = new CollectionTypeToken().getType();
}
