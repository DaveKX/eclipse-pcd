import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    public GUIProjeto() {
        
    	setTitle("GUI Projeto (Altura: " + this.getHeight() + ", Largura: " + this.getWidth());
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        // Painel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3, 0, 0));
        JLabel searchLabel = new JLabel("Texto a procurar: ");
        searchField = new JTextField(20);
        searchButton = new JButton("Procurar");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Painel esquerdo
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        leftPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        // Painel direito
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

        // Ação do botão "Procurar"
//        searchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String searchText = searchField.getText();
//                listModel.clear();
//                if (!searchText.isEmpty()) {
//                    // Simulando uma pesquisa
//                    for (int i = 1; i <= 10; i++) {
//                        listModel.addElement(searchText + " Resultado " + i);
//                    }
//                }
//            }
//        });
        
     // Ação do botão "Ligar a Nó"
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Criar a nova janela (JDialog)
                JDialog dialog = new JDialog(GUIProjeto.this, "Ligar a Nó", true);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                dialog.setResizable(false);

                // Campos de texto e botões para o JDialog
                JLabel addressLabel = new JLabel("Endereço: ");
                JTextField addressField = new JTextField();
                JLabel portLabel = new JLabel("Porta: ");
                JTextField portField = new JTextField();
                JButton cancelButton = new JButton("Cancelar");
                JButton okButton = new JButton("OK");

                addressField.setColumns(15);;
                portField.setColumns(10);
                
                dialog.add(addressLabel);
                dialog.add(addressField);
                dialog.add(portLabel);
                dialog.add(portField);
                dialog.add(cancelButton);
                dialog.add(okButton);

                // Permitir apenas numeros no campo da porta
                portField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c)) {
                            e.consume();
                        }
                    }
                });
                
                // Cancelar
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });

                // Ok
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

    public static void main(String[] args) {
    	GUIProjeto window = new GUIProjeto();
    	window.setVisible(true);
    }
}
