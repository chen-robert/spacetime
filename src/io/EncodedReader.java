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
	private final List<PacketListener> listeners = new LinkedList<>();

	public EncodedReader(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {
		boolean running = true;
		while (running) {
			try {
				if (in.available() > 7) {
					byte[] lenBuffer = new byte[4];
					in.read(lenBuffer);

					int headerLen = Util.toInt(lenBuffer);
					byte[] headerArr = new byte[headerLen];
					in.read(headerArr);
					String header = new String(headerArr);

					in.read(lenBuffer);
					int length = Util.toInt(lenBuffer);

					ByteArrayOutputStream out = new ByteArrayOutputStream(length);
					byte[] buffer = new byte[1024];

					while (out.size() < length) {
						if (buffer.length > length - out.size())
							buffer = new byte[length - out.size()];

						if (in.available() >= buffer.length) {
							out.write(buffer, 0, in.read(buffer));
						}

						Thread.sleep(10);
					}
					byte[] data = out.toByteArray();

					synchronized (listeners) {
						for (PacketListener pl : listeners)
							pl.packet(header, data);
					}
				} else {
					Thread.sleep(50);
				}
			} catch (IOException ie1) {
				try {
					in.close();
					running = false;
				} catch (IOException ignored) {
				}
			} catch (Exception e) {
				e.printStackTrace();
				resetStream();
			}
		}
	}

	private void resetStream() {
		try {
			while (in.available() > 0)
				in.skip(in.available());
		} catch (IOException ignored) {
		}
	}

	public void addListener(PacketListener pl) {
		synchronized (listeners) {
			listeners.add(pl);
		}
	}
}
