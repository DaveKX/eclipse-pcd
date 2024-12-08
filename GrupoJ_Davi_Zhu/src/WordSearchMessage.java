import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class WordSearchMessage implements Serializable {
	private final String searchId;
	private String text;
	private String address;
	private String port;

	public WordSearchMessage(String text, String address, String port) {
		this.searchId = UUID.randomUUID().toString();
		this.text = text;
		this.address = address;
		this.port = port;
	}

	public String getSearchId() {
        return searchId;
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
