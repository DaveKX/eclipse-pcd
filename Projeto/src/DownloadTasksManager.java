import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;

public class DownloadTasksManager {
	private ArrayList<FileBlockRequestMessage> blocks = new ArrayList<FileBlockRequestMessage>();
	private MessageDigest fileHash;

	public DownloadTasksManager(File f) {
		try {
			fileHash = MessageDigest.getInstance("SHA-256");
			byte[] data = Files.readAllBytes(f.toPath());
			fileHash.update(data);
			BlockManager.createBlocks((int) f.length(), fileHash);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<FileBlockRequestMessage> getBlocks() {
		return blocks;
	}

	public int getNumBlocks() {
		return blocks.size();
	}

	public String getHash() {
		byte[] hashBytes = fileHash.digest();
		String result = new String();

		for (byte b : hashBytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1)
				result = result + "0";
			result = result + hex;
		}

		return result;
	}
}
