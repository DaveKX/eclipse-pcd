import java.util.LinkedList;
import java.util.Queue;

public class DownloadTasksManager {
	private final Queue<FileBlockRequestMessage> taskQueue;
	private boolean isFinished;

	public DownloadTasksManager() {
		this.taskQueue = new LinkedList<>();
		this.isFinished = false;
	}

	// Adiciona um novo bloco à fila de tarefas
	public synchronized void addTask(FileBlockRequestMessage block) {
		taskQueue.offer(block);
		notifyAll(); // Notifica as threads aguardando
	}

	// Obtém o próximo bloco para descarregamento
	public synchronized FileBlockRequestMessage getNextTask() {
		while (taskQueue.isEmpty() && !isFinished) {
			try {
				wait(); // Aguarda até que uma nova tarefa seja adicionada ou o processo termine
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return null;
			}
		}
		return taskQueue.poll();
	}

	// Marca o processo como concluído
	public synchronized void finish() {
		isFinished = true;
		notifyAll(); // Notifica as threads aguardando
	}

	// Verifica se o gerenciador terminou
	public synchronized boolean isFinished() {
		return isFinished && taskQueue.isEmpty();
	}
}
