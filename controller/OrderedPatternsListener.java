package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Model;

public class OrderedPatternsListener extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4126475212745922118L;

	private Model model;
	private JTextArea textInput;
	private JTextField patternInput;
	private JFileChooser fc;
	private JTextArea output;
	private JProgressBar pb;

	public OrderedPatternsListener( Model m, JTextField pattern, JTextArea textInput2, JTextArea output,
			JProgressBar progressBar )
	{
		this.model = m;
		this.patternInput = pattern;
		this.textInput = textInput2;
		this.output = output;
		this.pb = progressBar;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		switch ( e.getActionCommand() )
		{
		case "Show":
			if ( !textInput.getText().equals( "" ) && !patternInput.getText().equals( "" ) )
			{
				model.setOrderedPatterns( true );
				model.setOrderedOutput( "" );
				model.setPatternsLimit( 5 );
				model.setInput( textInput.getText() );
				model.anyPattern( model.findUniqueCharacters( patternInput.getText() ) );
				output.setText( model.getOrderedOuput() );
				pb.setValue( 100 );
				pb.setForeground( new Color( 0, 200, 0 ) );
			}
			break;

		case "Browse...":
			fc = new JFileChooser();
			int returnVal = fc.showOpenDialog( this );
			if ( returnVal == JFileChooser.APPROVE_OPTION )
			{
				File file = fc.getSelectedFile();
				String fileName = file.getName();
				if ( fileName.substring( fileName.lastIndexOf( "." ), fileName.length() ).equals( ".txt" ) )
				{
					BufferedReader br = null;
					try
					{
						br = new BufferedReader( new FileReader( file ) );
					}
					catch ( FileNotFoundException e2 )
					{
						e2.printStackTrace();
					}
					String st;
					try
					{
						textInput.setText( "" );
						while ( (st = br.readLine()) != null )
							textInput.setText( textInput.getText() + st );
					}
					catch ( IOException e1 )
					{
						e1.printStackTrace();
					}
				}
				else
				{
					output.setText( "Please select a .txt file." );
				}
			}

			break;

		case "Clear":
			textInput.setText( "" );
			patternInput.setText( "" );
			pb.setValue( 0 );
			output.setText( "" );

		default:
			break;
		}
	}
}
