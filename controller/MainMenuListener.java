package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import model.Model;
import view.View;

public class MainMenuListener extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1784241484858260982L;

	Model model;

	public MainMenuListener( Model m )
	{
		this.model = m;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		View view = new View( model );
		switch ( e.getActionCommand() )
		{
		case "Factor Complexity":
			view.createFactorComplexity( model ).createGUI();

			break;

		case "Ordered Patterns":
			view.createOrderedPatterns( model ).createGUI();

			break;

		case "Unordered Patterns":
			view.createUnorderedPatterns( model ).createGUI();

			break;

		case "Generate Morphisms":
			view.createMorphisms( model ).createGUI();

			break;

		case "Calculate Complexity":
			view.createCalculateComplexity( model ).createGUI();

			break;

		case "Compare Texts":
			view.createCompareTexts( model ).createGUI();

			break;

		default:
			break;
		}

	}
}
