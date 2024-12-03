import java.io.Serializable;

public class WordSearchMessage implements Serializable{
	private String text;
	public WordSearchMessage(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
