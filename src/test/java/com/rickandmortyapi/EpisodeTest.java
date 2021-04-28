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
	public void testGet_withIds5and10() {
		final Episode s01e05 = episode.get(5, 10)
				.iterator()
				.next();
		assertNotNull(s01e05);
		assertEquals("Meeseeks and Destroy", s01e05.getName());
	}

	@Test
	public void testRefresh_withId1() {
		episode = new Episode(1);
		episode.refresh();
		assertEquals("Pilot", episode.getName());
	}

	@Test
	public void testGet_withInvalidId() {
		episode.setId(-1);
		episode.refresh();
		assertNull(episode.getName());
	}

	@Test
	public void testFilter_withPilotS01E01() {
		for (Episode episode : episode.withName("Pilot")
				.withNumber("S01E01")
				.filter()) {
			assertTrue(episode.getName().contains("Pilot"));
			assertEquals("S01E01", episode.getNumber());
		}
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
