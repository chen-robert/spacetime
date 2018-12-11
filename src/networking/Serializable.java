package networking;

import java.util.stream.IntStream;

public interface Serializable {
	/**
	 * The length of the generated id.
	 */
	public static final int ID_LEN = 32;
	static final int[] ID_CHARS = IntStream.range(0, 128)
			.filter(i -> ('A' <= i && i <= 'Z') || ('a' <= i && i <= 'z') || ('0' <= i && i <= '9')).toArray();

	/**
	 * Generates a unique id.
	 *
	 * @return id
	 */
	public static String generateId() {
		byte[] data = new byte[ID_LEN];
		for (int i = 0; i < ID_LEN; i++) {
			data[i] = (byte) ID_CHARS[(int) (Math.random() * ID_CHARS.length)];
		}
		return new String(data);
	}

	/**
	 * Returns a representation of the object in bytes. Note that the object
	 * must be able to be reconstructed from this byte array.
	 *
	 * Specifically, there should be a constructor that takes in a byte array
	 * and returns the object.
	 *
	 * For example, if the object you are serializing is {@link game.Ship},
	 * there should be an additional constructor {@link game.Ship(byte[])}. This
	 * will be called via java reflection.
	 *
	 * @return byte array representing the object
	 */
	public byte[] toBytes();

	/**
	 * Returns a unique id for this object.
	 *
	 * @return unique id
	 */
	public String getId();

}
