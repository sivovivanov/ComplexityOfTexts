package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;

import controller.CompareTextsListener;
import model.Model;

public class CompareTexts implements Module
{
	private JFrame frame;
	private Model model;
	private ActionListener listener;

	private JButton calculate, viewResult, viewChart, load, browse, clear, clearTable, deleteRow, clearResults, undo,
			undoRow;
	private JRadioButton textIn, browseIn, xx, xy, xxx, xyx;
	private BasicArrowButton arrow;
	private JTextArea output, textInput;

	private JTextField patternInput;

	private JLabel enterText, enterPattern, binaryLabel, enterLimit, enterModulo, patternLabel, patternLimitLabel,
			tableLabel, arrowLabel, progressLabel, lengthLabel, sliderLengthValue;

	private JCheckBox defaultCheck, keyWordCheck, vCBinaryCheck, moduloBinaryCheck, factorComplexityCheck,
			unorderedPatternsCheck, orderedPatternsCheck, standardLengthCheck, manageLengthCheck;

	private JTable textsTable;

	private Font font;

	private JScrollPane spIn, spOut, spTable;

	private SpinnerModel valueLimit, valueMod, valuePattern;

	private JSpinner limit, modulo, patternLength;

	private JSlider lengthsSnapshot;

	private JProgressBar progressBar;

	public CompareTexts( Model m )
	{
		this.model = m;
	}

