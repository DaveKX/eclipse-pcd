import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

@SuppressWarnings("serial")
public class Gui extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JButton downloadButton;
	private JButton connectButton;
	private JList<String> resultList;
	private DefaultListModel<String> listModel;
	private File[] files;
	private DownloadTasksManager downloadManager;

	public Gui(File[] file, String title) {
		this.files = file;
		setTitle("Cliente " + title + " (Altura: " + this.getHeight() + ", Largura: " + this.getWidth());
		setSize(800, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		setLocationRelativeTo(null);
		
	//	files = getFiles();
		
		
		downloadManager = new DownloadTasksManager();

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
		for (File f : files)
			listModel.addElement(f.getName());

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
//                listModel.clear();
				if (!searchText.isEmpty()) {
					for (File f : files)
						if (f.getName().indexOf(searchText) != -1)
							listModel.addElement(f.getName());
				} else {
					for (File f : files)
						listModel.addElement(f.getName());
				}
			}
		});

//		searchField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_B) {
//					String searchText = searchField.getText();
//					System.out.println("teste");
////                  listModel.clear();
//					if (!searchText.isEmpty()) {
//						listModel.addElement(searchText + " Nº elementos: " + listModel.capacity());
//					}
//				}
//			}
//		});

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
				JTextField addressField = new JTextField();
				JLabel portLabel = new JLabel("Porta: ");
				JTextField portField = new JTextField();
				JButton cancelButton = new JButton("Cancelar");
				JButton okButton = new JButton("OK");

				addressField.setColumns(15);
				;
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
						String address = addressField.getText();
						String port = portField.getText();

						System.out.println("Endereço: " + address);
						System.out.println("Porta: " + port);
						dialog.dispose();
					}
				});

				dialog.setSize(600, 80);
				dialog.setLocationRelativeTo(Gui.this);
				dialog.setVisible(true);
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setTitle("GUI Projeto (Altura: " + getHeight() + ", Largura: " + getWidth() + ")");
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

				File file = new File("src/files/" + selectedFile);
				if (!file.exists()) {
					JOptionPane.showMessageDialog(Gui.this, "Arquivo não encontrado: " + selectedFile);
					return;
				}

				List<FileBlockRequestMessage> blocks = BlockManager.createBlocks(file);
				if (blocks.isEmpty()) {
					JOptionPane.showMessageDialog(Gui.this, "Erro ao criar blocos para o arquivo.");
				} else {
					JOptionPane.showMessageDialog(Gui.this, "Blocos criados: " + blocks.size());
					blocks.forEach(downloadManager::addTask);

					// Iniciar threads de download
					for (int i = 0; i < 5; i++) { // Máximo de 5 threads
						new DownloadWorker(downloadManager).start();
					}
				}
			}
		});

	}

	private static File[] getFiles() {
        String path = "src/files";
        File dir = new File(path);
        return dir.listFiles(f -> true);
    }
	
	private void addFiles(String path) {
		File dir = new File(path);
		File[] newF = dir.listFiles(f -> true);
		File[] newFiles = new File[newF.length + files.length];
		int i;
		for(i = 0; i<newF.length; i++){
			newFiles[i] = newF[i];
		}
		for(i = 0; i<files.length; i++){
			newFiles[i+newF.length] = files[i];
		}
	}

	public static void main(String[] args) {
		Gui window = new Gui(getFiles(), "0");
		window.setVisible(true);
	}
}
