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
		return 0.9;
	}

	@Override
	public double getAcceleration() {
		// TODO Auto-generated method stub
		return 0.5;
	}
	
	@Override
	public double getDeceleration() {
		return 0.3;
	}

	@Override
	public double getMaxSpeed() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public double getTurnSpeed() {
		// TODO Auto-generated method stub
		return 2.5;
	}

	@Override
	public int getMaxHP() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public double getDef() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getDamageMultiplier() {
		// TODO Auto-generated method stub
		return 1;
	}

}
