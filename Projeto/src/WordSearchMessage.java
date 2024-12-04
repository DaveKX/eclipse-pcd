import java.io.Serializable;

public class WordSearchMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text;
	private String address;
	private String port;
	private int Id;
	
	public WordSearchMessage(int Id, String text, String address, String port) {
		this.Id = Id;
		this.text = text;
		this.address = address;
		this.port = port;
	}

	public int getId() {
		return Id;
	}

	public String getText() {
		return text;
	}

	public String getAddress() {
		return address;
	}

	public String getPort() {
		return port;
	}

}
