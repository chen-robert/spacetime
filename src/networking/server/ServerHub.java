package networking.server;

import java.net.Socket;

public class ServerHub implements SocketListener {

	@Override
	public boolean addSocket(Socket s) {
		return false;
	}

}
