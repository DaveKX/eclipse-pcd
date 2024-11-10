public class NameThread extends Thread {
	private int id = 0;
	
	public NameThread(int id) {
		this.id = id;
	}
	
	public void run() {
		try {
			for (int i = 0; i < 10; i ++) {
				int sleepVal = (int) (Math.random() * 2 + 1) * 1000;
				System.out.println("ID: " + id + ", Sleep: " + sleepVal / 1000 + "s");
				sleep(sleepVal);
			}
		} catch (InterruptedException e) {}
	}

	public static void main(String args[]) throws InterruptedException {
		Thread t1 = new NameThread(1);
		Thread t2 = new NameThread(2);
 		t1.start();
 		t2.start();
		try {
			sleep(4000);
			
			t1.interrupt();
			t2.interrupt();
			
			t1.join();
			t2.join();
			
		} catch (InterruptedException e) {}
		System.out.println("Main done, all done!");
	}
}