package test.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.EncodedOutputStream;
import io.EncodedReader;
import io.PacketListener;
import main.Main;

class IOTest {
	private EncodedOutputStream out;
	private EncodedReader in;
	private Socket s;
	private ServerSocket ss;

	@BeforeEach
	void setUp() throws IOException {
		new Thread(() -> {
			try {
				s = new Socket("", Main.PORT);
				out = new EncodedOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		ss = new ServerSocket(Main.PORT);
		in = new EncodedReader(ss.accept().getInputStream());
		new Thread(in).start();
	}

	@AfterEach
	void tearDown() throws IOException {
		in.close();
		out.close();

		s.close();
		ss.close();
	}

	@Test
	void test() {
		testWrite("Hello there!", new byte[] {});
	}

	@Test
	void testBasicIO() {
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
