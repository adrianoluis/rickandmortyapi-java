package com.rickandmortyapi;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class EpisodeTest {

	private Episode episode;

	@Before
	public void setUp() {
		episode = new Episode();
	}

	@Test
	public void testGet_withIds5and10() throws Throwable {
		final Episode s01e10 = episode.get(5, 10).stream()
				.reduce((first, second) -> second)
				.orElse(null);
		assertNotNull(s01e10);
		assertEquals("Close Rick-counters of the Rick Kind", s01e10.getName());
	}

	@Test
	public void testRefresh_withId1() throws Throwable {
		episode.setId(1);
		episode.refresh();
		assertEquals("Pilot", episode.getName());
	}

	@Test
	public void testFilter_withPilotS01E01() {
		episode.withName("Pilot")
				.withNumber("S01E01")
				.filter()
				.forEach(character -> {
					assertTrue(character.getName().contains("Pilot"));
					assertEquals("S01E01", character.getNumber());
				});
	}

	@Test
	public void testList_withPage2() {
		final Collection<Episode> episodes = episode.list(2);
		assertFalse(episodes.isEmpty());
		assertEquals(20, episodes.size());
	}

	@Test
	public void testList_withPage999() {
		final Collection<Episode> episodes = episode.list(999);
		assertTrue(episodes.isEmpty());
	}
}