	@Override
	public void createGUI()
	{
		ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
		frame = new JFrame( "Compare Texts" );
		frame.setSize( 1100, 800 );

		font = new Font( "Arial", Font.BOLD, 15 );

		textInput = new JTextArea();
		patternInput = new JTextField();

		output = new JTextArea();

		spIn = new JScrollPane();
		spIn.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		spOut = new JScrollPane();
		spOut.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		spTable = new JScrollPane();
		spTable.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		lengthsSnapshot = new JSlider( JSlider.HORIZONTAL, 0, 0, 0 );
		lengthsSnapshot.setMinorTickSpacing( 1 );
		lengthsSnapshot.setMajorTickSpacing( 2 );
		lengthsSnapshot.setPaintLabels( true );
		lengthsSnapshot.setPaintTicks( true );
		lengthsSnapshot.setSnapToTicks( true );
		lengthsSnapshot.setEnabled( false );

		enterText = new JLabel( "Enter input:" );
		enterLimit = new JLabel( "Enter limit:" );
		enterPattern = new JLabel( "Enter pattern:" );
		binaryLabel = new JLabel( "Select Binary Conversion Type:" );
		enterModulo = new JLabel( "Enter modulo:" );
		patternLabel = new JLabel( "Select Patterns:" );
		patternLimitLabel = new JLabel( "Pattern Limit:" );
		tableLabel = new JLabel( "Sentences to be compared:" );
		arrowLabel = new JLabel( "Save text in table" );
		lengthLabel = new JLabel( "Length of input:" );
		sliderLengthValue = new JLabel( "~" );
		progressLabel = new JLabel( "" );

		DefaultTableModel tableModel = new DefaultTableModel();
		textsTable = new JTable( tableModel );

		tableModel.addColumn( "Key-Word" );
		tableModel.addColumn( "Text" );
		tableModel.addColumn( "Length" );
		tableModel.addColumn( "Complexity" );

		progressBar = new JProgressBar( 0, 100 );
		progressBar.setValue( 0 );
		progressBar.setStringPainted( true );
		progressBar.setFont( font );
		progressBar.setStringPainted( true );
		progressBar.setBorder( BorderFactory.createLineBorder( Color.gray ) );

		valueLimit = new SpinnerNumberModel( 0, 0, 100, 1 );
		valueMod = new SpinnerNumberModel( 1, 1, 100, 1 );
		valuePattern = new SpinnerNumberModel( 5, 1, 5, 1 );

		limit = new JSpinner( valueLimit );
		modulo = new JSpinner( valueMod );
		patternLength = new JSpinner( valuePattern );
		limit.setEnabled( false );
		modulo.setEnabled( false );
		patternLength.setEnabled( true );

		listener = new CompareTextsListener( model, textInput, patternInput, limit, modulo, patternLength, tableModel,
				textsTable, checkBoxes, lengthsSnapshot, output, progressBar, progressLabel );

		createButtons();

		tableModel.addTableModelListener( new TableModelListener()
		{

			public void tableChanged( TableModelEvent e )
			{
				if ( textsTable.getRowCount() >= 1 )
				{
					viewResult.setEnabled( true );
					if ( !manageLengthCheck.isSelected() )
					{
						deleteRow.setEnabled( true );
						clearTable.setEnabled( true );
					}
					clearResults.setEnabled( true );
					calculate.setEnabled( true );
					viewChart.setEnabled( true );
				}
				else if ( textsTable.getRowCount() == 0 )
				{
					viewResult.setEnabled( false );
					deleteRow.setEnabled( false );
					clearTable.setEnabled( false );
					clearResults.setEnabled( false );
					calculate.setEnabled( false );
					viewChart.setEnabled( false );
				}
			}
		} );

		checkBoxes.add( vCBinaryCheck );
		checkBoxes.add( moduloBinaryCheck );
		checkBoxes.add( factorComplexityCheck );
		checkBoxes.add( unorderedPatternsCheck );
		checkBoxes.add( orderedPatternsCheck );
		checkBoxes.add( standardLengthCheck );
		checkBoxes.add( manageLengthCheck );

		JPanel panel = new JPanel();
		panel.setBackground( Color.gray );
		Dimension size = null;

		panel.setLayout( null );

		panel.add( tableLabel );
		size = tableLabel.getPreferredSize();
		tableLabel.setBounds( 15, 5, size.width, size.height );
		panel.add( spTable );
		size = textsTable.getPreferredSize();
		spTable.setBounds( 15, 25, 350, 325 );
		spTable.getViewport().add( textsTable );

		panel.add( enterText );
		size = enterText.getPreferredSize();
		enterText.setBounds( 400, 5, size.width, size.height );
		panel.add( spIn );
		size = textInput.getPreferredSize();
		textInput.setLineWrap( true );
		spIn.setBounds( 400, 25, size.width + 450, size.height + 200 );
		spIn.getViewport().add( textInput );

		panel.add( enterPattern );
		size = enterPattern.getPreferredSize();
		enterPattern.setBounds( 400, 250, size.width, size.height );
		panel.add( patternInput );
		size = patternInput.getPreferredSize();
		patternInput.setBounds( 480, 250, size.width + 366, size.height );

		panel.add( arrowLabel );
		size = arrowLabel.getPreferredSize();
		arrowLabel.setBounds( 400, 280, size.width, size.height );
		panel.add( arrow );
		arrow.setBounds( 400, 300, size.width, size.height + 25 );

		panel.add( viewResult );
		size = viewResult.getPreferredSize();
		viewResult.setBounds( 15, 360, size.width, size.height );
		size = clearResults.getPreferredSize();
		panel.add( clearResults );
		clearResults.setBounds( 15, 390, size.width, size.height );
		panel.add( deleteRow );
		size = deleteRow.getPreferredSize();
		deleteRow.setBounds( 138, 360, size.width, size.height );
		panel.add( undoRow );
		size = undoRow.getPreferredSize();
		undoRow.setBounds( 138, 390, size.width, size.height );
		panel.add( clearTable );
		size = clearTable.getPreferredSize();
		clearTable.setBounds( 270, 360, size.width, size.height );
		panel.add( undo );
		size = undo.getPreferredSize();
		undo.setBounds( 270, 390, size.width, size.height );

		panel.add( defaultCheck );
		size = defaultCheck.getPreferredSize();
		defaultCheck.setBounds( 600, 275, size.width, size.height );
		panel.add( xx );
		size = xx.getPreferredSize();
		xx.setBounds( 575, 295, size.width, size.height );
		panel.add( xy );
		size = xy.getPreferredSize();
		xy.setBounds( 615, 295, size.width, size.height );
		panel.add( xxx );
		size = xxx.getPreferredSize();
		xxx.setBounds( 655, 295, size.width, size.height );
		panel.add( xyx );
		size = xyx.getPreferredSize();
		xyx.setBounds( 705, 295, size.width, size.height );

		panel.add( textIn );
		size = textIn.getPreferredSize();
		textIn.setBounds( 880, 25, size.width, size.height );
		panel.add( browseIn );
		size = browseIn.getPreferredSize();
		browseIn.setBounds( 880, 50, size.width, size.height );
		panel.add( browse );
		size = browse.getPreferredSize();
		browse.setBounds( 880, 80, size.width, size.height );

		panel.add( keyWordCheck );
		size = keyWordCheck.getPreferredSize();
		keyWordCheck.setBounds( 965, 50, size.width, size.height );
		panel.add( load );
		size = load.getPreferredSize();
		load.setBounds( 970, 80, size.width, size.height );

		panel.add( binaryLabel );
		size = binaryLabel.getPreferredSize();
		binaryLabel.setBounds( 880, 120, size.width, size.height );
		panel.add( vCBinaryCheck );
		size = vCBinaryCheck.getPreferredSize();
		vCBinaryCheck.setBounds( 900, 140, size.width, size.height );
		panel.add( moduloBinaryCheck );
		size = moduloBinaryCheck.getPreferredSize();
		moduloBinaryCheck.setBounds( 900, 160, size.width, size.height );
		panel.add( enterModulo );
		size = enterModulo.getPreferredSize();
		enterModulo.setBounds( 905, 190, size.width, size.height );
		panel.add( modulo );
		size = modulo.getPreferredSize();
		modulo.setBounds( 990, 190, size.width, size.height );

		panel.add( factorComplexityCheck );
		size = factorComplexityCheck.getPreferredSize();
		factorComplexityCheck.setBounds( 880, 220, size.width, size.height );
		panel.add( enterLimit );
		size = enterLimit.getPreferredSize();
		enterLimit.setBounds( 905, 250, size.width, size.height );
		panel.add( limit );
		size = limit.getPreferredSize();
		limit.setBounds( 990, 250, size.width, size.height );

		panel.add( patternLabel );
		size = patternLabel.getPreferredSize();
		patternLabel.setBounds( 880, 280, size.width, size.height );
		panel.add( unorderedPatternsCheck );
		size = unorderedPatternsCheck.getPreferredSize();
		unorderedPatternsCheck.setBounds( 900, 300, size.width, size.height );
		panel.add( orderedPatternsCheck );
		size = orderedPatternsCheck.getPreferredSize();
		orderedPatternsCheck.setBounds( 900, 320, size.width, size.height );
		panel.add( patternLimitLabel );
		size = patternLimitLabel.getPreferredSize();
		patternLimitLabel.setBounds( 905, 350, size.width, size.height );
		panel.add( patternLength );
		size = patternLength.getPreferredSize();
		patternLength.setBounds( 990, 350, size.width + 15, size.height );

		panel.add( lengthLabel );
		size = lengthLabel.getPreferredSize();
		lengthLabel.setBounds( 880, 380, size.width, size.height );
		panel.add( sliderLengthValue );
		size = sliderLengthValue.getPreferredSize();
		sliderLengthValue.setBounds( 970, 380, size.width + 50, size.height );
		panel.add( standardLengthCheck );
		size = standardLengthCheck.getPreferredSize();
		standardLengthCheck.setBounds( 900, 400, size.width, size.height );
		panel.add( manageLengthCheck );
		size = manageLengthCheck.getPreferredSize();
		manageLengthCheck.setBounds( 900, 420, size.width, size.height );
		panel.add( lengthsSnapshot );
		size = lengthsSnapshot.getPreferredSize();
		lengthsSnapshot.setBounds( 880, 450, size.width, size.height );

		panel.add( calculate );
		size = calculate.getPreferredSize();
		calculate.setBounds( 540, 360, size.width, size.height + 10 );
		panel.add( viewChart );
		size = viewChart.getPreferredSize();
		viewChart.setBounds( 635, 360, size.width, size.height + 10 );
		panel.add( clear );
		size = clear.getPreferredSize();
		clear.setBounds( 740, 360, size.width, size.height + 10 );

		panel.add( progressLabel );
		size = progressLabel.getPreferredSize();
		progressLabel.setBounds( 15, 470, 400, 25 );
		panel.add( progressBar );
		size = progressBar.getPreferredSize();
		progressBar.setBounds( 15, 425, size.width + 690, size.height + 20 ); // 917 width

		panel.add( spOut );
		size = output.getPreferredSize();
		spOut.setBounds( 15, 500, size.width + 1065, size.height + 240 );
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
			else if ( c instanceof JSlider )
			{
				styleSlider( (JSlider) c );
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
		arrow = new BasicArrowButton( BasicArrowButton.WEST, null, null, Color.green, null );
		arrow.setText( "Arrow" );
		arrow.addActionListener( listener );

		calculate = new JButton( "Calculate" );
		calculate.addActionListener( listener );
		calculate.setEnabled( false );

		viewChart = new JButton( "View Chart" );
		viewChart.addActionListener( listener );
		viewChart.setEnabled( false );

		viewResult = new JButton( "View Result" );
		viewResult.addActionListener( listener );
		viewResult.setEnabled( false );

		load = new JButton( "Load" );
		load.setEnabled( false );
		load.addActionListener( listener );

		browse = new JButton( "Browse..." );
		browse.addActionListener( listener );
		browse.setEnabled( false );

		clear = new JButton( "Clear" );
		clear.addActionListener( listener );

		clearResults = new JButton( "Clear Results" );
		clearResults.addActionListener( listener );
		clearResults.setEnabled( false );

		undo = new JButton( "Undo Table" );
		undo.addActionListener( listener );
		undo.setEnabled( false );

		undoRow = new JButton( "Undo Row" );
		undoRow.addActionListener( listener );
		undoRow.setEnabled( false );

		clearTable = new JButton( "Clear Table" );
		clearTable.addActionListener( listener );
		clearTable.setEnabled( false );
		clearTable.addActionListener( (new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				undo.setEnabled( true );
				undoRow.setEnabled( true );
			}
		}) );
		deleteRow = new JButton( "Delete Row" );
		deleteRow.addActionListener( listener );
		deleteRow.setEnabled( false );
		deleteRow.addActionListener( (new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				undo.setEnabled( true );
				undoRow.setEnabled( true );
			}
		}) );

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

		vCBinaryCheck = new JCheckBox( "Vowel and Consonant" );
		vCBinaryCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( vCBinaryCheck.isSelected() || moduloBinaryCheck.isSelected() )
				{
					factorComplexityCheck.setEnabled( true );
					factorComplexityCheck.setEnabled( true );
					unorderedPatternsCheck.setEnabled( true );
					orderedPatternsCheck.setEnabled( true );
				}
				else
				{
					factorComplexityCheck.setEnabled( false );
					factorComplexityCheck.setEnabled( false );
					unorderedPatternsCheck.setEnabled( false );
					orderedPatternsCheck.setEnabled( false );
				}
			}
		}) );
		moduloBinaryCheck = new JCheckBox( "Modulo" );
		moduloBinaryCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( moduloBinaryCheck.isSelected() || vCBinaryCheck.isSelected() )
				{
					modulo.setEnabled( true );
					factorComplexityCheck.setEnabled( true );
					factorComplexityCheck.setEnabled( true );
					unorderedPatternsCheck.setEnabled( true );
					orderedPatternsCheck.setEnabled( true );
				}
				else
				{
					modulo.setEnabled( false );
					factorComplexityCheck.setEnabled( false );
					factorComplexityCheck.setEnabled( false );
					unorderedPatternsCheck.setEnabled( false );
					orderedPatternsCheck.setEnabled( false );
				}
			}
		}) );
		factorComplexityCheck = new JCheckBox( "Factor Complexity" );
		factorComplexityCheck.setEnabled( false );
		factorComplexityCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( factorComplexityCheck.isSelected() )
				{
					limit.setEnabled( true );
				}
				else
				{
					limit.setEnabled( false );
				}
			}
		}) );
		unorderedPatternsCheck = new JCheckBox( "Unordered" );
		unorderedPatternsCheck.setEnabled( false );
		unorderedPatternsCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( unorderedPatternsCheck.isSelected() && !orderedPatternsCheck.isSelected() )
				{
					defaultCheck.setEnabled( true );
				}
				else if ( unorderedPatternsCheck.isSelected() && orderedPatternsCheck.isSelected() )
				{
					defaultCheck.setEnabled( false );
					xx.setEnabled( false );
					xy.setEnabled( false );
					xyx.setEnabled( false );
					xxx.setEnabled( false );
				}
				else
				{
					defaultCheck.setEnabled( false );
					defaultCheck.setSelected( false );
				}
			}
		}) );
		orderedPatternsCheck = new JCheckBox( "Ordered" );
		orderedPatternsCheck.setEnabled( false );
		orderedPatternsCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( orderedPatternsCheck.isSelected() )
				{
					defaultCheck.setEnabled( false );
					patternLength.setEnabled( true );
					xx.setEnabled( false );
					xy.setEnabled( false );
					xyx.setEnabled( false );
					xxx.setEnabled( false );
				}
				else if ( !orderedPatternsCheck.isSelected() && unorderedPatternsCheck.isSelected() )
				{
					defaultCheck.setEnabled( true );
				}
			}
		}) );

		standardLengthCheck = new JCheckBox( "Standard Lengths" );
		standardLengthCheck.setSelected( true );
		standardLengthCheck.addItemListener( (new ItemListener()
		{
			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( standardLengthCheck.isSelected() )
				{
					manageLengthCheck.setSelected( false );
					lengthsSnapshot.setEnabled( false );
					lengthsSnapshot.setValue( lengthsSnapshot.getMinimum() );
				}
				else
				{
					manageLengthCheck.setSelected( true );
					lengthsSnapshot.setEnabled( true );
				}
			}
		}) );

		manageLengthCheck = new JCheckBox( "Manage Lengths" );
		manageLengthCheck.addItemListener( (new ItemListener()
		{
			boolean deletionOccured = false;

			@Override
			public void itemStateChanged( ItemEvent arg0 )
			{
				if ( manageLengthCheck.isSelected() )
				{
					sliderLengthValue.setText( String.valueOf( lengthsSnapshot.getMinimum() ) );
					for ( int i = 0; i < textsTable.getRowCount(); i++ )
					{
						textsTable.setValueAt( (double) lengthsSnapshot.getValue(), i, 2 );
					}
					standardLengthCheck.setSelected( false );
					lengthsSnapshot.setEnabled( true );
					sliderLengthValue.setEnabled( true );
					arrow.setEnabled( false );
					deleteRow.setEnabled( false );
					clearTable.setEnabled( false );
					if ( undo.isEnabled() && undoRow.isEnabled() )
					{
						undo.setEnabled( false );
						undoRow.setEnabled( false );
						deletionOccured = true;
					}
				}
				else
				{
					for ( int i = 0; i < textsTable.getRowCount(); i++ )
					{
						textsTable.setValueAt( model.characterCount( textsTable.getValueAt( i, 1 ).toString() ), i, 2 );
					}
					standardLengthCheck.setSelected( true );
					lengthsSnapshot.setEnabled( false );
					sliderLengthValue.setEnabled( false );
					sliderLengthValue.setText( "" );
					arrow.setEnabled( true );
					if ( textsTable.getRowCount() >= 1 )
					{
						deleteRow.setEnabled( true );
						clearTable.setEnabled( true );
					}
					if ( deletionOccured )
					{
						undo.setEnabled( true );
						undoRow.setEnabled( true );
						deletionOccured = false;
					}
				}
			}
		}) );

		lengthsSnapshot.addChangeListener( (new ChangeListener()
		{

			@Override
			public void stateChanged( ChangeEvent e )
			{
				if ( manageLengthCheck.isSelected() )
				{
					sliderLengthValue.setText( String.valueOf( lengthsSnapshot.getValue() ) );
					for ( int i = 0; i < textsTable.getRowCount(); i++ )
					{
						textsTable.setValueAt( (double) lengthsSnapshot.getValue(), i, 2 );
					}
				}
			}
		}) );

		defaultCheck = new JCheckBox( "Default Pattern" );
		defaultCheck.setEnabled( false );
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

	public void styleSlider( JSlider slider )
	{
		slider.setFont( new Font( "Arial", Font.BOLD, 12 ) );
		slider.setBackground( Color.GRAY );
		slider.setForeground( new Color( 0, 0, 0 ) );
	}

}
