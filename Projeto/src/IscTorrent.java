import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class IscTorrent {
	private ArrayList<NodeOutput> outList = new ArrayList<NodeOutput>();
	private DownloadTasksManager dtm;
	private ObjectInputStream in;
	private Socket socket;
	private static String id;
	private File[] files;
	private String address;
	private static String port;
	private List<FileSearchResult> fileList;
	private DefaultListModel<String> listModel;
	private int IdMsg = 0;
	private final Set<Integer> processedMessages = new HashSet<>();

	public IscTorrent(String id) throws UnknownHostException {
		IscTorrent.id = id;
		this.address = InetAddress.getByName(null).toString();
	}

	public static void main(String[] args) {
		try {
			port = args[0];
			IscTorrent server = new IscTorrent(args[1]);
			System.out.println("sou o servidor " + id);
			Gui window = server.new Gui(args[1]);
			window.setVisible(true);
			server.startServing(Integer.parseInt(port));

		} catch (IOException e) {
		}
	}

	public class DealWithClient extends Thread {
		private ObjectInputStream ins;
		private ObjectOutputStream outs;

		DealWithClient(Socket socket) throws IOException {
			outs = new ObjectOutputStream(socket.getOutputStream());
			ins = new ObjectInputStream(socket.getInputStream());
		}

		public void run() {
			while (true) {
				System.out.println("reading object");
				Object msg;
				try {
					msg = ins.readObject();
					System.out.println(msg.getClass());
					if (msg instanceof NewConnectionRequest) {
						System.out.println("Eco: Conectei-me ao servidor " + ((NewConnectionRequest) msg).getPort());
						connectToServer(Integer.parseInt(((NewConnectionRequest) msg).getPort()));
					} else if (msg instanceof WordSearchMessage) {
						responseWSM((WordSearchMessage) msg);
					} else if (msg instanceof ListFileSearch) {
						System.out.println("recebi files");
						fileList = ((ListFileSearch) msg).getFileList();
						if (fileList != null) {
						    for (FileSearchResult f : fileList) {
						        listModel.addElement(f.getFileName());
						    }
						}
					} else if (msg instanceof FileBlockRequestMessage) {
						sendBlock(((FileBlockRequestMessage) msg).getFileHash());
					}

				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		private synchronized void sendBlock(String hash) throws FileNotFoundException {
			BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
			FileOutputStream file = new FileOutputStream("temp");
			String line;
			for (NodeOutput out : outList) {
//				while((line = reader.readLine()) != null) {
//					out.getOut().writeObject(new FileBlockAnswerMessage());
//				}
			}
		}

		public synchronized void responseWSM(WordSearchMessage msg) throws IOException {
			 if (processedMessages.contains(msg.getId())) {
			        System.out.println("Mensagem já processada: " + msg.getId());
			        return;
			    }
			    processedMessages.add(msg.getId());
			System.out.println("Eco: Recebi pedido de procura - " + ((WordSearchMessage) msg).getText());
			String procura = ((WordSearchMessage) msg).getText();
			System.out.println(procura);
			List<FileSearchResult> fileList = new ArrayList<FileSearchResult>();
			for (File f : files) {
				if (f.getName().indexOf(procura) != -1) {
					String hash = FileUtils.calculateFileHash(f);
					FileSearchResult fsr = new FileSearchResult((WordSearchMessage) msg, hash, (int) f.length(),
							f.getName(), address, port);
					fileList.add(fsr);
				}
			}
			ListFileSearch listAnswer = new ListFileSearch(fileList);

			System.out.println("Enviando objeto: " + listAnswer);
			for (NodeOutput out : outList) {
			    try {
			        out.getOut().writeObject(listAnswer);
			        System.out.println("Objeto enviado com sucesso para o nó: " + out.getPort());
			    } catch (IOException e) {
			        System.err.println("Erro ao enviar para o nó: " + out.getPort());
			        e.printStackTrace();
			    }
			}

			
			for (NodeOutput out : outList) {
		        if (!out.getPort().equals(msg.getPort())) { // Não envie para o remetente
		            out.getOut().writeObject(listAnswer);
		        }
		    }
			
			System.out.println("Mandei resposta");

		}
	}

	void connectToServer(int port) throws IOException {
		if (socket != null && !socket.isClosed()) {
		    System.out.println("Já conectado ao servidor!");
		    return;
		}
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, port);
		System.out.println("Socket:" + socket);
		outList.add(new NodeOutput(new ObjectOutputStream(socket.getOutputStream()), Integer.toString(port)));
		in = new ObjectInputStream(socket.getInputStream());
	}

	public void startServing(int port) throws IOException {
		ServerSocket ss = new ServerSocket(port);
		try {// Conexao aceite
			while (true) {
				Socket socket = ss.accept();
		        System.out.println("Nova conexão aceita: " + socket);

		        // Feche conexões duplicadas
		        for (NodeOutput node : outList) {
		            if (Integer.parseInt(node.getPort()) == socket.getPort()) {
		                System.out.println("Conexão duplicada detectada, fechando...");
		                socket.close();
		                break;
		            }
		        }

		        new DealWithClient(socket).start();
			}
		} finally {// a fechar
			ss.close();
		}
	}

	public class Gui extends JFrame {
		private static final long serialVersionUID = 1L;
		private JTextField searchField;
		private JButton searchButton;
		private JButton downloadButton;
		private JButton connectButton;
		private JList<String> resultList;

		public Gui(String title) {
			setTitle("Cliente " + title);
			setSize(800, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
			setResizable(false);
			setLocationRelativeTo(null);

			files = getFiles();

			// painel superior
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new GridLayout(1, 3, 0, 0));
			JLabel searchLabel = new JLabel("Texto a procurar: ");
			searchField = new JTextField(20);
			searchButton = new JButton("Procurar");

			topPanel.add(searchLabel);
			topPanel.add(searchField);
			topPanel.add(searchButton);

			// painel esquerdo
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			listModel = new DefaultListModel<>();
			resultList = new JList<>(listModel);
			leftPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);

			// painel direito
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new GridLayout(2, 1, 0, 0));
			downloadButton = new JButton("Descarregar");
			connectButton = new JButton("Ligar a Nó");

			rightPanel.add(downloadButton);
			rightPanel.add(connectButton);

			// JFrame
			add(topPanel, BorderLayout.NORTH);
			add(leftPanel, BorderLayout.CENTER);
			add(rightPanel, BorderLayout.EAST);

			// botao 'procurar'
			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = searchField.getText();
					listModel.clear();
					try {
						sendMessage(searchText);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});

			// botao 'ligar a no'
			connectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Criar a nova janela (JDialog)
					JDialog dialog = new JDialog(Gui.this, "Ligar a Nó", true);
					dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					dialog.setResizable(false);

					// Campos JDialog
					JLabel addressLabel = new JLabel("Endereço: ");
					JTextField addressField = new JTextField("localhost");
					JLabel portLabel = new JLabel("Porta: ");
					JTextField portField = new JTextField("8081");
					JButton cancelButton = new JButton("Cancelar");
					JButton okButton = new JButton("OK");

					addressField.setColumns(15);

					portField.setColumns(10);

					dialog.add(addressLabel);
					dialog.add(addressField);
					dialog.add(portLabel);
					dialog.add(portField);
					dialog.add(cancelButton);
					dialog.add(okButton);

					// tratamento do campo da porta
					portField.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							char c = e.getKeyChar();
							if (!Character.isDigit(c)) {
								e.consume();
							}
						}
					});

					// cancelar
					cancelButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});

					// ok
					okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String addressGui = addressField.getText();
							String portGui = portField.getText();
							System.out.println("Endereço: " + address);
							System.out.println("Porta: " + port);
							try {
								connectToServer(Integer.parseInt(portGui));
								NewConnectionRequest ncr = new NewConnectionRequest(address, port);
								outList.get(outList.size() - 1).getOut().writeObject(ncr);
							} catch (NumberFormatException | IOException e1) {
								e1.printStackTrace();
							}
							dialog.dispose();
						}
					});

					dialog.setSize(600, 80);
					dialog.setLocationRelativeTo(Gui.this);
					dialog.setVisible(true);
				}
			});

			downloadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String selectedFile = resultList.getSelectedValue();
					if (selectedFile == null) {
						JOptionPane.showMessageDialog(Gui.this, "Por favor, selecione um arquivo para descarregar.");
						return;
					}
					ArrayList<FileBlockRequestMessage> blocks = null;
					ArrayList<String> users = new ArrayList<>();
					// alterar pra pegar só o arquivo selecionado na gui
					for (FileSearchResult f : fileList) {
						users.add(f.getPort());
						blocks = BlockManager.getBlocks(f.getFileSize(), f.getHash());
					}

					for (NodeOutput out : outList) {
						if (users.contains(out.getPort())) {
							try {
								System.out.println(blocks.get(blocks.size()-1));
								out.getOut().writeObject(blocks.get(0));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			});
		}

		public synchronized void sendMessage(String text) throws InterruptedException {
			WordSearchMessage procura = new WordSearchMessage(IdMsg++, text, address, port);
			try {
				for (NodeOutput out : outList)
					out.getOut().writeObject(procura);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private static File[] getFiles() {
		String path = id;
		File dir = new File(path);
		return dir.listFiles(f -> true);
	}
}
