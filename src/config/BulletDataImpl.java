package config;

public class BulletDataImpl implements BulletData {

	@Override
	public String getName() {
		return "Red Pellet";
	}

	@Override
	public double getInitialSpeed() {
		return 1.5;
	}
	
	@Override
	public double getMaxSpeed() {
		return 2.0;
	}

	@Override
	public double getHitboxRadius() {
		return 2.0;
	}

	@Override
	public double getExplosionRadius() {
		return 1.5;
	}

	@Override
	public double getBaseDamage() {
		return 10;
	}

	@Override
	public String getMovementType() {
		return "Homing";
	}

	@Override
	public double getRebound() {
		return 0.9;
	}

	@Override
	public double getWallDrag() {
		return -1;
	}

	@Override
	public double getMaxLifetime() {
		return 300;
	}
}
