package b_vorbereitung;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
/**
 * Diese Klasse erm�glicht es, Bilder von der Festplatte zu oeffnen.
 * @author Kosmonaut
 *
 */
public class ImageOpen {
	public static void main(String[] args) {
		final JFileChooser fChooser = new JFileChooser();
		// hinzufuegen eines FileFilters, der nur Bilder als �ffnenbare Dateien
		// anzeigt
		fChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "images( jpg, bmp, gif, png, jpeg, wbmp)";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				for (String s : ImageIO.getReaderFileSuffixes()) {
					if (f.toString().endsWith("." + s)) {
						return true;
					}
				}
				return false;
			}
		});

		int returnVal = fChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			showImageFile(fChooser.getSelectedFile());
		}
	}

	/**
	 * Zeigt eine als Bild in einem eigenen Fenster an
	 * 
	 * @param file
	 *            die Datei die angezeigt werden soll
	 */
	private static void showImageFile(final File file) {
		final JFrame imgframe = new JFrame();
		// try-catch-Block f�r den Fall das ImageIO file nicht lesen kann
		try {
			final BufferedImage img = ImageIO.read(file);
			imgframe.getContentPane().add(new JPanel() {
				/**
						 * 
						 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					BufferedImage image = img;
					Graphics2D graphics2d = (Graphics2D) g;
					graphics2d.drawImage(image, 0, 0, null);
					super.paintComponents(g);
				}
			});
			imgframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			imgframe.setSize(img.getWidth(), img.getHeight());
			imgframe.setVisible(true);
		} catch (IOException e) {
			System.err.println("IOException ist aufgetreten: " + file
					+ " konnte nicht gelesen werden");
		}
	};
}
