import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class NodeOutput implements Serializable {
//	private ObjectOutputStream out;
//	private ObjectInputStream in;
	private String port;
	private InetAddress address;
//	private Socket socket;
	private int blocos;

//	public NodeOutput(ObjectInputStream in, ObjectOutputStream out, String port, Socket socket) {
//		this.out = out;
//		this.port = port;
//		this.socket = socket;
//		this.address = socket.getInetAddress().toString();
//	}
	
	public NodeOutput(String port, InetAddress address) {
		this.port = port;
		this.address = address;
	}
	
	public int getBlocos() {
		return blocos;
	}
	
	public void setBlocos(int blocos) {
		this.blocos = blocos;
	}

//	public ObjectOutputStream getOut() {
//		return out;
//	}
//	
//	public ObjectInputStream getIn() {
//		return in;
//	}

	public String getPort() {
		return port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
//	public Socket getSocket() {
//		return socket;
//	}
	
	@Override
	public String toString() {
		return "NodeAddress [" + address + ", " + port + "]";
	}

}
