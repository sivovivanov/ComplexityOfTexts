package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Model;

public class CalculateComplexityListener extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5102196251409347760L;
	private Model model;
	private JTextField patternInput;
	private JTextArea textInput, output;
	private JSpinner limit, modulo;
	private JFileChooser fc;
	private JProgressBar pb;
	private JLabel pl;

	public CalculateComplexityListener( Model m, JTextArea textInput, JTextField pattern, JSpinner limit,
			JSpinner modulo, JTextArea output, JProgressBar progressBar, JLabel progressLabel )
	{
		this.model = m;
		this.textInput = textInput;
		this.patternInput = pattern;
		this.limit = limit;
		this.modulo = modulo;
		this.output = output;
		this.pb = progressBar;
		this.pl = progressLabel;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String in = textInput.getText();
		String pattern = patternInput.getText();
		int l = (int) limit.getValue();
		int mod = (int) modulo.getValue();

		boolean somethingWrong = (in.equals( "" ) || pattern.equals( "" ) || mod == 0);

		switch ( e.getActionCommand() )
		{
		case "Calculate":
			model.setOutput( "" );
			model.setPatternsLimit( 5 );
			pb.setValue( 0 );
			if ( !somethingWrong )
			{
				Runnable runner = new Runnable()
				{
					public void run()
					{
						ArrayList<Boolean> dummyCheckBoxes = new ArrayList<>();
						for ( int i = 0; i < 5; i++ )
						{
							dummyCheckBoxes.add( true );
						}
						model.calculateComplexity( in, pattern, dummyCheckBoxes, Integer.valueOf( mod ), l, pb, pl,
								25.0 );
						output.setText( model.getOutput() );
					}
				};
				Thread t = new Thread( runner, "Code Executer" );
				t.start();
			}
			else
			{
				output.setText( "Please fill in all fields." );
			}
			model.setOutput( "" );

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

		case "Load":
//			if ( model.wordCount( textInput.getText() ) == 1 )
				textInput.setText( model.cleanWikiText( model.keyWordText( textInput.getText() ) ) );
//			else
//				textInput.setText( "Please enter a single Key Word." );

			break;

		case "Clear":
			textInput.setText( "" );
			patternInput.setText( "" );
			pb.setValue( 0 );
			pl.setText( "" );
			output.setText( "" );

		default:
			break;
		}
	}

}
