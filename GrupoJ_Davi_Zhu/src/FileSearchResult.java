import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("serial")
public class FileSearchResult implements Serializable {
	private WordSearchMessage procura;
	private String hash;
	private int fileSize;
	private String fileName;
	private String address;
	private String port;
	private File f;
	private NodeOutput node;
	private ArrayList<NodeOutput> nodeList = new ArrayList<>();

	public FileSearchResult(WordSearchMessage procura, String hash, int fileSize, String fileName, String address,
			String port, File f, NodeOutput node) {
		this.procura = procura;
		this.hash = hash;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.address = address;
		this.port = port;
		this.f = f;
		this.node = node;
	}
	
	public ArrayList<NodeOutput> getNodeList() {
		return nodeList;
	}
	
	public void setNodeList(ArrayList<NodeOutput> nodeList) {
		this.nodeList = nodeList;
	}

	public WordSearchMessage getProcura() {
		return procura;
	}
	
	public File getFile() {
		return f;
	}
	
	public NodeOutput getNode() {
		return node;
	}

	public String getHash() {
		return hash;
	}

	public int getFileSize() {
		return fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public String getAddress() {
		return address;
	}

	public String getPort() {
		return port;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    FileSearchResult that = (FileSearchResult) o;
	    return getFileName().equals(that.getFileName());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(getFileName());
	}


	@Override
	public String toString() {
		return "FileSearchResult{" + "fileName='" + fileName + '\'' + ", fileSize=" + fileSize + ", fileHash='" + hash
				+ '\'' + ", nodeAddress='" + address + '\'' + ", nodePort=" + port + '}';
	}
}
