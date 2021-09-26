package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Model;
import view.LineChart;

@SuppressWarnings("serial")
public class CompareTextsListener extends JPanel implements ActionListener
{
	private Model model;
	private String keyWord = "~";
	private JTextField patternInput;
	private JTextArea textInput, output;
	private JSpinner limit, modulo, patternLength;
	private DefaultTableModel tableModel;
	private JTable table;
	private ArrayList<JCheckBox> boxes;
	private JSlider lengthsSnapshot;
	private JFileChooser fc;
	private JProgressBar pb;
	private JLabel pl;
	private HashMap<String, String> textsAndExplanation;

	private HashMap<Integer, ArrayList<Double>> rowAndComplexity;
	private HashMap<Integer, ArrayList<Double>> rowAndLength;
	private HashMap<Double, Double> complexityAndLength;
	private HashMap<Integer, Color> rowAndColor;

	private Stack<Object[]> deletedSingleRows;

	private double highestScore;
	private double lowestScore;
	private double highestLength;
	private double lowestLength;

	private boolean standardLength;
	private boolean manageLenth;

	public CompareTextsListener( Model m, JTextArea textInput, JTextField pattern, JSpinner limit, JSpinner modulo,
			JSpinner patternLength, DefaultTableModel tm, JTable table, ArrayList<JCheckBox> checkBoxes,
			JSlider lengthsSnapshot, JTextArea output, JProgressBar progressBar, JLabel progressLabel )
	{
		this.model = m;
		this.textInput = textInput;
		this.patternInput = pattern;
		this.limit = limit;
		this.modulo = modulo;
		this.patternLength = patternLength;
		this.output = output;
		this.tableModel = tm;
		this.table = table;
		this.boxes = checkBoxes;
		this.lengthsSnapshot = lengthsSnapshot;
		this.pb = progressBar;
		this.pl = progressLabel;

		this.textsAndExplanation = new HashMap<>();
		this.rowAndComplexity = new HashMap<>();
		this.rowAndLength = new HashMap<>();
		this.rowAndColor = new HashMap<>();
		this.complexityAndLength = new HashMap<>();
		this.deletedSingleRows = new Stack<>();
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		int row = 0;
		String in = textInput.getText();
		String pattern = patternInput.getText();
		int l = (int) limit.getValue();
		int mod = (int) modulo.getValue();
		double barIncrement = (100.0 / (double) table.getRowCount()) / 4.0;
		ArrayList<Boolean> options = new ArrayList<>();
		Hashtable<Integer, JLabel> labels = new Hashtable<>();

		switch ( e.getActionCommand() )
		{
		case "Calculate":
			model.setOutput( "" );
			pb.setValue( 0 );
			pb.setForeground( new Color( 255, 70, 70 ) );
			Runnable runner = new Runnable()
			{
				public void run()
				{
					model.setPatternsLimit( (int) patternLength.getValue() );
					highestLength = 0;
					highestScore = 0;
					Random random = new Random();
					for ( int i = 0; i < table.getRowCount(); i++ )
					{
						textsAndExplanation.put( table.getValueAt( i, 1 ).toString(), "" );
						double len = (double) table.getValueAt( i, 2 );
						int r = random.nextInt( 256 );
						int g = random.nextInt( 256 );
						int b = random.nextInt( 256 );
						if ( len > highestLength )
						{
							highestLength = len;
						}
						if ( !rowAndColor.containsKey( i ) )
						{
							rowAndColor.put( i, new Color( r, g, b ) );
						}
					}

					for ( JCheckBox b : boxes )
					{
						options.add( b.isSelected() );
					}

					standardLength = options.get( options.size() - 2 );
					manageLenth = options.get( options.size() - 1 );

					for ( String text : textsAndExplanation.keySet() )
					{
						double complexityValue = 0;
						if ( manageLenth )
						{
							if ( model.characterCount( text ) < lengthsSnapshot.getValue() )
							{
								complexityValue = model.calculateComplexity(
										model.extendToLength( (int) lengthsSnapshot.getValue(), text ), pattern,
										options, Integer.valueOf( mod ), l, pb, pl, barIncrement );
								complexityAndLength.put( complexityValue, (double) lengthsSnapshot.getValue() );
							}
							else if ( model.characterCount( text ) > lengthsSnapshot.getValue() )
							{
								highestLength = lengthsSnapshot.getValue();
								String temp_text = text;
								int substring = temp_text.length();
								while ( model.characterCount( temp_text ) > lengthsSnapshot.getValue() )
								{
									temp_text = temp_text.substring( 0, substring );
									substring--;
								}
								complexityValue = model.calculateComplexity( temp_text, pattern, options,
										Integer.valueOf( mod ), l, pb, pl, barIncrement );
								complexityAndLength.put( complexityValue, (double) lengthsSnapshot.getValue() );
							}
							else
							{
								complexityValue = model.calculateComplexity( text, pattern, options,
										Integer.valueOf( mod ), l, pb, pl, barIncrement );
								for ( int i = 0; i < table.getRowCount(); i++ )
								{
									if ( table.getValueAt( i, 1 ).toString().equals( text ) )
									{
										complexityAndLength.put( complexityValue, (double) table.getValueAt( i, 2 ) );
									}
								}
							}
						}
						else if ( standardLength )
						{
							complexityValue = model.calculateComplexity( text, pattern, options, Integer.valueOf( mod ),
									l, pb, pl, barIncrement );
							for ( int i = 0; i < table.getRowCount(); i++ )
							{
								if ( table.getValueAt( i, 1 ).toString().equals( text ) )
								{
									complexityAndLength.put( complexityValue, (double) table.getValueAt( i, 2 ) );
								}
							}
						}
						textsAndExplanation.put( text, model.getOutput() );
						model.setOutput( "" );
						for ( int j = 0; j < table.getRowCount(); j++ )
						{
							if ( table.getValueAt( j, 1 ).toString().equals( text ) )
							{
								table.setValueAt( complexityValue, j, 3 );
							}
						}
					}
					pb.setValue( 100 );
					pl.setText( "Done!" );
					for ( int i = 0; i < table.getRowCount(); i++ )
					{
						double comp = (double) table.getValueAt( i, 3 );
						double len = (double) table.getValueAt( i, 2 );
						if ( comp > highestScore )
						{
							highestScore = comp;
						}
						if ( lowestScore == 0 )
						{
							lowestScore = comp;
						}
						else if ( comp < lowestScore )
						{
							lowestScore = comp;
						}

						ArrayList<Double> previousValues;
						ArrayList<Double> previousLengths;
						if ( rowAndComplexity.get( i ) == null )
						{
							previousValues = new ArrayList<>();
						}
						else
						{
							previousValues = rowAndComplexity.get( i );
						}

						if ( rowAndLength.get( i ) == null )
						{
							previousLengths = new ArrayList<>();
						}
						else
						{
							previousLengths = rowAndLength.get( i );
						}
						previousValues.add( comp );
						previousLengths.add( len );
						rowAndComplexity.put( i, previousValues );
						rowAndLength.put( i, previousLengths );
					}
					textInput.setText( table.getValueAt( 0, 1 ).toString() );
					output.setText( textsAndExplanation.get( table.getValueAt( 0, 1 ).toString() ) );

				}
			};
			Thread t = new Thread( runner );
			t.start();

			break;

		case "View Chart":
			if ( highestLength > 0 && highestScore > 0 )
			{
				JFrame frame = new JFrame( "Chart" );
				LineChart lc = new LineChart( highestLength, highestScore, lowestLength, lowestScore, table,
						rowAndComplexity, rowAndLength, complexityAndLength, rowAndColor,
						lengthsSnapshot.getMinimum() );

				lc.setBackground( Color.lightGray );
				frame.getContentPane().add( lc );
				frame.setResizable( false );
				frame.pack();
				frame.setLocationByPlatform( true );
				frame.setVisible( true );
			}
			else
			{
				output.setText( "No complexity values have been calculated!" );
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

		case "Arrow":
			boolean containsAlready = false;
			for ( int i = 0; i < table.getRowCount(); i++ )
			{
				if ( table.getValueAt( i, 1 ).toString().equals( in ) )
				{
					containsAlready = true;
				}
			}
			if ( !containsAlready && !in.isEmpty() )
			{
				tableModel.addRow( new Object[]
				{ keyWord, in, model.characterCount( in ), 0.0 } );

				for ( int i = 0; i < table.getRowCount(); i++ )
				{
					double current = (Double) table.getValueAt( i, 2 );
					if ( lowestLength == 0 )
					{
						lowestLength = current;
					}
					else if ( current < lowestLength )
					{
						lowestLength = current;
					}
					if ( i == 0 )
					{
						lengthsSnapshot.setMinimum( (int) current );
						lengthsSnapshot.setMaximum( (int) current );
					}
					labels.put( (int) current, new JLabel( String.valueOf( (int) current ) ) );
					if ( (Double) table.getValueAt( i, 2 ) < lengthsSnapshot.getMinimum() )
					{
						lengthsSnapshot.setMinimum( (int) current );
					}
					else if ( (Double) table.getValueAt( i, 2 ) > lengthsSnapshot.getMaximum() )
					{
						lengthsSnapshot.setMaximum( (int) current );
					}
				}
				lengthsSnapshot.setLabelTable( labels );
			}
			lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
			keyWord = "~";
			textInput.setText( "" );

			break;

		case "Clear Table":
			int r = table.getRowCount() - 1;
			while ( tableModel.getRowCount() > 0 )
			{
				String keyWord = (String) table.getValueAt( r, 0 );
				String text = (String) table.getValueAt( r, 1 );
				double length = (double) table.getValueAt( r, 2 );

				deletedSingleRows.push( new Object[]
				{ keyWord, text, length, 0.0 } );
				tableModel.removeRow( r );
				r--;
			}
			pb.setValue( 0 );
			highestLength = 0;
			lowestLength = 0;
			lowestScore = 0;
			highestScore = 0;
			rowAndComplexity.clear();
			rowAndLength.clear();
			rowAndColor.clear();
			complexityAndLength.clear();
			lengthsSnapshot.setMinimum( 0 );
			lengthsSnapshot.setMaximum( 0 );
			lengthsSnapshot.setMajorTickSpacing( 0 );
			lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );

			break;

		case "Clear Results":
			for ( int i = 0; i < table.getRowCount(); i++ )
			{
				table.setValueAt( 0.0, i, 3 );
			}
			pb.setValue( 0 );
			highestLength = 0;
			highestScore = 0;
			rowAndComplexity.clear();
			rowAndLength.clear();
			rowAndColor.clear();
			complexityAndLength.clear();

			break;

		case "Delete Row":
			row = table.getSelectedRow();
			labels = new Hashtable<>();
			if ( row != -1 )
			{
				String keyWord = (String) table.getValueAt( row, 0 );
				String text = (String) table.getValueAt( row, 1 );
				double length = (double) table.getValueAt( row, 2 );

				deletedSingleRows.push( new Object[]
				{ keyWord, text, length, 0.0 } );

				lengthsSnapshot.setMaximum( 0 );
				tableModel.removeRow( row );
				rowAndColor.remove( row );
				ArrayList<Double> complexitiesOfRow = rowAndComplexity.get( row );
				rowAndComplexity.remove( row );
				rowAndLength.remove( row );
				if ( complexitiesOfRow != null )
				{
					for ( Double current : complexitiesOfRow )
					{
						complexityAndLength.remove( current );
					}
				}
				if ( table.getRowCount() >= 1 )
				{
					for ( int i = 0; i < table.getRowCount(); i++ )
					{
						double current = (Double) table.getValueAt( i, 2 );
						if ( i == 0 )
						{
							lengthsSnapshot.setMinimum( (int) current );
							lengthsSnapshot.setMaximum( (int) current );
						}
						labels.put( (int) current, new JLabel( String.valueOf( (int) current ) ) );
						if ( (Double) table.getValueAt( i, 2 ) < lengthsSnapshot.getMinimum() )
						{
							lengthsSnapshot.setMinimum( (int) current );
						}
						else if ( (Double) table.getValueAt( i, 2 ) > lengthsSnapshot.getMaximum() )
						{
							lengthsSnapshot.setMaximum( (int) current );
						}
					}
					lengthsSnapshot.setLabelTable( labels );
					lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
				}
				else
				{
					lengthsSnapshot.setMinimum( 0 );
					lengthsSnapshot.setMaximum( 0 );
					labels.put( 0, new JLabel( "0" ) );
					lengthsSnapshot.setLabelTable( labels );
					lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
				}
			}
			else
			{
				output.setText( "No row selected" );
			}

			break;

		case "View Result":
			output.setText( "" );
			model.setOutput( "" );
			row = table.getSelectedRow();
			if ( row != -1 )
			{
				String text = tableModel.getValueAt( row, 1 ).toString();
				String result = textsAndExplanation.get( text );
				textInput.setText( text );
				output.setText( result );
			}
			else
			{
				output.setText( "No row selected" );
			}

			break;

		case "Load":
			keyWord = textInput.getText();
			textInput.setText( model.cleanWikiText( model.keyWordText( textInput.getText() ) ) );
			break;

		case "Clear":
			textInput.setText( "" );
			patternInput.setText( "" );
			pb.setValue( 0 );
			pl.setText( "" );
			output.setText( "" );
			break;

		case "Undo Table":
			if ( !deletedSingleRows.isEmpty() )
			{
				while ( !deletedSingleRows.isEmpty() )
				{
					tableModel.addRow( deletedSingleRows.pop() );

					for ( int i = 0; i < table.getRowCount(); i++ )
					{
						double current = (Double) table.getValueAt( i, 2 );
						if ( lowestLength == 0 )
						{
							lowestLength = current;
						}
						else if ( current < lowestLength )
						{
							lowestLength = current;
						}
						if ( i == 0 )
						{
							lengthsSnapshot.setMinimum( (int) current );
							lengthsSnapshot.setMaximum( (int) current );
						}
						labels.put( (int) current, new JLabel( String.valueOf( (int) current ) ) );
						if ( (Double) table.getValueAt( i, 2 ) < lengthsSnapshot.getMinimum() )
						{
							lengthsSnapshot.setMinimum( (int) current );
						}
						else if ( (Double) table.getValueAt( i, 2 ) > lengthsSnapshot.getMaximum() )
						{
							lengthsSnapshot.setMaximum( (int) current );
						}
					}
					lengthsSnapshot.setLabelTable( labels );
				}
			}
			else
			{
				output.setText( "You haven't cleared the table yet!" );
			}

			break;

		case "Undo Row":
			if ( !deletedSingleRows.isEmpty() )
			{
				tableModel.addRow( deletedSingleRows.pop() );

				if ( table.getRowCount() >= 1 )
				{
					for ( int i = 0; i < table.getRowCount(); i++ )
					{
						double current = (Double) table.getValueAt( i, 2 );
						if ( lowestLength == 0 )
						{
							lowestLength = current;
						}
						else if ( current < lowestLength )
						{
							lowestLength = current;
						}
						if ( i == 0 )
						{
							lengthsSnapshot.setMinimum( (int) current );
							lengthsSnapshot.setMaximum( (int) current );
						}
						labels.put( (int) current, new JLabel( String.valueOf( (int) current ) ) );
						if ( (Double) table.getValueAt( i, 2 ) < lengthsSnapshot.getMinimum() )
						{
							lengthsSnapshot.setMinimum( (int) current );
						}
						else if ( (Double) table.getValueAt( i, 2 ) > lengthsSnapshot.getMaximum() )
						{
							lengthsSnapshot.setMaximum( (int) current );
						}
					}
					lengthsSnapshot.setLabelTable( labels );
					lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
				}
				else
				{
					lengthsSnapshot.setMinimum( 0 );
					lengthsSnapshot.setMaximum( 0 );
					labels.put( 0, new JLabel( "0" ) );
					lengthsSnapshot.setLabelTable( labels );
					lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
				}
			}
			else
			{
				output.setText( "You haven't deleted any rows!" );
			}

			break;

		default:
			break;
		}
	}
}
