package config;

public class DataLoader {
	public static CraftData getCraftData(String name) {
		return new CraftDataImpl();
	}
}