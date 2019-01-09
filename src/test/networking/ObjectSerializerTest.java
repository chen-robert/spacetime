package test.networking;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import game.Bullet;
import game.OtherShip;
import networking.ObjectSerializer;
import networking.Serializable;

class ObjectSerializerTest {

	@Test
	void test() {
		Bullet bullet = new Bullet("Test Bullet", Serializable.generateId(), randomNum(), randomNum(), randomNum(),
				randomNum());

		Bullet cloned = (Bullet) ObjectSerializer.deserialize(ObjectSerializer.serialize(bullet));

		assertTrue(bullet.getRenderX() == cloned.getRenderX());
		assertTrue(bullet.getRenderY() == cloned.getRenderY());
		assertTrue(bullet.getId().equals(cloned.getId()));
		assertTrue(bullet.getParentId().equals(cloned.getParentId()));

	}

	@Test
	void testOtherShip() {
		OtherShip ship = new OtherShip(randomNum(), randomNum(), randomNum(), randomNum(), Serializable.generateId(),
				"Hi");

		OtherShip cloned = (OtherShip) ObjectSerializer.deserialize(ObjectSerializer.serialize(ship));

		System.out.println(ship.getId());

		assertTrue(ship.getRenderX() == cloned.getRenderX());
		assertTrue(ship.getRenderY() == cloned.getRenderY());
		assertTrue(ship.getId().equals(cloned.getId()));

	}

	private double randomNum() {
		return new Random().nextDouble();
	}

}
