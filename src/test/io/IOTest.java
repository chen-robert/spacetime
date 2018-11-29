package test.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

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
