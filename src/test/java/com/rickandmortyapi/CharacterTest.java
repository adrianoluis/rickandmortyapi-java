package com.rickandmortyapi;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

public class CharacterTest {

	private Character character;

	@Before
	public void setUp() {
		character = new Character();
	}

	@Test
	public void testGet_withIds5and10() {
		final Character alanRails = character.get(5, 10).stream()
				.reduce((first, second) -> second)
				.orElse(null);
		assertNotNull(alanRails);
		assertEquals("Alan Rails", alanRails.getName());
	}

	@Test
	public void testRefresh_withId1() {
		character.setId(1);
		character.refresh();
		assertEquals("Rick Sanchez", character.getName());
	}

	@Test
	public void testGet_withInvalidId() {
		character.setId(-1);
		character.refresh();
		assertNull(character.getName());
	}

	@Test
	public void testFilter_withRickAlive() {
		character.withName("rick")
				.withStatus(Character.Status.ALIVE)
				.filter()
				.forEach(character -> {
					assertTrue(character.getName().contains("Rick"));
					assertEquals(Character.Status.ALIVE, character.getStatus());
				});
	}

	@Test
	public void testFilter_withAnnet() {
		assertTrue(character.withName("annet")
				.filter()
				.isEmpty());
	}

	@Test
	public void testFilter_withRick_withPage2() {
		character.withName("rick")
				.filter(3)
				.forEach(character -> assertTrue(character.getName().contains("Rick")));
	}

	@Test
	public void testList_withPage2() {
		final Collection<Character> characters = character.list(2);
		assertFalse(characters.isEmpty());
		assertEquals(20, characters.size());
	}

	@Test
	public void testList_withPage999() {
		final Collection<Character> characters = character.list(999);
		assertTrue(characters.isEmpty());
	}
}
