package networking.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;

import io.EncodedOutputStream;
import io.EncodedReader;

public class ServerHub implements SocketListener {

	class SocketData {
		public final Socket s;
		public final EncodedOutputStream out;
		public final EncodedReader in;

		SocketData(Socket s) throws IOException {
			this.s = s;
			this.out = new EncodedOutputStream(s.getOutputStream());
			this.in = new EncodedReader(s.getInputStream());

			new Thread(this.in).start();
		}
	}

	private final Collection<SocketData> sockets = new HashSet<>();

	@Override
	public boolean addSocket(Socket s) {
		try {
			SocketData curr = new SocketData(s);

			curr.in.addListener((header, data) -> sendAll(header, data));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void sendAll(String header, byte[] data) {
		sockets.forEach(sd -> {
			try {

				sd.out.writeWarn(header, data);
			} catch (IOException ioe) {
				try {
					sd.s.close();
					sd.out.close();
					sd.in.close();
				} catch (IOException e) {
				}
			}
		});

	}
}
