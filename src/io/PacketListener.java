package io;

/**
 * Created by Robert on 8/6/2017.
 */
public interface PacketListener {
	void packet(String header, byte[] data);
}
