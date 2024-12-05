import java.io.Serializable;

public class WordSearchMessage implements Serializable {
	private String text;
	private String address;
	private String port;

	public WordSearchMessage(String text, String address, String port) {
		this.text = text;
		this.address = address;
		this.port = port;
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
