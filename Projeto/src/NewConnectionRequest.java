import java.io.Serializable;

public class NewConnectionRequest implements Serializable {
	private static final long serialVersionUID = 1L;
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
