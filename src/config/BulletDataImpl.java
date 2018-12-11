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
		return "Linear";
	}

	@Override
	public double getRebound() {
		return -1;
	}

	@Override
	public double getWallDrag() {
		return 0.01;
	}

	@Override
	public double getMaxLifetime() {
		return 300;
	}
}
