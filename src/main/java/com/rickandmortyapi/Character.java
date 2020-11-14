package com.rickandmortyapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rickandmortyapi.util.Urls.asId;
import static java.lang.Integer.parseInt;

public class Character extends ApiModel<Integer, Character> {

	private final static long serialVersionUID = -6814865163305560069L;

	@Getter
	@Expose(serialize = false)
	String name;

	@Getter
	@Expose(serialize = false)
	Status status;

	@Getter
	@Expose(serialize = false)
	String species;

	@Getter
	@Expose(serialize = false)
	String type;

	@Getter
	@Expose(serialize = false)
	Gender gender;

	@Getter
	@Expose(serialize = false)
	@SerializedName("origin")
	Location originLocation;

	@Getter
	@Expose(serialize = false)
	@SerializedName("location")
	Location lastKnownLocation;

	@Getter
	@Expose(serialize = false)
	URL image;

	@Expose(serialize = false)
	@SerializedName("episode")
	private List<String> episodesUrl;

	@Getter
	List<Episode> episodes;

	void copy(final Character other) {
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
		super(Character.class);
	}

	public Character(int id) {
		this();
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

	@Override
	Type getTypeToken() {
		return new CollectionTypeToken().getType();
	}

	@PostConstruct
	public void postConstruct() {
		if (null != episodesUrl && !episodesUrl.isEmpty()) {
			episodes = new ArrayList<Episode>(episodesUrl.size());
			for (String url : episodesUrl) {
				final Episode episode = new Episode();
				episode.setId(asId(url));
				episodes.add(episode);
			}
			episodesUrl = null;
		}
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Character>> {
	}

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
