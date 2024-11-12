import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockManager {
	private static final int BLOCK_SIZE = 10240; // tamanho fixo do bloco

	public static List<FileBlockRequestMessage> createBlocks(File file) {
		List<FileBlockRequestMessage> blocks = new ArrayList<>();
		String fileHash = FileUtils.calculateFileHash(file);

		if (fileHash == null) {
			System.err.println("Não foi possível calcular o hash do arquivo: " + file.getName());
			return blocks;
		}

		long fileSize = file.length();
		for (long offset = 0; offset < fileSize; offset += BLOCK_SIZE) {
			int length = (int) Math.min(BLOCK_SIZE, fileSize - offset);
			blocks.add(new FileBlockRequestMessage(fileHash, offset, length));
		}
		return blocks;
	}
}
