package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.MainMenuListener;
import model.Model;

public class MainMenu
{
	private JFrame frame;
	private Model model;
	private ActionListener listener;

	private JButton factorComplexity, unorderedPatterns, orderedPatterns, morphisms, calculateComplexity, compareTexts;

	private Font font;

	public MainMenu( Model m )
	{
		this.model = m;
	}

	public void createGUI()
	{
		frame = new JFrame( "Complexity of Texts" );
		frame.setSize( 400, 400 );
		font = new Font( "Arial", Font.BOLD, 20 );
		listener = new MainMenuListener( model );

		createButtons();

		JPanel panel = new JPanel( new GridLayout( 0, 1 ) );
		panel.setSize( 400, 350 );
		panel.add( factorComplexity );
		panel.add( orderedPatterns );
		panel.add( unorderedPatterns );
		panel.add( morphisms );
		panel.add( calculateComplexity );
		panel.add( compareTexts );

		for ( Component c : panel.getComponents() )
		{
			styleButton( (JButton) c );
		}

		frame.add( panel );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
	}

	public void createButtons()
	{
		factorComplexity = new JButton( "Factor Complexity" );
		factorComplexity.addActionListener( listener );
		factorComplexity
				.setToolTipText( "This produces the factor complexity of the string you enter up to a set limit" );

		orderedPatterns = new JButton( "Ordered Patterns" );
		orderedPatterns.addActionListener( listener );
		orderedPatterns
				.setToolTipText( "This will find all the ordered patterns you specify in the input you entered" );

		unorderedPatterns = new JButton( "Unordered Patterns" );
		unorderedPatterns.addActionListener( listener );
		unorderedPatterns
				.setToolTipText( "This will find all the unordered patterns you specify in the input you entered" );

		morphisms = new JButton( "Generate Morphisms" );
		morphisms.addActionListener( listener );

		calculateComplexity = new JButton( "Calculate Complexity" );
		calculateComplexity.addActionListener( listener );
		calculateComplexity.setToolTipText(
				"This will produce a complexity score for a text based on Factor Complexity and Patterns found" );

		compareTexts = new JButton( "Compare Texts" );
		compareTexts.addActionListener( listener );
		compareTexts.setToolTipText(
				"This will allow you to compare the complexity values of 2 or more texts and determine which one is the most complex" );
	}

	private void styleButton( JButton button )
	{
		button.setFont( font );
		button.setBackground( new Color( 50, 50, 50 ) );

		Runnable runenr = new Runnable()
		{

			@Override
			public void run()
			{
				int mode = 0;
				int r = 255;
				int g = 0;
				int b = 0;
				while ( true )
				{
					if ( mode == 0 )
					{
						g++;
						if ( g == 255 )
							mode = 1;
					}
					else if ( mode == 1 )
					{
						r--;
						if ( r == 0 )
							mode = 2;
					}
					else if ( mode == 2 )
					{
						b++;
						if ( b == 255 )
							mode = 3;
					}
					else if ( mode == 3 )
					{
						g--;
						if ( g == 0 )
							mode = 4;
					}
					else if ( mode == 4 )
					{
						if ( r == 255 )
						{
							b--;
							if ( b == 0 )
								mode = 0;
						}
						else
							r++;
					}
					try
					{

						button.setForeground( new Color( r, g, b ) );
						Thread.sleep( 50 );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace();
					}
				}

			}
		};

		Thread t = new Thread( runenr );
		t.start();

		button.setBorder( BorderFactory.createLineBorder( new Color( 125, 125, 125 ) ) );
		button.setFocusPainted( false );
	}
}
