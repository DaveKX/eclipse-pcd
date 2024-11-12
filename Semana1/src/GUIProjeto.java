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

@SuppressWarnings("serial")
public class GUIProjeto extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JButton downloadButton;
	private JButton connectButton;
	private JList<String> resultList;
	private DefaultListModel<String> listModel;
	private File[] files;
	public GUIProjeto() {

		setTitle("GUI Projeto (Altura: " + this.getHeight() + ", Largura: " + this.getWidth());
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
		for(File f : files)
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
					for(File f : files)
						if(f.getName().indexOf(searchText) != -1)
							listModel.addElement(f.getName());
				} else {
					for(File f : files)
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
				JDialog dialog = new JDialog(GUIProjeto.this, "Ligar a Nó", true);
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
				dialog.setLocationRelativeTo(GUIProjeto.this);
				dialog.setVisible(true);
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setTitle("GUI Projeto (Altura: " + getHeight() + ", Largura: " + getWidth() + ")");
			}
		});
	}
	
	public File[] getFiles() {
		String path = "src/files";
		File[] files = new File(path).listFiles(new FileFilter() {
			public boolean accept(File f) {
				return true;
			}
		});
		return files;
	}

	public static void main(String[] args) {
		GUIProjeto window = new GUIProjeto();
		window.setVisible(true);
	}
}
