import java.io.Serializable;

public class NewConnectionRequest implements Serializable{
	private int id;
	private String text;
	public NewConnectionRequest(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public int getId() {
		return id;
	}

}
