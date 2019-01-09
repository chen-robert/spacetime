package networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import game.Bullet;
import io.Util;

public class ObjectSerializer {
	public static byte[] serialize(Object obj) {
		String className = obj.getClass().getName();

		Class<?> curr = obj.getClass();

		int fieldCount = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (!curr.equals(Object.class)) {
			if (curr.getAnnotation(Serialize.class) == null) continue;

			String[] fields = curr.getAnnotation(Serialize.class).fields();
			fieldCount += fields.length;

			for (String property : fields) {
				try {
					out.write(Util.toBytes(property.length()));
					out.write(property.getBytes());

					Field field = curr.getDeclaredField(property);
					field.setAccessible(true);
					Object value = field.get(obj);

					byte[] data = serializeField(value, field);

					out.write(Util.toBytes(data.length));
					out.write(data);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ReflectiveOperationException roe) {
					roe.printStackTrace();
				}
			}
			curr = curr.getSuperclass();
		}

		return Util.concat(Util.toBytes(className.length()), className.getBytes(), Util.toBytes(fieldCount),
				out.toByteArray());
	}

	private static byte[] serializeField(Object value, Field field) {
		byte[] data;
		switch (field.getType().getName()) {
		case "double":
			data = Util.toBytes((Double) value);
			break;
		case "java.lang.String":
			data = ((String) value).getBytes();
			break;
		default:
			System.err.println("Invalid field " + field.getClass().getName());
			data = null;
			break;
		}
		return data;
	}

	private static Object deserializeField(byte[] data, Field field) {
		Object ret;
		switch (field.getType().getName()) {
		case "double":
			ret = Util.toDouble(data);
			break;
		case "java.lang.String":
			ret = new String(data);
			break;
		default:
			System.err.println("Invalid field " + field.getClass().getName());
			ret = null;
			break;
		}
		return ret;

	}

	public static Object deserialize(byte[] bytes) {
		int offset = 0;
		int strLen = Util.toInt(bytes, offset);
		offset += 4;
		String name = new String(bytes, offset, strLen);
		offset += strLen;

		try {
			Object obj = Class.forName(name).newInstance();
			int fields = Util.toInt(bytes, offset);
			offset += 4;

			for (int i = 0; i < fields; i++) {
				int fieldLen = Util.toInt(bytes, offset);
				offset += 4;
				String fieldName = new String(bytes, offset, fieldLen);
				offset += fieldLen;

				int dataLen = Util.toInt(bytes, offset);
				offset += 4;
				byte[] data = Arrays.copyOfRange(bytes, offset, offset + dataLen);
				offset += dataLen;

				Class<?> curr = obj.getClass();
				while (!curr.equals(Object.class)) {
					try {
						Field field = curr.getDeclaredField(fieldName);
						field.setAccessible(true);
						field.set(obj, deserializeField(data, field));
						break;
					} catch (NoSuchFieldException nfe) {
						curr = curr.getSuperclass();
					}
				}
			}

			try {
				Method init = obj.getClass().getMethod("serializationInit");
				init.invoke(obj);
			} catch (ReflectiveOperationException rfe) {
				// Ignore if the object has no init method
			}

			return obj;
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String args[]) {
		byte[] b = serialize(new Bullet("asdf", "bcdf", 1, 1234, 23, 43));
		System.out.println(((Bullet) deserialize(b)).getRenderX());
	}
}
