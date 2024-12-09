import java.io.Serializable;
import java.net.InetAddress;

@SuppressWarnings("serial")
public class NewConnectionRequest implements Serializable {
	private InetAddress address;
	private String port;

	public NewConnectionRequest(InetAddress add, String port) {
		this.address = add;
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public InetAddress getAddress() {
		return address;
	}

}
