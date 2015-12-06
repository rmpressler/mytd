package myTD;

import javax.swing.JFrame;

public class Editor {
	public static void main(String[] args) {
		JFrame window = new JFrame("Editor");
		window.setContentPane(new EditorPanel());
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
