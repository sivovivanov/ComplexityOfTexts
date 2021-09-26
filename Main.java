import javax.swing.SwingUtilities;

import model.Model;
import view.View;

public class Main {
	public static void main(String[] args) {
		Model model = new Model("");
		View view = new View(model);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.createMainMenu().createGUI();
			}
		});
	}
}