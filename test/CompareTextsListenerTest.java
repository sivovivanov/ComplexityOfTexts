package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.CompareTextsListener;
import model.Model;

public class CompareTextsListenerTest
{
	private ActionListener al;
	private Model m;
	private ActionEvent action;
	private JTextField patternInput;
	private JTextArea textInput;
	private JSpinner limit, modulo, patternLength;
	private SpinnerNumberModel limitValue, moduloValue, patternLengthValue;
	private DefaultTableModel tm;
	private JTable table;
	private ArrayList<JCheckBox> checkBoxes;
	private JSlider lengthsSnapshot;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
		textInput = new JTextArea( "" );
		patternInput = new JTextField( "xx" );
		limitValue = new SpinnerNumberModel( 0, 0, 100, 1 );
		moduloValue = new SpinnerNumberModel( 0, 0, 100, 1 );
		patternLengthValue = new SpinnerNumberModel( 0, 0, 100, 1 );
		limit = new JSpinner( limitValue );
		modulo = new JSpinner( moduloValue );
		patternLengthValue.setValue( 5 );
		limit.setValue( 2 );
		modulo.setValue( 2 );
		patternLength = new JSpinner( patternLengthValue );
		tm = new DefaultTableModel();
		tm.addColumn( "key" );
		tm.addColumn( "text" );
		tm.addColumn( "len" );
		tm.addColumn( "comp" );
		table = new JTable( tm );
		action = new ActionEvent( new JButton(), 0, "Arrow" );
		checkBoxes = new ArrayList<>();
		lengthsSnapshot = new JSlider( JSlider.HORIZONTAL, 0, 0, 0 );
		lengthsSnapshot.setMinorTickSpacing( 1 );
		lengthsSnapshot.setMajorTickSpacing( 2 );
		lengthsSnapshot.setPaintLabels( true );
		lengthsSnapshot.setPaintTicks( true );
		lengthsSnapshot.setSnapToTicks( true );
		lengthsSnapshot.setEnabled( false );
		for ( int i = 0; i < 6; i++ )
		{
			JCheckBox temp = new JCheckBox();
			temp.setSelected( true );
			checkBoxes.add( temp );
		}
		al = new CompareTextsListener( m, textInput, patternInput, limit, modulo, patternLength, tm, table, checkBoxes,
				lengthsSnapshot, new JTextArea(), new JProgressBar(), new JLabel() );
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		al = null;
		m = null;
	}

	@Test
	public void testAddCalculateAndViewChart()
	{
		// Testing Calculate with limiter and without
		textInput.setText( "cat" );
		al.actionPerformed( action );
		textInput.setText( "dog" );
		al.actionPerformed( action );
		textInput.setText( "car" );
		al.actionPerformed( action );

		try
		{
			action = new ActionEvent( new JButton(), 0, "Calculate" );
			al.actionPerformed( action );
			Thread.sleep( 50 );
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action = new ActionEvent( new JButton(), 0, "View Chart" );
		al.actionPerformed( action );
	}

	@Test
	public void testAddClearAndUndo()
	{
		textInput.setText( "cat" );
		al.actionPerformed( action );
		textInput.setText( "dog" );
		al.actionPerformed( action );
		textInput.setText( "car" );
		al.actionPerformed( action );

		action = new ActionEvent( new JButton(), 0, "Clear Table" );
		al.actionPerformed( action );
		action = new ActionEvent( new JButton(), 0, "Undo Row" );
		al.actionPerformed( action );
		action = new ActionEvent( new JButton(), 0, "Undo Table" );
		al.actionPerformed( action );
	}

	@Test
	public void testAddViewResultAndDeleteRow()
	{
		textInput.setText( "cat" );
		al.actionPerformed( action );
		textInput.setText( "dog" );
		al.actionPerformed( action );
		textInput.setText( "car" );
		al.actionPerformed( action );

		table.setRowSelectionInterval( 0, 0 );
		action = new ActionEvent( new JButton(), 0, "View Result" );
		al.actionPerformed( action );
		table.setRowSelectionInterval( 0, 0 );
		action = new ActionEvent( new JButton(), 0, "Delete Row" );
		al.actionPerformed( action );
	}

	@Test
	public void testLoad()
	{
		action = new ActionEvent( new JButton(), 0, "Load" );
		al.actionPerformed( action );
	}

	@Test
	public void testBrowse()
	{
		// Testing Browse
		// Opens up file choose dialog box, hence commented out
		// Test Passes
		// action = new ActionEvent( new JButton(), 0, "Browse..." );
		// al.actionPerformed( action );
	}

	@Test
	public void testClear()
	{
		// Testing Clear
		action = new ActionEvent( new JButton(), 0, "Clear" );
		al.actionPerformed( action );
	}

}
