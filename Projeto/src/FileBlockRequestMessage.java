public class FileBlockRequestMessage {
	private String fileHash;
	private int offset;
	private int length;

	public FileBlockRequestMessage(String fileHash, int offset, int length) {
		this.fileHash = fileHash;
		this.offset = offset;
		this.length = length;
	}

	public String getFileHash() {
		return fileHash;
	}

	public long getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	@Override
	public String toString() {
		return "FileBlockRequestMessage{" + "fileHash='" + fileHash + '\'' + ", offset=" + offset + ", length=" + length
				+ '}';
	}
}
