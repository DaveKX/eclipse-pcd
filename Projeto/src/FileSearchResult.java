import java.io.Serializable;

public class FileSearchResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private WordSearchMessage procura;
	private String hash;
	private int fileSize;
	private String fileName;
	private String address;
	private String port;

	public FileSearchResult(WordSearchMessage procura, String hash, int fileSize, String fileName, String address,
			String port) {
		this.procura = procura;
		this.hash = hash;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.address = address;
		this.port = port;
	}

	public WordSearchMessage getProcura() {
		return procura;
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

}
