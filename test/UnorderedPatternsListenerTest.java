package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UnorderedPatternsListener;
import model.Model;

public class UnorderedPatternsListenerTest
{
	private ActionListener al;
	private Model m;
	private JTextArea textInput;
	private JTextField patternInput;
	private ActionEvent action;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
		textInput = new JTextArea( "4312" );
		patternInput = new JTextField( "abc" );
		action = new ActionEvent( new JButton(), 0, "Show" );
		al = new UnorderedPatternsListener( m, patternInput, textInput, new JTextArea(), new JProgressBar());
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		al = null;
		m = null;
	}

	@Test
	public void testShow()
	{
		// Testing Show
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
