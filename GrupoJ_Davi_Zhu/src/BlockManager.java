import java.util.ArrayList;

public class BlockManager {
	private final static int blockSize = 10240; // tamanho fixo do bloco

	public static ArrayList<FileBlockRequestMessage> createBlocks(int fileSize, String hash) {
		ArrayList<FileBlockRequestMessage> blocks = new ArrayList<FileBlockRequestMessage>();
		for (int i = 0; i < fileSize; i += blockSize) {
			blocks.add(new FileBlockRequestMessage(hash, i, (int) Math.min(blockSize, fileSize - i)));
		}
		return blocks;
	}
}
