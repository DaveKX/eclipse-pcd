import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class FrameTest {
	private JFrame frame;

	public FrameTest() {
		frame = new JFrame("Test");
		
		// para que o botao de fechar a janela termine a aplicacao
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		addFrameContent();
		
		// para que a janela se redimensione de forma a ter todo o seu conteudo visivel
		frame.pack();
		frame.setSize(500, 300);
	}

	public void open() {
		// para abrir a janela (torna-la visivel)
		frame.setVisible(true);
	}

	private void addFrameContent() {
		/* para organizar o conteudo em grelha (linhas x colunas)
		se um dos valores for zero, o numero de linhas ou colunas (respetivamente) fica indefinido,
		e estas sao acrescentadas automaticamente */
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLayout(new GridLayout(4,5));
		JLabel title = new JLabel("title");
		frame.add(title);
		JTextField titleText = new JTextField("");
		frame.add(titleText);
		JLabel width = new JLabel("width");
		frame.add(width);
		JTextField widthText = new JTextField(""+frame.getWidth());
		frame.add(widthText);
		JLabel height = new JLabel("height");
		frame.add(height);
		JTextField heightText = new JTextField(""+frame.getHeight());
		frame.add(heightText);
		JCheckBox check = new JCheckBox("center");
		frame.add(check);
		System.out.println(widthText.getText() + " " + heightText.getText());
		JButton button = new JButton("update");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!titleText.getText().isBlank())
					frame.setTitle(titleText.getText());
				Dimension d = new Dimension(Integer.parseInt(widthText.getText()), Integer.parseInt(heightText.getText()));
				frame.setSize(d);
				checkSelected(check);
			}

			private void checkSelected(JCheckBox check) {
				if(check.isSelected())
					frame.setLocation(tela.width/2-frame.getSize().width/2, tela.height/2-frame.getSize().height/2);
			}
		});
		frame.add(button);	
		
		
	}
	
	public static void main(String[] args) {
		FrameTest window = new FrameTest();
		window.open();
	}
}
