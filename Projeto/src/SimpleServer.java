import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer {
	public static class DealWithClient extends Thread{
		private BufferedReader in;

		private PrintWriter out;
		
		DealWithClient(Socket socket) throws IOException {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}
		
		public void run() {
			while (true) {
				String str = "início";
				try {
					str = in.readLine();
					if (str == null || str.equals("FIM") )
						break;
					System.out.println("Eco:" + str);
					out.println(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}}
			}
		}

	public static final int PORTO = 8080;
	public static void main(String[] args) {
		try {
			new SimpleServer().startServing();
		} catch (IOException e) {}}

	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(PORTO);
			try {//Conexao aceite
				while(true) {
					Socket socket = ss.accept();
					new DealWithClient(socket).start();
				}
			} finally {//a fechar
				ss.close();
			}
	}
}
