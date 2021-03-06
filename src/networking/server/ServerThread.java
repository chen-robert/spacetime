package networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robert on 8/6/2017.
 */
public class ServerThread implements Runnable {
	private int port;
	private SocketListener sl;

	public ServerThread(int port, SocketListener sl) {
		this.sl = sl;
		this.port = port;
	}

	@Override
	public void run() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				Socket s = ss.accept();

				new Thread(() -> sl.addSocket(s)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}