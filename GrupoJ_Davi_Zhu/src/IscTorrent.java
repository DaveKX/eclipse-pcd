import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
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
	@SuppressWarnings("unused")
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private static String id;
	private File[] files;
	private InetAddress address;
	private static String port;
	private NodeOutput noAtual;
	private List<FileSearchResult> fileList = new ArrayList<>();
	private DefaultListModel<String> listModel;
//	private final Set<String> processedSearchIds = Collections.synchronizedSet(new HashSet<>());
	private Set<FileSearchResult> searchResults = new HashSet<>();

	public IscTorrent(String id) throws UnknownHostException {
		IscTorrent.id = id;
		this.address = InetAddress.getByName(null);
		this.noAtual = new NodeOutput(port, address);
	}

	public static void main(String[] args) {
		try {
			port = args[0];
			IscTorrent server = new IscTorrent(args[1]);
			System.out.println("Sou o servidor: " + id);
			Gui window = server.new Gui(args[1]);
			window.setVisible(true);
			server.startServing();

		} catch (IOException e) {
		}
	}

	public class DealWithClient extends Thread {
		private ObjectInputStream ins;
		@SuppressWarnings("unused")
		private ObjectOutputStream outs;

		DealWithClient(Socket socket) throws IOException {
			outs = new ObjectOutputStream(socket.getOutputStream());
			ins = new ObjectInputStream(socket.getInputStream());
		}

		public void run() {
			while (true) {
				System.out.println("Reading object...");
				Object msg;
				try {
					msg = ins.readObject();
					System.out.println("Classe da mensagem recebida: " + msg.getClass().getName());
					if (msg instanceof NewConnectionRequest) {
//						connectToServer(Integer.parseInt(((NewConnectionRequest) msg).getPort()));
						NewConnectionRequest ncr = (NewConnectionRequest) msg;
						InetAddress address = ncr.getAddress();
						String port = ncr.getPort();		        		   
		        		handleNewNodeRequest(address, port);
						System.out.println("Conectei-me ao servidor " + ((NewConnectionRequest) msg).getPort());
					} else if (msg instanceof WordSearchMessage) {
						responseWSM((WordSearchMessage) msg);
						for (FileSearchResult f : fileList) {
							outs.writeObject(f);
						}
						outs.writeObject("fim");
					}
//					else if (msg instanceof FileSearchResult) {
//						listModel.clear();
//						handleSearchResult((FileSearchResult) msg);
//						for(FileSearchResult f : searchResults) {
//							listModel.addElement(f.getFileName() + "<" + f.getNodeList().size() + ">");
//						}
//						searchResults.clear();
//					}
//					} else if (msg instanceof ListFileSearch) {
//						if (!((ListFileSearch) msg).getOriginPort().equals(port)) {
//							System.out.println("Mensagem recebida, mas este nó não é o originador. Não atualizando GUI.");
//						} else {
//							System.out.println("Arquivos recebidos. Atualizando GUI.");
//							listModel.clear();
//							searchResults.clear();
//							searchResults = ((ListFileSearch) msg).getFileList();
//							for(FileSearchResult f : searchResults) {
//								listModel.addElement(f.getFileName() + "<" + f.getNodeList().size() + ">");
//							}
//						}
//					}

				} catch (ClassNotFoundException | IOException e) {
					System.err.println(e.getMessage());
					System.exit(1); // fechar tudo se um nó for fechado
				} 
//				finally {
//					try {
//						if (socket != null && !socket.isClosed()) {
//							socket.close();
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}

		}
		
		private void handleNewNodeRequest(InetAddress address, String port) {
			NodeOutput no = new NodeOutput(port, address);
        	if(!outList.contains(no)){
        		outList.add(no);
        	}	                	
		}

//		public synchronized void handleSearchResult(FileSearchResult f) throws IOException {
//			String porta = f.getPort();
//			boolean exists = false;
//			for (FileSearchResult item : searchResults) {
//				if (item.getFileName().equals(f.getFileName())) {
//					exists = true;
//					ArrayList<NodeOutput> listaNos = item.getNodeList();
//					for (NodeOutput node : outList) {
//						if (node.getPort().equals(item.getPort())) {
//							listaNos.add(node);
//						}
//					}
//					item.setNodeList(listaNos);
//				}
//			}
//			if (!exists) {
//				ArrayList<NodeOutput> aux = new ArrayList<>();
//				for (NodeOutput node : outList) {
//					if (node.getPort().equals(f.getPort())) {
//						aux.add(node);
//					}
//				}
//				f.setNodeList(aux);
//				searchResults.add(f);
//			}
//			ListFileSearch listAnswer = new ListFileSearch(searchResults, porta);
//			for (NodeOutput out : outList) {
//				out.getOut().writeObject(listAnswer);
//			}
//		}

		public synchronized void responseWSM(WordSearchMessage msg) throws IOException {
			System.out.println("Recebi pedido de procura: '" + ((WordSearchMessage) msg).getText() + "', do nó: "
					+ ((WordSearchMessage) msg).getPort());
			String procura = ((WordSearchMessage) msg).getText().toLowerCase();

			// Verificar se já processei este searchId
//			if (processedSearchIds.contains(msg.getSearchId())) {
//				return; // Ignorar pesquisa duplicada
//			}
//			processedSearchIds.add(msg.getSearchId());

//			List<FileSearchResult> fileList = new ArrayList<>();
//			ArrayList<NodeOutput> availableNodes = new ArrayList<>();
			for (File f : files) {
				if (f.getName().toLowerCase().contains(procura)) {
					String hash = FileUtils.calculateFileHash(f);
					FileSearchResult fsr = new FileSearchResult((WordSearchMessage) msg, hash, (int) f.length(),
							f.getName(), address.toString(), port, f, noAtual);
//					for (NodeOutput node : outList) {
//						if (node.getPort().equals(port)) {
//							availableNodes.add(node);
//						}
//					}
//					fsr.setNodeList(availableNodes);
					fileList.add(fsr);
				}
			}
			if (!fileList.isEmpty()) {
//				for (NodeOutput out : outList) {
//					for(FileSearchResult f : fileList) {
//						out.getOut().writeObject(f);
//					}
//				}
//				ListFileSearch listAnswer = new ListFileSearch(searchResults, ((WordSearchMessage) msg).getPort());
//				for (NodeOutput out : outList) {
//					out.getOut().writeObject(listAnswer);
//				}
				System.out.println("Enviei resposta.");
			} else {
				System.out.println("Não foram encontrados arquivos correspondentes.");
			}

		}
	}

	void connectToServer(int port) throws IOException {
//		InetAddress endereco = InetAddress.getByName(null);
//		System.out.println("Endereco:" + endereco);
//		try {
//			socket = new Socket(endereco, port);
//			System.out.println("Socket:" + socket);
//			out = new ObjectOutputStream(socket.getOutputStream());
//			in = new ObjectInputStream(socket.getInputStream());
//			outList.add(new NodeOutput(Integer.toString(port), socket));
//		} catch (ConnectException e) {
//			JOptionPane.showMessageDialog(null, "O nó que tentou conectar não está ativo\n" + e.getMessage());
//		}

	}

	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(Integer.parseInt(port));
		try { // Conexao aceite
			while (true) {
				Socket socket = ss.accept();
				new DealWithClient(socket).start();
			}
		} finally { // a fechar
			ss.close();
		}
	}

