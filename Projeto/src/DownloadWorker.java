public class DownloadWorker extends Thread {
	private final DownloadTasksManager manager;

	public DownloadWorker(DownloadTasksManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		while (!manager.isFinished()) {
			FileBlockRequestMessage block = manager.getNextTask();
			if (block != null) {
				// Simula o processamento do bloco
				System.out.println("Thread " + this.getName() + " descarregando bloco: " + block);
				try {
					Thread.sleep(1000); // Simula o tempo de descarregamento
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		synchronized (manager) {
			if (Thread.activeCount() <= 2) { // Apenas a thread principal e a última worker
				manager.finish();
				System.out.println("Todos os blocos foram processados. Gerenciador finalizado.");
			}
		}

		System.out.println("Thread " + this.getName() + " finalizada.");
	}
}
