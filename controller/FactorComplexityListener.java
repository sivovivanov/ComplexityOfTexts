package controller;

import java.awt.Color;
import java.awt.Cursor;
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

import model.Model;

public class FactorComplexityListener extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3068298002041229870L;
	private Model model;
	private JTextArea textInput;
	private JTextArea output;
	private JFileChooser fc;
	private JProgressBar pb;
	private JSpinner limit;
	private JLabel pl;

	public FactorComplexityListener( Model m, JTextArea textInput, JSpinner limit, JTextArea output,
			JProgressBar progressBar, JLabel progressLabel )
	{
		this.model = m;
		this.textInput = textInput;
		this.limit = limit;
		this.output = output;
		this.pb = progressBar;
		this.pl = progressLabel;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		pb.setValue( 0 );
		switch ( e.getActionCommand() )
		{
		case "Show":
			setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
			output.setText( "Output" );
			if ( !textInput.getText().equals( "" ) && (int) limit.getValue() > 0 )
			{
				pl.setText( "Computing Factors" );
				Runnable runner = new Runnable()
				{
					public void run()
					{
						int length = 1;
						double x = 0;
						ArrayList<ArrayList<String>> factors = model.factorComplexity( textInput.getText(),
								(int) limit.getValue(), false );
						for ( ArrayList<String> current : factors )
						{
							output.setText( output.getText() + "\n" + "Length: " + length + ", Elements: "
									+ current.size() + " - " + current.toString() );
							pb.setValue( (int) ((x / (double) factors.size()) * 100) );
							if ( pb.getValue() <= 25 )
							{
								pb.setForeground( new Color( 255, 70, 70 ) );
							}
							else if ( pb.getValue() <= 50 )
							{
								pb.setForeground( new Color( 255, 170, 70 ) );
							}
							else if ( pb.getValue() <= 75 )
							{
								pb.setForeground( new Color( 210, 205, 0 ) );
							}
							else
							{
								pb.setForeground( new Color( 0, 200, 0 ) );
							}
							if ( factors.get( factors.size() - 1 ).equals( current ) )
							{
								pb.setValue( 100 );
							}
							length++;
							x++;
						}
						pl.setText( "Done" );
						pb.setForeground( new Color( 0, 200, 0 ) );
					}
				};
				Thread t = new Thread( runner, "Code Executer" );
				t.start();
			}
			else if ( (int) limit.getValue() == 0 && !textInput.getText().equals( "" ) )
			{
				pl.setText( "Computing Factors" );
				Runnable runner = new Runnable()
				{
					public void run()
					{
						ArrayList<ArrayList<String>> factors = model.factorComplexity( textInput.getText(),
								textInput.getText().length(), false );
						int length = 1;
						double x = 0;
						for ( ArrayList<String> current : factors )
						{
							output.setText( output.getText() + "\n" + "Length: " + length + ", Elements: "
									+ current.size() + " - " + current.toString() );
							pb.setValue( (int) ((x / (double) factors.size()) * 100) );
							if ( pb.getValue() <= 25 )
							{
								pb.setForeground( new Color( 255, 70, 70 ) );
							}
							else if ( pb.getValue() <= 50 )
							{
								pb.setForeground( new Color( 255, 170, 70 ) );
							}
							else if ( pb.getValue() <= 75 )
							{
								pb.setForeground( new Color( 210, 205, 0 ) );
							}
							else
							{
								pb.setForeground( new Color( 0, 200, 0 ) );
							}
							if ( factors.get( factors.size() - 1 ).equals( current ) )
							{
								pb.setValue( 100 );
							}
							length++;
							x++;
						}
						pl.setText( "Done" );
						pb.setForeground( new Color( 0, 200, 0 ) );
					}
				};
				Thread t = new Thread( runner, "Code Executer" );
				t.start();
			}
			else
			{
				output.setText( "No input present" );
			}
			setCursor( null );

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
			pl.setText( "" );
			pb.setValue( 0 );
			output.setText( "" );

			break;

		default:
			break;
		}
	}

}
