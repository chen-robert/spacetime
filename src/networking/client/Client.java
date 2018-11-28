package networking.client;

import static main.Main.PORT;

import networking.server.ServerHub;
import networking.server.ServerThread;

public class Client {
	public static void startServer() {
		new ServerThread(PORT, new ServerHub());
	}
}
