package com.rickandmortyapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.threeten.bp.LocalDate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rickandmortyapi.util.Urls.asId;

public class Episode extends ApiModel<Integer, Episode> {

	private final static long serialVersionUID = 1761212523288968116L;

	@Getter
	@Expose(serialize = false)
	String name;

	@Getter
	@Expose(serialize = false)
	@SerializedName("air_date")
	LocalDate showDate;

	@Getter
	@Expose(serialize = false)
	@SerializedName("episode")
	String number;

	@Expose(serialize = false)
	String url;

	@Expose(serialize = false)
	@SerializedName("characters")
	private List<String> charactersUrl;

	@Getter
	List<Character> characters;

	void copy(final Episode other) {
		super.copy(other);
		this.name = other.name;
		this.showDate = other.showDate;
		this.number = other.number;
		this.characters = other.characters;
	}

	public Episode() {
		super(Episode.class);
	}

	public Episode(int id) {
		this();
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

	@PostConstruct
	public void postConstruct() {
		setId(asId(url));
		if (null != charactersUrl && !charactersUrl.isEmpty()) {
			characters = new ArrayList<Character>(charactersUrl.size());
			for (String url : charactersUrl) {
				final Character character = new Character();
				character.setId(asId(url));
				characters.add(character);
			}
			charactersUrl = null;
		}
		url = null;
	}

	@Override
	Type getTypeToken() {
		return new CollectionTypeToken().getType();
	}

	private static class CollectionTypeToken extends TypeToken<Collection<Episode>> {
	}
}
