package config;

public class DataLoader {
	static {

	}

	public static CraftData getCraftData(String name) {
		return new CraftDataImpl();
	}

	public static BulletData getBulletData(String name) {
		return new BulletDataImpl();
	}
}