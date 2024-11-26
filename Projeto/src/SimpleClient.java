import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class SimpleClient {
	private BufferedReader in;
	private PrintWriter out;
	private BufferedReader console;
	private Socket socket;
	private String id;
	public static void main(String[] args) {
		new SimpleClient().runClient();
	}
	
	private File[] getFiles() {
        String path = "src/dl" + id;
        File dir = new File(path);
        return dir.listFiles(f -> true);
    }
	
	public void runClient() {
		try {
			connectToServer();
			id = console.readLine();
			Gui window = new Gui(getFiles(), id);
			window.setVisible(true);
			
			sendMessages();
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//... 
			}
		}
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, SimpleServer.PORTO);
		System.out.println("Socket:" + socket);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		console = new BufferedReader(new InputStreamReader(
				System.in));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	void sendMessages() throws IOException {
		String str = ""; 
		while(!str.equals("FIM")) {
			out.println(console.readLine() + " teste");
		}
		
	}

}
