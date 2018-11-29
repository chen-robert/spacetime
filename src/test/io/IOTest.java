package test.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.EncodedOutputStream;
import io.EncodedReader;
import io.PacketListener;

class IOTest {
	private EncodedOutputStream out;
	private EncodedReader in;

	@BeforeEach
	void setUp() throws IOException {
		PipedInputStream pIn = new PipedInputStream();
		PipedOutputStream pOut = new PipedOutputStream(pIn);

		in = new EncodedReader(pIn);
		new Thread(in).start();
		out = new EncodedOutputStream(pOut);
	}

	@AfterEach
	void tearDown() throws IOException {
		in.close();
		out.close();
	}

	@Test
	void test() {
		testWrite("Hello there!", new byte[] {});

		for (int i = 0; i < 10; i++) {
			testWrite("Hello there!", genByteArray(1 << i));
		}
		for (int i = 0; i < 10; i++) {
			testWrite(new String(genByteArray(1 << i)), new byte[] {});
		}
		for (int i = 0; i < 10; i++) {
			testWrite(new String(genByteArray(1 << i)), genByteArray(1 << i));
		}
	}

	@Test
	void testLarge() {
		for (int i = 0; i < 20; i++) {
			testWrite("Hello there!", genByteArray(1 << i));
		}
	}

	byte[] genByteArray(int size) {
		byte[] ret = new byte[size];
		new Random().nextBytes(ret);

		return ret;
	}

	void testWrite(String header, byte[] data) {
		Object lock = new Object();

		PacketListener pl = (h, b) -> {
			assertTrue(h.equals(h), "Headers mismatched");
			assertTrue(Arrays.equals(data, b), "Bodies mismatched");

			synchronized (lock) {
				lock.notify();
			}
		};

		in.addListener(pl);

		out.write(header, data);

		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
		}

		in.removeListener(pl);

	}

}
