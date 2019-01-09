package game;

import java.lang.reflect.Method;

import networking.ObjectSerializer;
import networking.client.Client;

public class GameStateListenerImpl implements GameStateListener {
	private Game game;
	private Client client;

	private boolean loaded = false;

	public GameStateListenerImpl() {
		Client.startServer();

		this.client = new Client();
		client.bind((header, data) -> onLoad(header, data));
	}

	@Override
	public void addObject(Object obj) {
		client.write(obj.getClass().getName(), ObjectSerializer.serialize(obj));
	}

	public void onLoad(String className, byte[] data) {
		if (!loaded) return;
		try {
			String simpleName = className.substring(className.lastIndexOf('.') + 1);
			Method addObj = game.getClass().getMethod("add" + simpleName, Class.forName(className));

			Object obj = ObjectSerializer.deserialize(data);

			addObj.invoke(game, obj);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bind(Game game) {
		this.game = game;
		this.loaded = true;
	}

}
