package networking;

@Serialize(fields = { "ID" })
public abstract class SerializableObject implements Serializable {
	final String ID = Serializable.generateId();
	final long INIT_TIME = System.currentTimeMillis();

	public long getAge() {
		return System.currentTimeMillis() - INIT_TIME;
	}

	@Override
	public String getId() {
		return ID;
	}

}
