package view;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public interface Module
{
	public void createGUI();

	public void createButtons();

	void styleButton( JButton button );

	void styleRadioButton( JRadioButton rButton );

	void styleCheckBox( JCheckBox checkBox );

	void styleTextField( JTextField textField );

	void styleTextArea( JTextArea textArea );

	void styleLabel( JLabel label );
}
