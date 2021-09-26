package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.MainMenuListener;
import model.Model;

public class MainMenuListenerTest
{
	private ActionListener al;
	private Model m;
	private ActionEvent action;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
		al = new MainMenuListener( m );
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		al = null;
		m = null;
	}

	@Test
	public void testFactorComplexity()
	{
		action = new ActionEvent( new JButton(), 0, "Factor Complexity" );
		al.actionPerformed( action );
	}

	@Test
	public void testOrderedPatterns()
	{
		action = new ActionEvent( new JButton(), 0, "Ordered Patterns" );
		al.actionPerformed( action );
	}

	@Test
	public void testUnorderedPatterns()
	{
		action = new ActionEvent( new JButton(), 0, "Unordered Patterns" );
		al.actionPerformed( action );
	}

	@Test
	public void testMorphisms()
	{
		action = new ActionEvent( new JButton(), 0, "Generate Morphisms" );
		al.actionPerformed( action );
	}

	@Test
	public void testCalculateComplexity()
	{
		action = new ActionEvent( new JButton(), 0, "Calculate Complexity" );
		al.actionPerformed( action );
	}

	@Test
	public void testCompareTexts()
	{
		action = new ActionEvent( new JButton(), 0, "Compare Texts" );
		al.actionPerformed( action );
	}

}
