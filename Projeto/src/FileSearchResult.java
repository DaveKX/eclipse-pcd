import java.io.File;
import java.io.Serializable;

@SuppressWarnings("serial")
public class FileSearchResult implements Serializable {
	private WordSearchMessage procura;
	private String hash;
	private int fileSize;
	private String fileName;
	private String address;
	private String port;
	private File f;

	public FileSearchResult(WordSearchMessage procura, String hash, int fileSize, String fileName, String address,
			String port, File f) {
		this.procura = procura;
		this.hash = hash;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.address = address;
		this.port = port;
		this.f = f;
	}

	public WordSearchMessage getProcura() {
		return procura;
	}
	
	public File getFile() {
		return f;
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
	public String toString() {
		return "FileSearchResult{" + "fileName='" + fileName + '\'' + ", fileSize=" + fileSize + ", fileHash='" + hash
				+ '\'' + ", nodeAddress='" + address + '\'' + ", nodePort=" + port + '}';
	}
}
