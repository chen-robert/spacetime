package test.networking;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import networking.Serializable;

class SerializableTest {

	@Test
	void testGenerateId() {
		int tests = (int) 1e9;

		HashSet<String> ids = new HashSet<>();
		for (int i = 0; i < tests; i++) {
			ids.add(Serializable.generateId());
		}

		assertTrue(ids.size() == tests, "Failed to generate unique ids. " + (tests - ids.size()) + " dupes.");
	}

}
