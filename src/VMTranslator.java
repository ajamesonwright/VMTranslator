import java.io.File;

import javax.swing.SwingUtilities;

public class VMTranslator {
	static GUI gui;
	static Parser fileParser;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui = new GUI();

			}
		});
	}

	public static void createParser(File file) {
		fileParser = new Parser(file);
	}
}
