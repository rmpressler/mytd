package myTD;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	JPanel buttonPanel;
	JButton selectionButton;
	JButton button0;
	JButton button1;
	JButton button2;
	JButton saveButton;
	
	TileEditorPanel surfacePanel;
	
	private static int selection;
	
	public EditorPanel() {
		setLayout(new BorderLayout());
		
		buttonPanel = new JPanel();
		
		selectionButton = new JButton("Selection Tool");
		selectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
			}
		});
		buttonPanel.add(selectionButton);
		
		button0 = new JButton("Grass");
		button0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setSelection(0);
			}
		});
		buttonPanel.add(button0);
		
		button1 = new JButton("Path");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setSelection(1);
			}
		});
		buttonPanel.add(button1);
		
		button2 = new JButton("Mountain");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setSelection(2);
			}
		});
		buttonPanel.add(button2);
		
		saveButton = new JButton("Save Map");
		buttonPanel.add(saveButton);
		
		add(buttonPanel, BorderLayout.NORTH);
		
		int newWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "Please input a width: "));
		int newHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "Please input a height: "));
		surfacePanel = new TileEditorPanel(newWidth, newHeight);
		add(surfacePanel, BorderLayout.CENTER);
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				surfacePanel.tileMap.saveMap();
			}
		});
		
		setSelection(-1);
	}

	public static int getSelection() {
		return selection;
	}

	public static void setSelection(int selection) {
		EditorPanel.selection = selection;
	}
	
}