//	public class DownloadThread extends Thread {
//		private final NodeOutput nodeOutput;
//		private final ArrayList<FileBlockRequestMessage> blocks;
//		private final DownloadTasksManager manager;
//
//		public DownloadThread(NodeOutput nodeOutput, ArrayList<FileBlockRequestMessage> blocks,
//				DownloadTasksManager manager) {
//			this.nodeOutput = nodeOutput;
//			this.blocks = blocks;
//			this.manager = manager;
//		}
//
//		@Override
//		public void run() {
//			try {
//				for (FileBlockRequestMessage block : blocks) {
//					synchronized (manager) {
//						// Solicitar bloco
//						nodeOutput.getOut().writeObject(block);
//						System.out.println("Pedido bloco enviado: " + block.getOffset());
//
//						// Esperar resposta
//						ObjectInputStream in = new ObjectInputStream(nodeOutput.getSocket().getInputStream());
//						FileBlockAnswerMessage answer = (FileBlockAnswerMessage) in.readObject();
//
//						// Salvar bloco recebido no gerenciador
//						manager.saveBlock(answer.getData(), block.getOffset());
//					}
//				}
//			} catch (IOException | ClassNotFoundException e) {
//				System.err.println("Erro no download do bloco: " + e.getMessage());
//			}
//		}
//	}

	@SuppressWarnings("serial")
	public class Gui extends JFrame {
		private JTextField searchField;
		private JButton searchButton;
		private JButton downloadButton;
		private JButton connectButton;
		private JList<String> resultList;

		private DownloadTasksManager downloadManager;

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
					System.out.println("Limpeza de searchResults. Tamanho antes: " + searchResults.size());
					searchResults.clear();
					System.out.println("Limpeza concluída. Tamanho após: " + searchResults.size());
					for (NodeOutput node : outList) {
						try {
							Socket s = new Socket(node.getAddress(), Integer.parseInt(node.getPort()));
							out = new ObjectOutputStream(s.getOutputStream());
	                        in = new ObjectInputStream(s.getInputStream());

							WordSearchMessage wsm = new WordSearchMessage(searchText, node.getAddress().toString(), node.getPort());
							out.writeObject(wsm);
//							out.flush();

//							sendMessage(searchText);

							Object msg;
							while ((msg = in.readObject()) != null) {
								if (msg instanceof FileSearchResult) {
									FileSearchResult f = (FileSearchResult) msg;
									FileSearchResult existing = searchResults.stream()
			                                .filter(item -> item.getFileName().equals(f.getFileName()))
			                                .findFirst()
			                                .orElse(null);
									System.out.println("1 " + f.getFileName());
									if (existing != null) {
		                                existing.getNodeList().add(f.getNode());
		                            } else {
		                                ArrayList<NodeOutput> aux = new ArrayList<>();
		                                aux.add(f.getNode());
		                                f.setNodeList(aux);
		                                searchResults.add(f);
		                            }
								} else if (msg instanceof String) {
									if (msg.equals("fim")) {
										System.out.println("Recebido sinal de fim.");
										break;
									}
								}
							}

						} catch (IOException | ClassNotFoundException e1) {
							System.err.println(e1.getMessage());
						}
					}
					
					for (FileSearchResult f : searchResults) {
						listModel.addElement(f.getFileName() + "<" + f.getNodeList().size() + ">");
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
							String port = portField.getText().trim();
							InetAddress addr = null;
							try {
								addr = InetAddress.getLocalHost();
							} catch (UnknownHostException e1) {
								e1.printStackTrace();
							}
							System.out.println("Endereço: " + addr);
							System.out.println("Porta: " + port);
							try {

								System.out.println("Endereço: " + addr);
								socket = new Socket(addr, Integer.parseInt(port));
								System.out.println("Socket: " + socket);
								out = new ObjectOutputStream(socket.getOutputStream());
//								in = new ObjectInputStream(socket.getInputStream());

								NewConnectionRequest ncr = new NewConnectionRequest(addr, noAtual.getPort());
								out.writeObject(ncr);

								NodeOutput node = new NodeOutput(port, addr);
								System.out.println("Novo nó: " + node);
								outList.add(node);

							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "O nó que tentou conectar não está ativo\n" + e1.getMessage());
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

					// Filtrar nós que possuem o arquivo
					FileSearchResult arquivoSelecionado = null;
					for (FileSearchResult f : searchResults) {
						if (f.getFileName().equals(selectedFile)) {
							arquivoSelecionado = f;
						}
					}

					// Verificar se o arquivo foi encontrado
					if (arquivoSelecionado == null) {
						JOptionPane.showMessageDialog(Gui.this, "Erro: Arquivo não encontrado nos nós disponíveis.");
						return;
					}

					// Inicializar o DownloadTasksManager

//					long start = System.currentTimeMillis();

					// Criar threads para cada nó disponível
//					ArrayList<NodeOutput> availableNodes = arquivoSelecionado.getNodeList();
//					ArrayList<FileBlockRequestMessage> blocks = downloadManager.getBlocks();
//					for (NodeOutput node : availableNodes) {
//						node.setBlocos(blocks.size());
//						DownloadThread thread = new DownloadThread(node, blocks, downloadManager);
//						thread.start();
//					}
					
					downloadManager = new DownloadTasksManager(arquivoSelecionado, id);
					downloadManager.download();
					long duration = downloadManager.getDuration();

//					long end = System.currentTimeMillis();
//					int tempo = (int) ((end - start) / 1000);
					JOptionPane.showMessageDialog(Gui.this, downloadManager.result() + "\nTempo decorrido: " + (int) (duration / 1000) + "s");
				}
			});

		}

//		public synchronized void sendMessage(String text) throws InterruptedException {
//			WordSearchMessage procura = new WordSearchMessage(text, address.toString(), port);
//			try {
//				for (NodeOutput out : outList)
//					out.getOut().writeObject(procura);
//
//			} catch (IOException e1) {
//				System.err.println("Erro ao enviar mensagem de pesquisa: " + e1.getMessage());
//			}
//		}
	}

	private static File[] getFiles() {
		String path = id;
		File dir = new File(path);
		return dir.listFiles(f -> true);
	}
}
