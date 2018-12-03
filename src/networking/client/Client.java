package networking.client;

import static main.Main.PORT;

import java.io.IOException;
import java.net.Socket;

import io.EncodedOutputStream;
import io.EncodedReader;
import networking.server.ServerHub;
import networking.server.ServerThread;

public class Client {
	private Socket s;
	private EncodedOutputStream out;
	private EncodedReader in;

	public static void startServer() {
		new ServerThread(PORT, new ServerHub());
	}

	public Client() {
		try {
			s = new Socket("", PORT);

			out = new EncodedOutputStream(s.getOutputStream());
			in = new EncodedReader(s.getInputStream());

			new Thread(in).start();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
