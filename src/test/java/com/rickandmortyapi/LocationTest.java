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
	public void testGet_withIds5and10() throws Throwable {
		final Location venzenulon7 = location.get(5, 10).stream()
				.reduce((first, second) -> second)
				.orElse(null);
		assertNotNull(venzenulon7);
		assertEquals("Venzenulon 7", venzenulon7.getName());
	}

	@Test
	public void testRefresh_withId1() throws Throwable {
		location.setId(1);
		location.refresh();
		assertEquals("Earth (C-137)", location.getName());
	}

	@Test
	public void testList_withPage2() throws Throwable {
		final Collection<Location> locations = location.list(2);
		assertFalse(locations.isEmpty());
		assertEquals(20, locations.size());
	}

	@Test
	public void testList_withPage999() throws Throwable {
		final Collection<Location> locations = location.list(999);
		assertTrue(locations.isEmpty());
		assertEquals(0, locations.size());
	}
}
