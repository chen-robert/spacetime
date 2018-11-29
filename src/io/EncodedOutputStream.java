package io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Robert on 8/5/2017.
 */
public class EncodedOutputStream {
	private OutputStream out;

	public EncodedOutputStream(OutputStream out) {
		this.out = out;
	}

	/**
	 * Writes a packet with a string header.
	 *
	 * @param header
	 * @param data
	 * @throws IOException if the underlying outputstream errors
	 */
	public void writeWarn(String header, byte[]... data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		out.write(Util.toBytes(header.length()));
		out.write(header.getBytes());

		int tot = Arrays.stream(data).mapToInt(arr -> arr.length).sum();
		out.write(Util.toBytes(tot));

		for (byte[] aData : data) {
			out.write(aData);
		}

		synchronized (this.out) {
			byte[] finalData = out.toByteArray();

			int blockSize = 1024;
			int blocks = finalData.length / blockSize;
			for (int i = 0; i < blocks; i++) {
				this.out.write(finalData, blockSize * i, blockSize);
			}
			this.out.write(finalData, blocks * blockSize, finalData.length - blocks * blockSize);
		}
	}

	/**
	 * Same as {@link #write(String, byte[]...)} except that IOExceptions are
	 * silenced.
	 * 
	 * @param header
	 * @param data
	 */
	public void write(String header, byte[]... data) {
		try {
			writeWarn(header, data);
		} catch (IOException ignored) {
		}
	}

	/**
	 * Closes the outputstream and releases underlying resources.
	 * 
	 * @throws IOException if the underlying outputstream throws an exception
	 */
	public void close() throws IOException {
		out.close();
	}
}
