package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Model;

public class ModelTest
{
	Model m;

	@BeforeEach
	public void setUp()
	{
		System.out.println( "Setting up!" );
		m = new Model( "" );
	}

	@AfterEach
	public void tearDown()
	{
		System.out.println( "Tearing Down!" );
		m = null;
	}

	@Test
	public void testGettersAndSetters()
	{
		System.out.println( "Getters and Setters" );
		m.setInput( "test" );
		m.setOutput( "test" );
		assertEquals( "test", m.getOutput() );
	}

	@Test
	public void testFindUniqueCharacters()
	{
		System.out.println( "Find Unique Characters" );
		m.setInput( "test" );
		m.findUniqueCharacters( "longerthantest" );
		m.findUniqueCharacters( "XY" );
		m.findUniqueCharacters( "XX" );
	}

	@Test
	public void testAnyPattern()
	{
		System.out.println( "Any Pattern" );
		m.setInput( "4312" );
		assertEquals( 0, m.anyPattern( m.findUniqueCharacters( "abc" ) ) );
		m.setInput( "934" );
		m.anyPattern( m.findUniqueCharacters( "512" ) );
		assertEquals( 0, m.anyPattern( new HashMap<Character, String>() ) );
		m.setInput( "431212" );
		m.anyPattern( m.findUniqueCharacters( "xy" ) );

		m.setOrderedPatterns( true );
		m.setInput( "123" );
		m.anyPattern( m.findUniqueCharacters( "123" ) );
		assertNotEquals( "", m.getOrderedOuput() );
	}

	@Test
	public void testFactorComplexity()
	{
		System.out.println( "Factor Complexity" );
		assertFalse( m.factorComplexity( "test", 10, false ).isEmpty() );
	}

	@Test
	public void testPalindromeCheck()
	{
		System.out.println( "Palindrome Check" );
		assertTrue( m.palindromeCheck( "mum" ) );
		assertFalse( m.palindromeCheck( "" ) );
		assertFalse( m.palindromeCheck( "mama" ) );
	}

	@Test
	public void testCharacterCount()
	{
		System.out.println( "Character Count" );
		assertEquals( 4, m.characterCount( "abc1" ) );
		assertEquals( 0, m.characterCount( "" ) );
		assertEquals( 0, m.characterCount( null ) );
	}

	@Test
	public void testPunctoationCount()
	{
		System.out.println( "Punctoation Count" );
		assertEquals( 1, m.punctuationCount( "a,b " ) );
		assertEquals( 0, m.punctuationCount( "" ) );
		assertEquals( 0, m.punctuationCount( null ) );
	}

	@Test
	public void testWordCount()
	{
		System.out.println( "Word Count" );
		assertEquals( 0, m.wordCount( null ) );
		assertEquals( 0, m.wordCount( "" ) );
		assertEquals( 1, m.wordCount( "abc" ) );
	}

	@Test
	public void testProduceBinary()
	{
		System.out.println( "Produce Binary" );
		m.produceBinary( "test", 2 );
		m.produceBinary( ",./", 2 );
	}

	@Test
	public void testVowelAndConsonantBinary()
	{
		System.out.println( "Vowel and Consonant Binary" );
		m.vowelAndConsonantBinary( "aeiou bcd" );
	}

	@Test
	public void testMorphisms()
	{
		System.out.println( "Morphisms" );
		String output = m.morphisms( 3, "11001", "111", new JProgressBar() );
		assertTrue( output.contains( "0" ) );
		assertTrue( output.contains( "1" ) );
	}

	@Test
	public void testCleanKeyWordText()
	{
		System.out.println( "Clean Key Word Text" );
		String output = m.cleanWikiText( m.keyWordText( "cat" ) );
		assertTrue( output.contains( "cat" ) );
	}

	@Test
	public void testCalculateComplexity()
	{
		System.out.println( "Calculate Complexity" );
		JProgressBar pb = new JProgressBar();
		JLabel pl = new JLabel();
		ArrayList<Boolean> dummyCheckBoxes = new ArrayList<>();
		for ( int i = 0; i < 5; i++ )
		{
			dummyCheckBoxes.add( true );
		}
		m.calculateComplexity( "Dog", "xy", dummyCheckBoxes, 2, 2, pb, pl, 0.0 );
		m.calculateComplexity( "Slav ivov ivanov", "xy", dummyCheckBoxes, 2, 2, pb, pl, 0.0 );
	}

}
