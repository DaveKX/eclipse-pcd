import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DownloadTasksManager {
	private ArrayList<FileBlockRequestMessage> blocks = new ArrayList<>();
	private ArrayList<NodeOutput> nodeList = new ArrayList<>();
	private String fileHash;
	private String fileName;
	private byte[] data;

	public DownloadTasksManager(FileSearchResult f) {
		try {
			this.fileName = f.getFileName();
			this.fileHash = f.getHash();
			this.data = new byte[f.getFileSize()];
			this.nodeList = new ArrayList<NodeOutput>(f.getNodeList());
			this.blocks = new ArrayList<FileBlockRequestMessage>(BlockManager.createBlocks(f.getFileSize(), fileHash));

		} catch (Exception e) {
			throw new RuntimeException("Erro ao inicializar DownloadTasksManager", e);
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
	
	public String result() {
		String s = "Descarga completa." + "\n";
		for(NodeOutput node : nodeList) {
			s += node + ":" + node.getBlocos();
		}
		return s;
	}
	
	public synchronized void saveBlock(byte[] blockData, int offset) {
        if (offset < 0 || offset >= data.length) {
            throw new IllegalArgumentException("Offset inválido para o bloco recebido.");
        }

        System.arraycopy(blockData, 0, data, offset, blockData.length);
        System.out.println("Bloco salvo no offset: " + offset);
    }
	
	public void saveToFile(String directoryPath) throws IOException {
        File outputFile = new File(directoryPath, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(data);
            fos.flush();
        }
        System.out.println("Arquivo salvo com sucesso: " + outputFile.getAbsolutePath());
    }

}
