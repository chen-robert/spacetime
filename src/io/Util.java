package io;

import java.util.Arrays;

/**
 * Created by Robert on 8/5/2017.
 */
public class Util {
	/**
	 * Converts an integer to a byte array of length 4.
	 * 
	 * @param n integer to convert
	 * @return converted byte array
	 */
	public static byte[] toBytes(int n) {
		byte[] ret = new byte[4];

		if (n < 0) {
			n -= Integer.MIN_VALUE;
			ret[3] = -128;
		}

		for (int i = 0; i < 4; i++) {
			ret[i] += (byte) (n % 256);
			n /= 256;
		}

		return ret;
	}

	/**
	 * Converts a byte array back into an integer. Reverse of
	 * {@link Util#toBytes(int)}
	 * 
	 * @param b byte array
	 * @return resulting integer
	 * @throws IllegalArgumentException if array length is not 4
	 */
	public static int toInt(byte[] b) throws IllegalArgumentException {
		if (b.length != 4) {
			throw new IllegalArgumentException(String.format("Invalid byte array length. %d != 4", b.length));
		}

		int ret = 0;
		for (int i = 0; i < 3; i++) {
			ret += (int) Math.pow(256, i) * (b[i] < 0 ? b[i] + 256 : b[i]);
		}
		ret += (int) Math.pow(256, 3) * b[3];

		return ret;
	}

	/**
	 * Joins a string array with a given separator.
	 * 
	 * For example, join(["a", "b", "c"], " ") results in "a b c".
	 * 
	 * @param s         array of strings
	 * @param separator string separator
	 * @return joined string
	 */
	public static String join(String[] s, String separator) {
		if (s.length == 0)
			return "";

		StringBuilder ret = new StringBuilder(s[0]);
		for (int i = 1; i < s.length; i++) {
			ret.append(separator).append(s[i]);
		}

		return ret.toString();
	}

	/**
	 * Joins a string array with a space as a separator.
	 * 
	 * @param s array of strings
	 * @return joined string
	 */
	public static String join(String[] s) {
		return join(s, " ");
	}

	/**
	 * Concatenated a series of arrays.
	 * 
	 * @param arr arrays to be concatenated
	 * @return concatenated array
	 */
	public static <T> T[] concat(T[]... arr) {
		int len = Arrays.stream(arr).mapToInt(a -> a.length).sum();

		T[] ret = (T[]) new Object[len];

		int copied = 0;
		for (int i = 0; i < arr.length; i++) {
			System.arraycopy(arr[i], 0, ret, copied, arr[i].length);
			copied += arr[i].length;
		}

		return ret;
	}

	/**
	 * Bounds an integer from min to max
	 * 
	 * @param min min
	 * @param num number
	 * @param max max
	 * @return bounded number
	 */
	public static int bound(int min, int num, int max) {
		return Math.max(Math.min(num, max), min);
	}
}
