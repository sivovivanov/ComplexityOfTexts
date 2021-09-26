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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import model.Model;

public class MorphismsListener extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 928985493559617163L;
	private Model model;
	private JTextArea wf0, wf1, output;
	private JSpinner itterations;
	private JFileChooser fc;
	private JProgressBar pb;
	private JLabel pl;

	public MorphismsListener( Model m, JTextArea wordFor0, JTextArea wordFor1, JSpinner itterations, JTextArea output,
			JProgressBar progressBar, JLabel progressLabel )
	{
		this.model = m;
		this.wf0 = wordFor0;
		this.wf1 = wordFor1;
		this.itterations = itterations;
		this.output = output;
		this.pb = progressBar;
		this.pl = progressLabel;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String zero = wf0.getText();
		String one = wf1.getText();
		int itter = (int) itterations.getValue();

		switch ( e.getActionCommand() )
		{
		case "Generate":
			boolean binaryFlag = true;
			pb.setValue( 0 );
			pb.setForeground( new Color( 255, 70, 70 ) );
			for ( char c : zero.toCharArray() )
			{
				if ( c != '0' && c != '1' )
				{
					binaryFlag = false;
				}
			}
			for ( char c : one.toCharArray() )
			{
				if ( c != '0' && c != '1' )
				{
					binaryFlag = false;
				}
			}
			if ( binaryFlag )
			{
				Runnable runner = new Runnable()
				{
					public void run()
					{
						output.setText( model.morphisms( itter, zero, one, pb ) );
						pl.setText( "Finished!" );
					}
				};
				Thread t = new Thread( runner, "Code Executer" );
				t.start();
			}
			else
			{
				output.setText( "Please enter only binary words for 0 and 1" );
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
						wf0.setText( "" );
						wf1.setText( "" );
						int i = 0;
						while ( (st = br.readLine()) != null )
						{
							if ( i == 0 )
							{
								wf0.setText( wf0.getText() + st );
							}
							else
							{
								wf1.setText( wf1.getText() + st );
								break;
							}
							i++;
						}
					}
					catch ( IOException e1 )
					{
						e1.printStackTrace();
					}
				}
			}
			else
			{
				output.setText( "Please select a .txt file." );
			}

			break;

		case "Clear":
			wf0.setText( "" );
			wf1.setText( "" );
			itterations.setValue( 0 );
			pb.setValue( 0 );
			pl.setText( "" );
			output.setText( "" );
			break;

		default:
			break;
		}
	}

}
