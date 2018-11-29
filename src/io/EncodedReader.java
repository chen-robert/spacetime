package io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Robert on 8/6/2017.
 */
public class EncodedReader implements Runnable {
	private InputStream in;
	private volatile boolean running = true;
	private final List<PacketListener> listeners = new LinkedList<>();

	public EncodedReader(InputStream in) {
		this.in = in;
	}

	/**
	 * Reads in the next integer from {@link #in}.
	 * 
	 * @return the next integer
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		byte[] lenBuffer = new byte[4];
		in.read(lenBuffer);

		return Util.toInt(lenBuffer);
	}

	/**
	 * Reads in the next string with certain length from {@link #in}.
	 * 
	 * @param len length of string
	 * @return string read
	 * @throws IOException
	 */
	public String readStr(int len) throws IOException {
		byte[] buffer = new byte[len];
		in.read(buffer);

		return new String(buffer);
	}

	@Override
	public void run() {
		while (running) {
			try {
				if (in.available() > 7) {

					int headerLen = readInt();
					String header = readStr(headerLen);
					int length = readInt();

					ByteArrayOutputStream out = new ByteArrayOutputStream(length);
					byte[] buffer = new byte[1024];

					while (out.size() < length) {
						if (buffer.length > length - out.size())
							buffer = new byte[length - out.size()];

						if (in.available() >= buffer.length) {
							out.write(buffer, 0, in.read(buffer));
						}
					}
					byte[] data = out.toByteArray();

					synchronized (listeners) {
						for (PacketListener pl : listeners)
							pl.packet(header, data);
					}
				} else {
					Thread.sleep(10);
				}
			} catch (IOException ie1) {
				// Socket disconnected
				running = false;
				ie1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add a listener to receive updates.
	 * 
	 * @param pl listener to add
	 */
	public void addListener(PacketListener pl) {
		synchronized (listeners) {
			listeners.add(pl);
		}
	}

	/**
	 * Removes a listener.
	 * 
	 * @param pl listener to remove
	 */
	public void removeListener(PacketListener pl) {
		synchronized (listeners) {
			listeners.remove(pl);
		}
	}

	/**
	 * Closes the reader and releases all resources.
	 * 
	 * @throws IOException if the underlying input stream throws an exception
	 */
	public void close() throws IOException {
		running = false;
		in.close();
	}

}
