package test.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.Util;

class UtilTest {

	@Test
	void testIntConversion() {
		for (int i = (int) -1e6; i <= 1e6; i++) {
			assertTrue(Util.toInt(Util.toBytes(i)) == i, "Failed conversion for " + i);
		}
	}

}
