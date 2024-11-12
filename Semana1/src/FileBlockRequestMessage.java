

public class FileBlockRequestMessage {
	public final String text;
	private final int initialPos;

	public FileBlockRequestMessage(String text, int initialPos) {
		super();
		this.text = text;
		this.initialPos = initialPos;
	}

	public int getInitialPos() {
		return initialPos;
	}

}
