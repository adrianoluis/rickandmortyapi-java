package com.rickandmortyapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.rickandmortyapi.util.Jsons;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.rickandmortyapi.util.Urls.asId;

public class Character extends ApiModel<Integer> {

	private final static long serialVersionUID = -6814865163305560069L;

	@Getter
	@Expose(serialize = false)
	private String name;

	@Getter
	@Expose(serialize = false)
	private Status status;

	@Getter
	@Expose(serialize = false)
	private String species;

	@Getter
	@Expose(serialize = false)
	private String type;

	@Getter
	@Expose(serialize = false)
	private Gender gender;

	@Getter
	@Expose(serialize = false)
	@SerializedName("origin")
	private Location originLocation;

	@Getter
	@Expose(serialize = false)
	@SerializedName("location")
	private Location lastKnownLocation;

	@Getter
	@Expose(serialize = false)
	private URL image;

	@Expose(serialize = false)
	@SerializedName("episode")
	private List<String> episodesUrl;

	@Getter
	private List<Episode> episodes;

	private void copy(Character other) {
		super.copy(other);
		this.name = other.name;
		this.status = other.status;
		this.species = other.species;
		this.type = other.type;
		this.gender = other.gender;
		this.originLocation = other.originLocation;
		this.lastKnownLocation = other.lastKnownLocation;
		this.image = other.image;
		this.episodes = other.episodes;
	}

	public Character() {
		super();
	}

	public Character(int id) {
		setId(id);
	}

	public Character withName(String name) {
		addFilter("name", name);
		return this;
	}

	public Character withStatus(Status status) {
		addFilter("status", status);
		return this;
	}

	public Character withSpecies(String species) {
		addFilter("species", species);
		return this;
	}

	public Character withType(String type) {
		addFilter("type", type);
		return this;
	}

	public Character withGender(Gender gender) {
		addFilter("gender", gender);
		return this;
	}

	public Character refresh() throws ApiException {
		final Character other = Jsons.asObject(refreshModel(), Character.class);
		copy(other);
		return other;
	}

	public Collection<Character> get(Integer... ids) throws ApiException {
		return Jsons.asCollection(super.get(Arrays.asList(ids)), TYPE_TOKEN);
	}

	public Collection<Character> filter() {
		return Jsons.asCollection(super.query(), TYPE_TOKEN);
	}

	public Collection<Character> filter(Integer page) {
		return Jsons.asCollection(super.query(page), TYPE_TOKEN);
	}

	public Collection<Character> list() {
		return Jsons.asCollection(super.next(1), TYPE_TOKEN);
	}

	public Collection<Character> list(Integer page) {
		return Jsons.asCollection(super.next(page), TYPE_TOKEN);
	}

	@PostConstruct
	public void postConstruct() {
		if (null != episodesUrl && !episodesUrl.isEmpty()) {
			episodes = new ArrayList<>(episodesUrl.size());
			for (String url : episodesUrl) {
				final Episode episode = new Episode();
				episode.setId(asId(url, Integer::parseInt));
				episodes.add(episode);
			}
			episodesUrl = null;
		}
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Character>> {
	}

	private static final Type TYPE_TOKEN = new CollectionTypeToken().getType();

	public enum Gender {

		@SerializedName("unknown")
		UNKNOWN,

		@SerializedName("Female")
		FEMALE,

		@SerializedName("Male")
		MALE,

		@SerializedName("Genderless")
		GENDERLESS
	}

	public enum Status {

		@SerializedName("unknown")
		UNKNOWN,

		@SerializedName("Alive")
		ALIVE,

		@SerializedName("Dead")
		DEAD
	}
}
