package config;

public class DataLoader {
	public static ShipData getShipData(String name) {
		return new ShipDataImpl();
	}
}