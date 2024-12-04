// Classe para gerenciar tarefas de descarregamento

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

//Classe para gerenciar tarefas de descarregamento
public class DownloadTasksManager {

	private final List<NodeOutput> remoteNodes; // Lista de nós remotos
	private final Map<Integer, byte[]> receivedBlocks; // Blocos recebidos
	private final int totalBlocks; // Número total de blocos a descarregar
	private final String fileHash; // Hash do arquivo a descarregar
	private final String outputFilePath; // Caminho do arquivo final
	private final Object lock = new Object(); // Sincronização

	public DownloadTasksManager(List<NodeOutput> remoteNodes, int totalBlocks, String fileHash, String outputFilePath) {
		this.remoteNodes = remoteNodes;
		this.receivedBlocks = new ConcurrentHashMap<>();
		this.totalBlocks = totalBlocks;
		this.fileHash = fileHash;
		this.outputFilePath = outputFilePath;
	}

	public void startDownload() {
		ExecutorService executor = Executors.newFixedThreadPool(remoteNodes.size());

		for (NodeOutput node : remoteNodes) {
			executor.submit(() -> downloadBlocksFromNode(node));
		}

		executor.shutdown();
		try {
			if (executor.awaitTermination(1, TimeUnit.HOURS)) {
				writeFileToDisk();
				displayCompletionWindow();
			} else {
				System.err.println("Tempo de descarregamento excedido.");
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadBlocksFromNode(NodeOutput node) {
		try {
			ObjectOutputStream out = node.getOut();
			ObjectInputStream in = node.getIn();

			for (int blockIndex = 0; blockIndex < totalBlocks; blockIndex++) {
				if (receivedBlocks.containsKey(blockIndex))
					continue;

				// Enviando o pedido de bloco com o hash do arquivo
				FileBlockRequestMessage request = new FileBlockRequestMessage(fileHash, blockIndex,
						getBlockSize(blockIndex));
				out.writeObject(request);

				FileBlockAnswerMessage blockMessage = (FileBlockAnswerMessage) in.readObject();

				synchronized (lock) {
					receivedBlocks.put(blockMessage.getBlockIndex(), blockMessage.getData());
					System.out.println("Bloco " + blockMessage.getBlockIndex() + " recebido do nó " + node.getPort());
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Erro ao descarregar blocos do nó: " + node.getPort());
			e.printStackTrace();
		}
	}

	private int getBlockSize(int blockIndex) {
		// Retorna o tamanho de um bloco (último bloco pode ter tamanho menor)
		return blockIndex == totalBlocks - 1 ? getLastBlockSize() : DEFAULT_BLOCK_SIZE;
	}

	private int getLastBlockSize() {
		// Calcula o tamanho do último bloco
		return (int) (new File(outputFilePath).length() % DEFAULT_BLOCK_SIZE);
	}

	private void writeFileToDisk() throws IOException {
		try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
			for (int i = 0; i < totalBlocks; i++) {
				byte[] blockData = receivedBlocks.get(i);
				if (blockData != null) {
					fos.write(blockData);
				} else {
					System.err.println("Bloco faltando: " + i);
				}
			}
		}
		System.out.println("Arquivo final escrito em: " + outputFilePath);
	}

	private void displayCompletionWindow() {
		JFrame frame = new JFrame("Descarregamento");
		frame.setSize(300, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel label = new JLabel("Descarregamento completo", SwingConstants.CENTER);
		frame.add(label);

		frame.setVisible(true);
	}
}
