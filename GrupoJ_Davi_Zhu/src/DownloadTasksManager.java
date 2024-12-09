import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DownloadTasksManager {
	private ArrayList<FileBlockRequestMessage> blocks = new ArrayList<>();
	private ArrayList<NodeOutput> nodeList = new ArrayList<>();
	private String fileHash;
	private String fileName;
	private String directoryPath;
	private FileSearchResult f;
	private byte[] data;
	private ExecutorService threadPool;
	private long duration = 0;

	public DownloadTasksManager(FileSearchResult f, String directoryPath) {
		try {
			this.f = f;
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
	
	public long getDuration() {
		return duration;
	}

	public String result() {
		String s = "Descarga completa." + "\n";
		for (NodeOutput node : nodeList) {
			s += node + ":" + node.getBlocos();
		}
		return s;
	}

	public synchronized void saveBlock(byte[] blockData, int offset, int length) {
		if (offset < 0 || offset >= data.length) {
			throw new IllegalArgumentException("Offset inválido para o bloco recebido.");
		}

		System.arraycopy(blockData, 0, data, offset, length);
		System.out.println("Bloco salvo no offset: " + offset);
	}

	public void download() {
		long startTime = System.nanoTime();

		for (NodeOutput node : nodeList) {
			node.setBlocos(0);
			;
		}

		for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
			int currentBlock = blockIndex;
			threadPool.submit(() -> {
				FileBlockRequestMessage bloco = blocks.get(currentBlock);
				for (NodeOutput node : nodeList) {
					byte[] data = tryDownloadBlock(node, bloco);
					if (data != null) {
//						saveBlock(data, bloco.getOffset(), bloco.getLength());
						System.arraycopy(data, 0, data, bloco.getOffset(), bloco.getLength());
						node.setBlocos(node.getBlocos() + 1);
						break;
					}
				}
			});
		}

		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(20000, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
			}
		} catch (InterruptedException e) {
			threadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}

		writeToFile();
		long endTime = System.nanoTime();
		duration = (endTime - startTime) / 1_000_000;
		System.out.println("Download concluído em " + duration + " ms.");
	}

	private byte[] tryDownloadBlock(NodeOutput node, FileBlockRequestMessage bloco) {
		
		byte[] content = null;
		try {
			content = Files.readAllBytes(f.getFile().toPath());
			if (content != null) {
				System.arraycopy(content, bloco.getOffset(), data, 0, bloco.getLength());
			} else {
				System.err.println("Erro: não foi possível encontrar o conteúdo do arquivo no node.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private void writeToFile() {
		try {
			File directory = new File(directoryPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File outputFile = new File(directory, f.getFileName());
			Files.write(outputFile.toPath(), data);
			System.out.println("Arquivo completo baixado com sucesso.");
		} catch (IOException e) {
			System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
		}
	}
}