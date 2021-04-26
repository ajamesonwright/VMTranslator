package com.jwright;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class GUI extends JPanel implements ActionListener {
	JFrame frame;
	JPanel buttonPanel;
	JTextArea logI, logO;
	JScrollPane logScrollPaneI, logScrollPaneO;
	JButton openButton, saveButton, parseButton;
	JFileChooser fc;
	File activeDirectory;
	File file;
		
	public GUI() {
		super(new BorderLayout());
		
		// main window for user interaction
		frame = new JFrame();
		frame.setSize(400,400);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		// set title for main window
		frame.setTitle("VM Translator");
		
		// container for interface buttons
		buttonPanel = new JPanel();
		
		// log to display input file, unedited
		logI = new JTextArea(30, 30);
		logI.setMargin(new Insets(5,5,5,5));
		logI.setEditable(false);
		logScrollPaneI = new JScrollPane(logI);
		// log to display parsed file information
		logO = new JTextArea(30, 30);
		logO.setMargin(new Insets(5,5,5,5));
		logO.setEditable(false);
		logScrollPaneO = new JScrollPane(logO);

		fc = new JFileChooser();
		activeDirectory = new File("C:/Users/Jamie/git/VMTranslator/src/vmtranslator/com/jwright/");
		fc.setCurrentDirectory(activeDirectory);
		
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
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// if a file selected, attempt to parse
				file = fc.getSelectedFile();
				
				logI.append("Opening" + " " + file.getName() + "\n\n");			
				VMTranslator.openFile(file);					
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
				logO.append("Parsing" + " " + file.getName() + "...\n\n");
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
