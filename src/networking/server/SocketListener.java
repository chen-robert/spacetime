package networking.server;

import java.net.Socket;

/**
 * Created by Robert on 8/6/2017.
 */
public interface SocketListener {
	boolean addSocket(Socket s);
}
