package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.OrderedPatternsListener;
import model.Model;

public class OrderedPatterns implements Module
{

	private JFrame frame;
	private Model model;
	private ActionListener listener;

	private JButton show, browse, clear;
	private JRadioButton textIn, browseIn;
	private JTextField patternInput;

	private JTextArea output, textInput;

	private JLabel enterText, enterPattern;

	private Font font;

	private JScrollPane sp, spIn;

	private JProgressBar progressBar;

	public OrderedPatterns( Model m )
	{
		this.model = m;
	}

	@Override
	public void createGUI()
	{
		frame = new JFrame( "Ordered Patterns" );
		frame.setSize( 650, 650 );
		frame.setBackground( Color.white );

		font = new Font( "Arial", Font.BOLD, 15 );

		textInput = new JTextArea();
		patternInput = new JTextField();
		output = new JTextArea();

		spIn = new JScrollPane();
		spIn.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		enterText = new JLabel( "Enter numeric input:" );
		enterPattern = new JLabel( "Enter numeric pattern:" );

		progressBar = new JProgressBar( 0, 100 );
		progressBar.setValue( 0 );
		progressBar.setStringPainted( true );
		progressBar.setFont( font );

		model.setOrderedPatterns( true );
		listener = new OrderedPatternsListener( model, patternInput, textInput, output, progressBar );

		createButtons();

		JPanel panel = new JPanel();
		panel.setBackground( Color.gray );
		Dimension size = null;

		panel.setLayout( null );

		panel.add( enterText );
		size = enterText.getPreferredSize();
		enterText.setBounds( 15, 5, size.width, size.height );
		panel.add( spIn );
		size = textInput.getPreferredSize();
		textInput.setLineWrap( true );
		spIn.setBounds( 15, 25, size.width + 450, size.height + 200 );
		spIn.getViewport().add( textInput );

		panel.add( enterPattern );
		size = enterPattern.getPreferredSize();
		enterPattern.setBounds( 15, 250, size.width, size.height );
		panel.add( patternInput );
		size = patternInput.getPreferredSize();
		patternInput.setBounds( 150, 250, size.width + 313, size.height );

		panel.add( textIn );
		size = textIn.getPreferredSize();
		textIn.setBounds( 480, 25, size.width, size.height );
		panel.add( browseIn );
		size = browseIn.getPreferredSize();
		browseIn.setBounds( 480, 50, size.width, size.height );

		panel.add( browse );
		size = browse.getPreferredSize();
		browse.setBounds( 480, 80, size.width, size.height );

		panel.add( show );
		size = show.getPreferredSize();
		show.setBounds( 480, 120, size.width, size.height );
		panel.add( clear );
		size = clear.getPreferredSize();
		clear.setBounds( 550, 120, size.width, size.height );

		panel.add( progressBar );
		size = progressBar.getPreferredSize();
		progressBar.setBounds( 15, 300, size.width + 465, size.height + 20 );

		panel.add( sp );
		size = output.getPreferredSize();
		sp.setBounds( 15, 350, size.width + 615, size.height + 240 );
		sp.getViewport().add( output );
		output.setEditable( false );

		for ( Component c : panel.getComponents() )
		{
			if ( c instanceof JButton )
			{
				styleButton( (JButton) c );
			}
			else if ( c instanceof JRadioButton )
			{
				styleRadioButton( (JRadioButton) c );
			}
			else if ( c instanceof JCheckBox )
			{
				styleCheckBox( (JCheckBox) c );
			}
			else if ( c instanceof JTextField )
			{
				styleTextField( (JTextField) c );
			}
			else if ( c instanceof JTextArea )
			{
				styleTextArea( (JTextArea) c );
			}
			else if ( c instanceof JLabel )
			{
				styleLabel( (JLabel) c );
			}
		}

		frame.setResizable( false );
		frame.add( panel );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
	}

	@Override
	public void createButtons()
	{
		show = new JButton( "Show" );
		show.addActionListener( listener );

		browse = new JButton( "Browse..." );
		browse.addActionListener( listener );
		browse.setEnabled( false );

		clear = new JButton( "Clear" );
		clear.addActionListener( listener );

		textIn = new JRadioButton( "Enter text" );
		textIn.setSelected( true );
		textIn.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( textIn.isSelected() )
				{
					browse.setEnabled( false );
					browseIn.setSelected( false );
				}
				else
				{
					browse.setEnabled( true );
					browseIn.setSelected( true );
				}
			}
		}) );

		browseIn = new JRadioButton( "Select file" );
		browseIn.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( browseIn.isSelected() )
				{
					browse.setEnabled( true );
					textIn.setSelected( false );
				}
				else
				{
					browse.setEnabled( false );
					textIn.setSelected( true );
				}
			}
		}) );
	}

	@Override
	public void styleButton( JButton button )
	{
		button.setFont( font );
		button.setBackground( new Color( 50, 50, 50 ) );
		button.setForeground( new Color( 30, 255, 0 ) );
		button.setBorder( BorderFactory.createLineBorder( new Color( 125, 125, 125 ) ) );
		button.setFocusPainted( false );
	}

	@Override
	public void styleTextField( JTextField textField )
	{
		textField.setFont( font );
		textField.setBackground( new Color( 180, 180, 180 ) );
		textField.setForeground( new Color( 0, 0, 0 ) );
		textField.setBorder( BorderFactory.createLineBorder( new Color( 125, 125, 125 ) ) );
	}

	@Override
	public void styleLabel( JLabel label )
	{
		label.setFont( new Font( "Arial", Font.BOLD, 12 ) );
		label.setBackground( new Color( 180, 180, 180 ) );
		label.setForeground( new Color( 0, 0, 0 ) );
	}

	@Override
	public void styleTextArea( JTextArea textArea )
	{
		textArea.setFont( font );
		textArea.setBackground( new Color( 180, 180, 180 ) );
		textArea.setForeground( new Color( 0, 0, 0 ) );
		textArea.setBorder( BorderFactory.createLineBorder( new Color( 125, 125, 125 ) ) );
	}

	@Override
	public void styleRadioButton( JRadioButton rButton )
	{
		rButton.setFont( new Font( "Arial", Font.BOLD, 12 ) );
		rButton.setBackground( Color.GRAY );
		rButton.setForeground( new Color( 0, 0, 0 ) );
	}

	@Override
	public void styleCheckBox( JCheckBox checkBox )
	{
		checkBox.setFont( new Font( "Arial", Font.BOLD, 12 ) );
		checkBox.setBackground( Color.GRAY );
		checkBox.setForeground( new Color( 0, 0, 0 ) );
	}

}
