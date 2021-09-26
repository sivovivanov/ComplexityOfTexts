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
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controller.CalculateComplexityListener;
import model.Model;

public class CalculateComplexity implements Module
{
	private JFrame frame;
	private Model model;
	private ActionListener listener;

	private JButton calculate, load, browse, clear;
	private JRadioButton textIn, browseIn, xx, xy, xxx, xyx;
	private JTextArea output, textInput;

	private JTextField patternInput;

	private JLabel enterText, enterPattern, enterLimit, enterModulo, progressLabel;

	private JCheckBox defaultCheck, keyWordCheck;

	private Font font;

	private JScrollPane spIn, spOut;

	private SpinnerModel valueLimit, valueMod;

	private JSpinner limit, modulo;

	private JProgressBar progressBar;

	public CalculateComplexity( Model m )
	{
		this.model = m;
	}

	@Override
	public void createGUI()
	{
		frame = new JFrame( "Calculate Complexity" );
		frame.setSize( 650, 650 );

		font = new Font( "Arial", Font.BOLD, 15 );

		textInput = new JTextArea();
		patternInput = new JTextField();

		output = new JTextArea();

		spIn = new JScrollPane();
		spIn.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		spOut = new JScrollPane();
		spOut.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		enterText = new JLabel( "Enter input:" );
		enterLimit = new JLabel( "Enter limit:" );
		enterPattern = new JLabel( "Enter pettern:" );
		enterModulo = new JLabel( "Enter modulo:" );
		progressLabel = new JLabel( "" );

		progressBar = new JProgressBar( 0, 100 );
		progressBar.setValue( 0 );
		progressBar.setStringPainted( true );
		progressBar.setFont( font );

		valueLimit = new SpinnerNumberModel( 0, 0, 100, 1 );
		valueMod = new SpinnerNumberModel( 1, 1, 100, 1 );

		limit = new JSpinner( valueLimit );
		modulo = new JSpinner( valueMod );

		listener = new CalculateComplexityListener( model, textInput, patternInput, limit, modulo, output, progressBar,
				progressLabel );

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
		patternInput.setBounds( 95, 250, size.width + 366, size.height );

		panel.add( textIn );
		size = textIn.getPreferredSize();
		textIn.setBounds( 480, 25, size.width, size.height );
		panel.add( browseIn );
		size = browseIn.getPreferredSize();
		browseIn.setBounds( 480, 50, size.width, size.height );

		panel.add( keyWordCheck );
		size = keyWordCheck.getPreferredSize();
		keyWordCheck.setBounds( 565, 50, size.width, size.height );
		panel.add( load );
		size = load.getPreferredSize();
		load.setBounds( 570, 80, size.width, size.height );

		panel.add( browse );
		size = browse.getPreferredSize();
		browse.setBounds( 480, 80, size.width, size.height );

		panel.add( enterLimit );
		size = enterLimit.getPreferredSize();
		enterLimit.setBounds( 480, 120, size.width, size.height );
		panel.add( limit );
		size = limit.getPreferredSize();
		limit.setBounds( 565, 120, size.width, size.height );

		panel.add( enterModulo );
		size = enterModulo.getPreferredSize();
		enterModulo.setBounds( 480, 150, size.width, size.height );
		panel.add( modulo );
		size = modulo.getPreferredSize();
		modulo.setBounds( 565, 150, size.width, size.height );

		panel.add( defaultCheck );
		size = defaultCheck.getPreferredSize();
		defaultCheck.setBounds( 475, 170, size.width, size.height );
		panel.add( xx );
		size = xx.getPreferredSize();
		xx.setBounds( 465, 200, size.width, size.height );
		panel.add( xy );
		size = xy.getPreferredSize();
		xy.setBounds( 505, 200, size.width, size.height );
		panel.add( xxx );
		size = xxx.getPreferredSize();
		xxx.setBounds( 545, 200, size.width, size.height );
		panel.add( xyx );
		size = xyx.getPreferredSize();
		xyx.setBounds( 595, 200, size.width, size.height );

		panel.add( calculate );
		size = calculate.getPreferredSize();
		calculate.setBounds( 480, 250, size.width, size.height );
		panel.add( clear );
		size = clear.getPreferredSize();
		clear.setBounds( 570, 250, size.width, size.height );

		panel.add( progressLabel );
		size = progressLabel.getPreferredSize();
		progressLabel.setBounds( 15, 270, 400, 25 );
		panel.add( progressBar );
		size = progressBar.getPreferredSize();
		progressBar.setBounds( 15, 300, size.width + 465, size.height + 20 );

		panel.add( spOut );
		size = output.getPreferredSize();
		spOut.setBounds( 15, 350, size.width + 615, size.height + 240 );
		spOut.getViewport().add( output );
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
		calculate = new JButton( "Calculate" );
		calculate.addActionListener( listener );

		load = new JButton( "Load" );
		load.setEnabled( false );
		load.addActionListener( listener );

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
					textInput.setEditable( true );
				}
				else
				{
					browse.setEnabled( true );
					browseIn.setSelected( true );
					textInput.setEditable( false );
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
					textInput.setEditable( false );
					browse.setEnabled( true );
					textIn.setSelected( false );
				}
				else
				{
					browse.setEnabled( false );
					textIn.setSelected( true );
					textInput.setEditable( true );
				}
			}
		}) );

		keyWordCheck = new JCheckBox( "Key Word" );
		keyWordCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( keyWordCheck.isSelected() )
				{
					load.setEnabled( true );
					textIn.setEnabled( false );
					textIn.setSelected( true );
					browseIn.setEnabled( false );
					browse.setEnabled( false );
				}
				else
				{
					textInput.setText( "" );
					load.setEnabled( false );
					textIn.setEnabled( true );
					textIn.setSelected( true );
					browseIn.setEnabled( true );
					browse.setEnabled( false );
				}
			}
		}) );

		defaultCheck = new JCheckBox( "Default Pattern" );
		defaultCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( defaultCheck.isSelected() )
				{
					xx.setEnabled( true );
					xx.setSelected( true );
					xy.setEnabled( true );
					xxx.setEnabled( true );
					xyx.setEnabled( true );
				}
				else
				{
					xx.setEnabled( false );
					xx.setSelected( false );
					xy.setEnabled( false );
					xy.setSelected( false );
					xxx.setEnabled( false );
					xxx.setSelected( false );
					xyx.setEnabled( false );
					xyx.setSelected( false );
					patternInput.setText( "" );
				}
			}
		}) );

		xx = new JRadioButton( "XX" );
		xx.setEnabled( false );
		xx.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( xx.isSelected() )
				{
					xy.setSelected( false );
					xxx.setSelected( false );
					xyx.setSelected( false );
					patternInput.setText( xx.getText() );
				}
			}
		}) );
		xy = new JRadioButton( "XY" );
		xy.setEnabled( false );
		xy.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( xy.isSelected() )
				{
					xx.setSelected( false );
					xxx.setSelected( false );
					xyx.setSelected( false );
					patternInput.setText( xy.getText() );
				}
			}
		}) );
		xxx = new JRadioButton( "XXX" );
		xxx.setEnabled( false );
		xxx.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( xxx.isSelected() )
				{
					xx.setSelected( false );
					xy.setSelected( false );
					xyx.setSelected( false );
					patternInput.setText( xxx.getText() );
				}
			}
		}) );
		xyx = new JRadioButton( "XYX" );
		xyx.setEnabled( false );
		xyx.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( xyx.isSelected() )
				{
					xx.setSelected( false );
					xy.setSelected( false );
					xxx.setSelected( false );
					patternInput.setText( xyx.getText() );
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
