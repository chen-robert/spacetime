package networking;

import java.util.stream.IntStream;

public interface Serializable {
	/**
	 * The length of the generated id.
	 */
	public static final int ID_LEN = 32;

	/**
	 * Generates a unique id.
	 * 
	 * @return id
	 */
	public static String generateId() {
		int[] chars = IntStream.range(0, 128)
				.filter(i -> ('A' <= i && i <= 'Z') || ('a' <= i && i <= 'z') || ('0' <= i && i <= '9')).toArray();

		String id = "";
		for (int i = 0; i < ID_LEN; i++) {
			id += chars[(int) (Math.random() * chars.length)];
		}

		return id;
	}

	String id = generateId();

	/**
	 * Returns a unique id for this object.
	 * 
	 * @return unique id
	 */
	public default String getId() {
		return id;
	}

	/**
	 * Returns a representation of the object in bytes. Note that the object must be
	 * able to be reconstructed from this byte array.
	 * 
	 * Specifically, there should be a constructor that takes in a byte array and
	 * returns the object.
	 * 
	 * For example, if the object you are serializing is {@link game.Ship}, there
	 * should be an additional constructor {@link game.Ship(byte[])}. This will be
	 * called via java reflection.
	 * 
	 * @return byte array representing the object
	 */
	public byte[] toBytes();
}