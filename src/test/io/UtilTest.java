package test.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.Util;
import networking.Serializable;

class UtilTest {

	@Test
	void testIntConversion() {
		for (int i = (int) -1e6; i <= 1e6; i++) {
			testIntConversion(i);
		}
		testIntConversion(Integer.MIN_VALUE);
		testIntConversion(Integer.MAX_VALUE);

		assertThrows(IllegalArgumentException.class, () -> Util.toInt(new byte[] { 1 }));
	}

	void testIntConversion(int n) {
		assertTrue(Util.toInt(Util.toBytes(n)) == n, "Failed conversion for " + n);
	}

	@Test
	void testJoin() {
		assertEquals(Util.join(new String[] { "Hello", "World", "!" }), "Hello World !");
		assertEquals(Util.join(new String[] { "Hello", "World", "!" }, " . "), "Hello . World . !");
		assertEquals(Util.join(new String[] {}, "anything_here"), "");
	}

	@Test
	void testConcat() {
		String[] data = new String[100];
		for (int i = 0; i < data.length; i++) {
			data[i] = Serializable.generateId();
		}

		for (int i = 0; i < data.length; i++) {
			assertArrayEquals(Util.concat(Arrays.copyOfRange(data, 0, i), Arrays.copyOfRange(data, i, data.length)),
					data, "Failed concat operation");
		}
	}

}
