
public enum Message {
	PLAYER(0),
	CHARACTERS(1),
	ID(2),
	NAMECOLOR(3),
	BULLETS(4);
	
	private final int d_value;
	
	Message(int value) {
		d_value = value;
	}
	
	public int value() {
		return d_value;
	}
}
