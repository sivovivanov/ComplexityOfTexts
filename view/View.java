package view;

import model.Model;

public class View
{
	Model model;

	public View( Model m )
	{
		this.model = m;
	}

	public MainMenu createMainMenu()
	{
		return new MainMenu( model );
	}

	public Module createFactorComplexity( Model model )
	{
		return new FactorComplexity( model );
	}
	
	public Module createOrderedPatterns( Model model )
	{
		return new OrderedPatterns( model );
	}

	public Module createUnorderedPatterns( Model model )
	{
		return new UnorderedPatterns( model );
	}

	public Module createMorphisms( Model model )
	{
		return new Morphisms( model );
	}

	public Module createCalculateComplexity( Model model )
	{
		return new CalculateComplexity( model );
	}

	public Module createCompareTexts( Model model )
	{
		return new CompareTexts( model );
	}

}
