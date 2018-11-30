package config;

public class CraftDataImpl implements CraftData {

	@Override
	public String getName() {
		return "Test";
	}

	@Override
	public double getHitboxRadius() {
		return 5;
	}

	@Override
	public double getRebound() {
		return 0.5;
	}

	@Override
	public double getAcceleration() {
		return 0.5;
	}

	@Override
	public double getDeceleration() {
		return 0.3;
	}

	@Override
	public double getMaxSpeed() {
		return 3;
	}

	@Override
	public double getTurnSpeed() {
		return 2.5;
	}

	@Override
	public int getMaxHP() {
		return 10;
	}

	@Override
	public double getDef() {
		return 1;
	}

	@Override
	public double getDamageMultiplier() {
		return 1;
	}

}
