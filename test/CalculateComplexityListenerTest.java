package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.CalculateComplexityListener;
import model.Model;

public class CalculateComplexityListenerTest
{
	private ActionListener al;
	private Model m;
	private ActionEvent action;
	private JTextField patternInput;
	private JTextArea textInput;
	private JSpinner limit, modulo;
	private SpinnerNumberModel limitValue, moduloValue;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
		textInput = new JTextArea( "Livingstone Tower" );
		patternInput = new JTextField( "xx" );
		limitValue = new SpinnerNumberModel( 0, 0, 100, 1 );
		moduloValue = new SpinnerNumberModel( 0, 0, 100, 1 );
		limit = new JSpinner( limitValue );
		modulo = new JSpinner( moduloValue );
		action = new ActionEvent( new JButton(), 0, "Calculate" );
		al = new CalculateComplexityListener( m, textInput, patternInput, limit, modulo, new JTextArea(),
				new JProgressBar(), new JLabel() );
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		al = null;
		m = null;
	}

	@Test
	public void testCalculate()
	{
		// Testing Calculate with limiter and without
		limit.setValue( 2 );
		modulo.setValue( 2 );
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
