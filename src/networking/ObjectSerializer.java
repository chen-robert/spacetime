package networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import game.Bullet;
import io.Util;

public class ObjectSerializer {
	public static byte[] serialize(Object obj) {
		String className = obj.getClass().getName();

		String[] fields = obj.getClass().getAnnotation(Serialize.class).fields();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < fields.length; i++) {
			try {
				String property = fields[i];
				out.write(Util.toBytes(property.length()));
				out.write(property.getBytes());

				Field field = obj.getClass().getDeclaredField(property);
				field.setAccessible(true);
				Object value = field.get(obj);

				byte[] data;
				switch (value.getClass().getName()) {
				case "java.lang.Double":
					data = Util.toBytes((Double) value);
					break;
				case "java.lang.String":
					data = ((String) value).getBytes();
					break;
				default:
					System.err.println("Invalid field " + property);
					return null;
				}

				out.write(data.length);
				out.write(data);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ReflectiveOperationException roe) {
				roe.printStackTrace();
			}
		}

		// @formatter:off
		return Util.concat(
			Util.toBytes(className.length()), className.getBytes(),
			out.toByteArray()
		);
		// @formatter:on
	}

	public static Object deserialize(byte[] bytes) {
		int offset = 0;
		int strLen = Util.toInt(bytes, offset);
		offset += 4;
		String name = new String(bytes, offset, strLen);
		offset += strLen;

		try {
			Object obj = Class.forName(name).newInstance();
			return obj;
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String args[]) {
		byte[] b = serialize(new Bullet("asdf", "bcdf", 1, 1234, 23, 43));
		System.out.println(deserialize(b));
	}
}
