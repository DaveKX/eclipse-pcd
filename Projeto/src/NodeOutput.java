import java.io.ObjectOutputStream;

public class NodeOutput {
	private ObjectOutputStream out;
	private String port;
	public NodeOutput(ObjectOutputStream out, String port) {
		this.out = out;
		this.port = port;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public String getPort() {
		return port;
	}
	
	
}
