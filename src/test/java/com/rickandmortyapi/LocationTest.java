package com.rickandmortyapi;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class LocationTest {

	private Location location;

	@Before
	public void setUp() {
		location = new Location();
	}

	@Test
	public void testGet_withIds5and10() {
		final Location venzenulon7 = location.get(5, 10).stream()
				.reduce((first, second) -> second)
				.orElse(null);
		assertNotNull(venzenulon7);
		assertEquals("Venzenulon 7", venzenulon7.getName());
	}

	@Test
	public void testRefresh_withId1() {
		location.setId(1);
		location.refresh();
		assertEquals("Earth (C-137)", location.getName());
	}

	@Test
	public void testGet_withInvalidId() {
		location.setId(-1);
		location.refresh();
		assertNull(location.getName());
	}

	@Test
	public void testFilter_withEarthPlanet() {
		location.withName("Earth")
				.withType("Planet")
				.filter()
				.forEach(character -> {
					assertTrue(character.getName().contains("Earth"));
					assertEquals("Planet", character.getType());
				});
	}

	@Test
	public void testList_withPage2() {
		final Collection<Location> locations = location.list(2);
		assertFalse(locations.isEmpty());
		assertEquals(20, locations.size());
	}

	@Test
	public void testList_withPage999() {
		final Collection<Location> locations = location.list(999);
		assertTrue(locations.isEmpty());
	}
}
