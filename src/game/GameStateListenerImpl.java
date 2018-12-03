package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import networking.Serializable;

public class GameStateListenerImpl implements GameStateListener {
	private Game game;

	@Override
	public void addObject(Serializable obj) {
		onLoad(obj.getClass().getName(), obj.toBytes());
	}

	public void onLoad(String className, byte[] data) {
		try {
			String simpleName = className.substring(className.lastIndexOf('.') + 1);
			Method addObj = game.getClass().getMethod("add" + simpleName, Class.forName(className));
			Constructor construct = Class.forName(className).getConstructor(byte[].class);

			Object obj = construct.newInstance(data);

			addObj.invoke(game, obj);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bind(Game g) {
		this.game = g;
	}

}
