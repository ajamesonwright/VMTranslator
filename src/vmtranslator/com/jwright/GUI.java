package com.jwright;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class GUI extends JPanel implements ActionListener {
	JFrame frame;
	JPanel buttonPanel;
	JTextArea logI, logO;
	JScrollPane logScrollPaneI, logScrollPaneO;
	JButton openButton, saveButton, parseButton;
	JFileChooser fc;
	File activeDirectory;
	List<File> file;
		
	public GUI() {
		super(new BorderLayout());
		
		// main window for user interaction
		frame = new JFrame();
		frame.setSize(1000,1000);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		// set title for main window
		frame.setTitle("VM Translator");
		
		// container for interface buttons
		buttonPanel = new JPanel();
		
		// log to display input file, unedited
		logI = new JTextArea(50, 50);
		logI.setMargin(new Insets(5,5,5,5));
		logI.setEditable(false);
		logScrollPaneI = new JScrollPane(logI);
		// log to display parsed file information
		logO = new JTextArea(50, 50);
		logO.setMargin(new Insets(5,5,5,5));
		logO.setEditable(false);
		logScrollPaneO = new JScrollPane(logO);

		// set file chooser and allow selection of file or directory
		fc = new JFileChooser();
		activeDirectory = new File("C:\\Users\\Jameson\\Nand to Tetris\\nand2tetris\\projects\\08");
		fc.setCurrentDirectory(activeDirectory);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// set filter for .vm files only
		FileNameExtensionFilter filter = new FileNameExtensionFilter("VM Files", "vm");
		fc.setFileFilter(filter);
		
		openButton = new JButton("Load file...");
		openButton.addActionListener(this);
		saveButton = new JButton("Save file...");
		saveButton.addActionListener(this);
		parseButton = new JButton("Parse");
		parseButton.addActionListener(this);
		
		buttonPanel.add(openButton);
		buttonPanel.add(parseButton);
		buttonPanel.add(saveButton);
		
		frame.add(buttonPanel, BorderLayout.PAGE_START);
		frame.add(logScrollPaneI, BorderLayout.WEST);
		frame.add(logScrollPaneO, BorderLayout.EAST);
		
		// display the window
		frame.pack();
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// handle action from open button
		if (e.getSource() == openButton) {
			logI.setText("");
			logO.setText("");
			int returnVal = fc.showOpenDialog(GUI.this);
			file = new ArrayList<File>();
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// if a directory selected, step through files contained
				if (fc.getSelectedFile().isDirectory()) {
					String fileExt;
					for (String s : fc.getSelectedFile().list()) {

						if (s.indexOf(".") >= 0) {
							fileExt = s.substring(s.indexOf("."));
							
							// check file extension to verify it is of type .vm
							if (fileExt.equals(".vm")) {

								// concat directory path with applicable filename
								System.out.println(fc.getSelectedFile().getAbsolutePath() + "\\" + s);
								file.add(new File(fc.getSelectedFile().getAbsolutePath() + "\\" + s));
							}
						}
						
					}
					if (file.isEmpty()) {
						logI.append("\n No .vm files contained in selected directory. Check sub-directories.\n");
					}
				}
				// if a file selected, attempt to parse
				else {
					file.add(fc.getSelectedFile());
				}

				for (File f : file) {
					logI.append("\n*** Opening" + " " + f.getName() + " ***\n\n");	
					VMTranslator.openFile(f);					
				}
						
			}
			// if cancelled, append message to scroll pane
			else {
				logI.append("\n Open command cancelled by user.\n");
			}
		}
		if (e.getSource() == parseButton) {
			logO.setText("");

			// check logI for contents, parse file if found
			if (logI.getText().trim().length() > 0) {
				for (File f : file) {
					logO.append("Parsing" + " " + f.getName() + "...\n\n");
				}
				VMTranslator.triggerParser();
			}
		}
		if (e.getSource() == saveButton) {
			// if file has been parsed, write logO contents to file
			if (logO.getText().trim().length() > 0) {
				VMTranslator.triggerCodeWriter(file);
			}			
		}
	}
}
