package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class LineChart extends JPanel implements ActionListener
{
	private static final int WIDTH = 800; // 750
	private static final int HEIGHT = 600; // 600
	private static final int NOTCH_COUNT = 5;
	private static DecimalFormat format = new DecimalFormat( "#.##" );
	private int xPos1 = 50;
	private int yPos1 = 550;
	private int xPos2 = 50;
	private int yPos2 = 50;

	private double highestLength;
	private double lowestLength;
	private double originalHighestLength;
	private double highestScore;
	private double lowestScore;
	private double originalHighestScore;

	private JTable table;

	private HashMap<Integer, ArrayList<Double>> rowAndComplexity;
	private HashMap<Integer, ArrayList<Double>> rowAndLength;
	private HashMap<Double, Double> complexityAndLength; // IF YOU HAVE 2 of the same complexity or length, obtaining
															// lengh for x coordinate is issue
	private HashMap<Integer, Color> rowAndColor;
	private HashMap<Color, ArrayList<Point>> points;

	private int lowestNotch;

	private JTable textSelect;
	private JScrollPane spTable;
	private JButton view;
	private JLabel textComplexity, textLength, pointsNumber, maxLen, maxComp, complexityLabel, lengthLabel, pointsLabel,
			maxComplexityLabel, maxLengthLabel;

	public LineChart( double highestLength, double highestScore, double lowestLength, double lowestScore, JTable table,
			HashMap<Integer, ArrayList<Double>> rowAndComplexity, HashMap<Integer, ArrayList<Double>> rowAndLength,
			HashMap<Double, Double> complexityAndLength, HashMap<Integer, Color> rowAndColor, int lowestNotch )
	{
		setLayout( null );
		this.highestLength = highestLength;
		this.originalHighestLength = highestLength;
		this.highestScore = highestScore;
		this.originalHighestScore = highestScore;
		this.lowestLength = lowestLength;
		this.lowestScore = lowestScore;
		this.table = table;
		this.rowAndComplexity = rowAndComplexity;
		this.rowAndLength = rowAndLength;
		this.complexityAndLength = complexityAndLength;
		this.rowAndColor = rowAndColor;
		this.lowestNotch = lowestNotch;
		createGUI();
		expandValues();
	}

	private void expandValues()
	{
		while ( this.highestScore % 5 != 0 )
		{
			this.highestScore = Double.valueOf( format.format( this.highestScore ) );
			this.highestScore += 0.01;
		}
		while ( this.highestLength % 5 != 0 )
		{
			this.highestLength = Double.valueOf( format.format( this.highestLength ) );
			this.highestLength += 0.01;
		}
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		xPos1 = 50;
		yPos1 = 550;
		xPos2 = 50;
		yPos2 = 50;
		super.paintComponent( g );
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		ArrayList<Double> notchesComplexity = new ArrayList<>();
		ArrayList<Double> notchesLength = new ArrayList<>();
		g2.setColor( Color.WHITE );
		g2.fillRect( 50, 50, 500, 500 );
		g2.setColor( Color.BLACK );

		// y
		g2.drawLine( xPos1, yPos1, xPos2, yPos2 );
		xPos2 = 550; // 700
		yPos2 = 550;
		// x
		g2.drawLine( xPos1, yPos1, xPos2, yPos2 );

		double notch = 0;
		double fifth;
		if ( table.getRowCount() == 1 )
		{
			fifth = highestScore / 5;
			// Calculating values for Y
			while ( notch <= highestScore )
			{

				notchesComplexity.add( notch );
				notch += fifth;
			}
			fifth = highestLength / 5;
			notch = 0;
			// Calculating values for X
			while ( notch <= highestLength )
			{
				notchesLength.add( notch );
				notch += fifth;
			}
		}
		else
		{
			double lengthDiff = highestLength - lowestLength;
			double scoreDiff = highestScore - lowestScore;
			int times = 0;

			fifth = lengthDiff / 5;
			notch = lowestLength;
			while ( times <= 5 )
			{
				notch = Double.valueOf( format.format( notch ) );
				notchesLength.add( notch );
				notch += fifth;
				times++;
			}
			times = 0;
			fifth = scoreDiff / 5;
			notch = lowestScore;
			while ( times <= 5 )
			{
				notch = Double.valueOf( format.format( notch ) );
				notchesComplexity.add( notch );
				notch += fifth;
				times++;
			}
		}

		yPos1 = 50;
		// Drawing vertical grey lines
		for ( int i = NOTCH_COUNT * 2; i >= 0; i-- )
		{
			xPos2 = xPos1 + 10;
			yPos2 = yPos1;
			if ( i != 0 )
			{
				g2.setColor( Color.lightGray );
				g2.drawLine( xPos1, yPos1, 550, yPos2 );
				g2.setColor( Color.black );
			}
			yPos1 += 50;
		}

		yPos1 = 50;
		// Placing notches and values for Complexity Y
		// -1 is the addition
		for ( int i = NOTCH_COUNT; i >= 0; i-- )
		{
			xPos2 = xPos1 + 10;
			yPos2 = yPos1;
			g2.drawString( format.format( notchesComplexity.get( i ) ), (xPos1 - 30), yPos1 );
			g2.drawLine( xPos1, yPos1, xPos2, yPos2 );
			yPos1 += 100;
		}

		xPos1 = 50;
		yPos1 = getHeight() - 50;
		// Drawing horizontal grey lines
		int j = 0;
		for ( int i = 0; i < (NOTCH_COUNT + 1) * 2; i++ )
		{
			xPos2 = xPos1;
			yPos2 = yPos1 - 10;
			if ( i != 0 )
			{
				g2.setColor( Color.lightGray );
				g2.drawLine( xPos1, yPos1, xPos2, 50 );
				g2.setColor( Color.black );
			}
			xPos1 += 50;
			j++;
		}

		xPos1 = 50;
		yPos1 = getHeight() - 50;
		// Placing notches and values for Length X
		j = 0;
		for ( int i = 0; i < NOTCH_COUNT + 1; i++ )
		{
			xPos2 = xPos1;
			yPos2 = yPos1 - 10;
			g2.drawString( format.format( notchesLength.get( j ) ), xPos1, yPos1 + 30 );
			g2.drawLine( xPos1, yPos1, xPos2, yPos2 );
			xPos1 += 100;
			j++;
		}

		points = new HashMap<>();
		for ( Integer row : rowAndComplexity.keySet() )
		{
			// for ( Double complexity : rowAndComplexity.get( row ) )
			for ( int i = 0; i < rowAndComplexity.get( row ).size(); i++ )
			{
				double complexity = rowAndComplexity.get( row ).get( i );
				double length = rowAndLength.get( row ).get( i );
				Color c = rowAndColor.get( row );
				ArrayList<Point> previousPoints;
				double pcent = 0.0;
				if ( table.getRowCount() == 1 )
				{
					pcent = (complexity / highestScore) * 100;
				}
				else
				{
					pcent = ((complexity - lowestScore) / (highestScore - lowestScore)) * 100;
				}
				double y = (getHeight() - 50) - (500 * (pcent / 100));

				if ( table.getRowCount() == 1 )
				{
					pcent = (length / highestLength) * 100;
				}
				else
				{
					pcent = ((length - lowestLength) / (highestLength - lowestLength)) * 100;
				}
				double x = getWidth() - ((getWidth() - 50) - (500 * (pcent / 100)));
				g2.setColor( Color.black );
				g2.drawString( row.toString(), (int) x + 10, (int) y );
				g2.fillOval( (int) x - 5, (int) y - 5, 10, 10 );

				g2.setColor( c );
				g2.fillOval( (int) x - 5, (int) y - 5, 8, 8 );

				if ( points.get( c ) == null )
				{
					previousPoints = new ArrayList<>();
				}
				else
				{
					previousPoints = points.get( c );
				}
				if ( !previousPoints.contains( new Point( (int) x - 5, (int) y - 5 ) ) )
				{
					previousPoints.add( new Point( (int) x - 5, (int) y - 5 ) );
					points.put( c, previousPoints );
				}
			}
		}

		if ( lowestNotch != originalHighestLength )
		{
			g2.setStroke( new BasicStroke( 3f ) );
			for ( Color color : points.keySet() )
			{
				ArrayList<Point> current = points.get( color );
				if ( current.size() > 1 )
				{
					g2.setColor( color );
					int previousX = current.get( 0 ).x;
					int previousY = current.get( 0 ).y;
					for ( int i = 1; i < current.size(); i++ )
					{
						int currentX = current.get( i ).x;
						int currentY = current.get( i ).y;
						g2.drawLine( previousX + 5, previousY + 5, currentX + 5, currentY + 5 );
						previousX = currentX;
						previousY = currentY;
					}
				}
			}
		}

		g2.setColor( Color.black );
		g2.drawLine( 600, 440, 780, 440 );
	}

	private void createGUI()
	{
		Dimension size = new Dimension();
		DefaultTableModel tableModel = new DefaultTableModel();
		textSelect = new JTable( tableModel );
		spTable = new JScrollPane();
		spTable.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

		textComplexity = new JLabel( "Text Complexity:" );
		textLength = new JLabel( "Text Length:" );
		pointsNumber = new JLabel( "Number of points:" );
		maxComp = new JLabel( "Highest Complexity:" );
		maxLen = new JLabel( "Highest Length:" );
		complexityLabel = new JLabel( "0.0" );
		lengthLabel = new JLabel( "0" );
		pointsLabel = new JLabel( "0" );
		maxComplexityLabel = new JLabel( String.valueOf( format.format( originalHighestScore ) ) );
		maxLengthLabel = new JLabel( String.valueOf( originalHighestLength ) );

		TableCellRenderer tcr = new TableCellRenderer()
		{
			private final TableCellRenderer RENDERER = new DefaultTableCellRenderer();

			@Override
			public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column )
			{
				Component c = RENDERER.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				if ( column == 2 )
				{
					c.setBackground( rowAndColor.get( row ) );
				}
				else
				{
					c.setBackground( Color.WHITE );
				}
				return c;
			}
		};

		tableModel.addColumn( "Number" );
		tableModel.addColumn( "Text" );
		tableModel.addColumn( "Color" );
		for ( int i = 0; i < table.getRowCount(); i++ )
		{
			tableModel.addRow( new Object[]
			{ i, table.getValueAt( i, 1 ).toString(), null } );
			textSelect.setDefaultRenderer( Object.class, tcr );
		}

		createButtons();

		add( spTable );
		spTable.setBounds( 600, 50, 175, 200 );
		spTable.getViewport().add( textSelect );

		add( view );
		size = view.getPreferredSize();
		view.setBounds( 600, 280, size.width + 120, size.height + 20 );

		add( textComplexity );
		size = textComplexity.getPreferredSize();
		textComplexity.setBounds( 600, 350, size.width, size.height );
		add( complexityLabel );
		size = complexityLabel.getPreferredSize();
		complexityLabel.setBounds( 720, 350, size.width + 20, size.height );

		add( textLength );
		size = textLength.getPreferredSize();
		textLength.setBounds( 600, 380, size.width, size.height );
		add( lengthLabel );
		size = lengthLabel.getPreferredSize();
		lengthLabel.setBounds( 720, 380, size.width + 20, size.height );

		add( pointsNumber );
		size = pointsNumber.getPreferredSize();
		pointsNumber.setBounds( 600, 410, size.width, size.height );
		add( pointsLabel );
		size = pointsLabel.getPreferredSize();
		pointsLabel.setBounds( 720, 410, size.width + 20, size.height );

		add( maxComp );
		size = maxComp.getPreferredSize();
		maxComp.setBounds( 600, 450, size.width, size.height );
		add( maxComplexityLabel );
		size = maxComplexityLabel.getPreferredSize();
		maxComplexityLabel.setBounds( 720, 450, size.width + 50, size.height );
		add( maxLen );
		size = maxLen.getPreferredSize();
		maxLen.setBounds( 600, 480, size.width, size.height );
		add( maxLengthLabel );
		size = maxLengthLabel.getPreferredSize();
		maxLengthLabel.setBounds( 720, 480, size.width + 50, size.height );

		for ( Component c : getComponents() )
		{
			if ( c instanceof JButton )
			{
				styleButton( (JButton) c );
			}
			else if ( c instanceof JLabel )
			{
				styleLabel( (JLabel) c );
			}
		}
	}

	private void createButtons()
	{
		view = new JButton( "View" );
		view.addActionListener( this );
	}

	private void styleButton( JButton button )
	{
		button.setFont( new Font( "Arial", Font.BOLD, 15 ) );
		button.setBackground( new Color( 50, 50, 50 ) );
		button.setForeground( new Color( 30, 255, 0 ) );
		button.setBorder( BorderFactory.createLineBorder( new Color( 125, 125, 125 ) ) );
		button.setFocusPainted( false );
	}

	private void styleLabel( JLabel label )
	{
		label.setFont( new Font( "Arial", Font.BOLD, 12 ) );
		label.setBackground( new Color( 180, 180, 180 ) );
		label.setForeground( new Color( 0, 0, 0 ) );
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension( WIDTH, HEIGHT );
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		switch ( e.getActionCommand() )
		{
		case "View":
			int row = textSelect.getSelectedRow();

			if ( row != -1 )
			{
				double comp = (double) table.getValueAt( row, 3 );
				complexityLabel.setText( String.valueOf( comp ) );
				lengthLabel.setText( complexityAndLength.get( comp ).toString() );
				pointsLabel.setText( String.valueOf( points.get( rowAndColor.get( row ) ).size() ) );
			}
			break;
		}
	}
}
