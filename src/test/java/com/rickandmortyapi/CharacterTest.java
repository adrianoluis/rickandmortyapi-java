package com.rickandmortyapi;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class CharacterTest {

	private Character character;

	@Before
	public void setUp() {
		character = new Character();
	}

	@Test
	public void testGet_withIds5and10() {
		final Character jerrySmith = character.get(5, 10)
				.iterator()
				.next();
		assertNotNull(jerrySmith);
		assertEquals("Jerry Smith", jerrySmith.getName());
	}

	@Test
	public void testRefresh_withId1() {
		character = new Character(1);
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
		for (Character rick : character.withName("rick")
				.withStatus(Character.Status.ALIVE)
				.filter()) {
			assertTrue(rick.getName().contains("Rick"));
			assertEquals(Character.Status.ALIVE, rick.getStatus());
		}
	}

	@Test
	public void testFilter_withAnnet() {
		assertTrue(character.withName("annet")
				.filter()
				.isEmpty());
	}

	@Test
	public void testFilter_withRick_withPage2() {
		for (Character rick : character.withName("rick").filter(2)) {
			assertTrue(rick.getName().contains("Rick"));
		}
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
