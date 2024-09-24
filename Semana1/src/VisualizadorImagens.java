import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class VisualizadorImagens {
	private JFrame frame;
	private int pos = 0;
	private File[] images;
	
	public VisualizadorImagens() {
		frame = new JFrame("Imagens");

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addFrameContent();

		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	public void open() {
		frame.setVisible(true);
	}

	private void addFrameContent() {
		frame.setLayout(new BorderLayout());

		JButton next = new JButton(">");
		JButton previous = new JButton("<");
		JButton update = new JButton("UPDATE");
		JLabel image = new JLabel();
		JLabel imageName = new JLabel();

		images = getImages();
		
		if (images == null || images.length == 0) {
	        JOptionPane.showMessageDialog(frame, "Nenhuma imagem encontrada no diretório");
	        return;
	    }

		ImageIcon icon = new ImageIcon(images[pos].getAbsolutePath());
		image.setIcon(icon);
		imageName.setText(images[pos].getName());
		imageName.setHorizontalAlignment(SwingConstants.CENTER);

		frame.add(next, BorderLayout.EAST);
		frame.add(previous, BorderLayout.WEST);
		frame.add(update, BorderLayout.SOUTH);
		frame.add(imageName, BorderLayout.NORTH);
		frame.add(image, BorderLayout.CENTER);

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pos + 1 >= images.length) {
					JOptionPane.showMessageDialog(frame, "Não há mais imagens");
				} else {
					ImageIcon icon = new ImageIcon(images[++pos].getAbsolutePath());
					image.setIcon(icon);
					imageName.setText(images[pos].getName());
				}
			}
		});

		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pos - 1 < 0) {
					JOptionPane.showMessageDialog(frame, "Não há mais imagens");
				} else {
					ImageIcon icon = new ImageIcon(images[--pos].getAbsolutePath());
					image.setIcon(icon);
					imageName.setText(images[pos].getName());
				}
			}
		});
		
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				images = getImages();
			}
		});

	}

	public File[] getImages() {
		String path = "src/images";
		File[] files = new File(path).listFiles(new FileFilter() {
			public boolean accept(File f) {
				return true;
			}
		});
		return files;
	}

	public static void main(String[] args) {
		VisualizadorImagens window = new VisualizadorImagens();
		window.open();
	}
}
