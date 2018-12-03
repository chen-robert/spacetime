package networking.client;

import static main.Main.PORT;

import java.io.IOException;
import java.net.Socket;

import io.EncodedOutputStream;
import io.EncodedReader;
import io.PacketListener;
import networking.server.ServerHub;
import networking.server.ServerThread;

public class Client {
	private Socket s;
	private EncodedOutputStream out;
	private EncodedReader in;

	public static void startServer() {
		new Thread(new ServerThread(PORT, new ServerHub())).start();
	}

	public Client() {
		try {
			s = new Socket("127.0.0.1", PORT);

			out = new EncodedOutputStream(s.getOutputStream());
			in = new EncodedReader(s.getInputStream());

			new Thread(in).start();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void write(String header, byte[]... data) {
		out.write(header, data);
	}

	public void bind(PacketListener pl) {
		in.addListener(pl);
	}
}
