import java.io.File;
import java.util.ArrayList;

public class DownloadTasksManager {
	private ArrayList<FileBlockRequestMessage> blocks = new ArrayList<FileBlockRequestMessage>();
	private String fileHash;

	public DownloadTasksManager(File f) {
		try {
			fileHash = FileUtils.calculateFileHash(f);
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
		return fileHash;
	}
}
