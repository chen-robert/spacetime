package config;

public class DataLoader {
	static {

	}

	public static CraftData getCraftData(String name) {
		return new CraftDataImpl();
	}
}