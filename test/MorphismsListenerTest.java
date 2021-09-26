package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.MorphismsListener;
import model.Model;

public class MorphismsListenerTest
{
	private ActionListener al;
	private Model m;
	private JTextArea textInput1;
	private JTextArea textInput2;
	private ActionEvent action;
	private SpinnerNumberModel value;
	private JSpinner limit;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
		textInput1 = new JTextArea( "0011" );
		textInput2 = new JTextArea( "1100" );
		value = new SpinnerNumberModel( 0, 0, 100, 1 );
		limit = new JSpinner( value );
		action = new ActionEvent( new JButton(), 0, "Generate" );
		al = new MorphismsListener( m, textInput1, textInput2, limit, new JTextArea(), new JProgressBar(),
				new JLabel() );
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		al = null;
		m = null;
	}

	@Test
	public void testGenerate()
	{
		// Testing Generate with limiter and without
		al.actionPerformed( action );
		textInput1 = new JTextArea( "0011a" );
		textInput2 = new JTextArea( "1100b" );
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
