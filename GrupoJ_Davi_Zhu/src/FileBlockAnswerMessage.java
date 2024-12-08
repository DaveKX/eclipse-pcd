import java.io.Serializable;

public class FileBlockAnswerMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String fileHash;
    private final int offset;
    private final byte[] data;

    public FileBlockAnswerMessage(String fileHash, int offset, byte[] data) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.data = data;
    }

    public String getFileHash() {
        return fileHash;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getData() {
        return data;
    }
}
