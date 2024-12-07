import java.io.ObjectOutputStream;
import java.net.Socket;

public class NodeOutput {
	private ObjectOutputStream out;
	private String port;
	private Socket socket;
	private int blocos;

	public NodeOutput(ObjectOutputStream out, String port, Socket socket) {
		this.out = out;
		this.port = port;
		this.socket = socket;
	}
	
	public int getBlocos() {
		return blocos;
	}
	
	public void setBlocos(int blocos) {
		this.blocos = blocos;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public String getPort() {
		return port;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	@Override
	public String toString() {
		return "NodeAddress [" + socket.getInetAddress() + ", " + port + "]";
	}

}
