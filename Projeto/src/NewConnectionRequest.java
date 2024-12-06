import java.io.Serializable;

@SuppressWarnings("serial")
public class NewConnectionRequest implements Serializable {
	private String address;
	private String port;

	public NewConnectionRequest(String add, String port) {
		this.address = add;
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public String getAddress() {
		return address;
	}

}
