package model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

// import org.jsoup.Jsoup;

public class Model {
	String input = null;
	String pattern = null;
	String output = "";
	String orderedPatternOutput = "";

	boolean complexity = false;
	boolean orderedPatterns = false;
	int patternsLimit = 0;
	int orderedCount = 0;

	ArrayList<Character> charactersOrder = new ArrayList<Character>();
	ArrayList<ArrayList<String>> samples = new ArrayList<ArrayList<String>>();
	HashMap<String, Integer> occurances = new HashMap<String, Integer>();

	public Model(String input) {
		this.input = input;
	}

	public void setInput(String in) {
		this.input = in;
	}

	public void setOrderedPatterns(boolean mode) {
		orderedPatterns = mode;
	}

	public void setPatternsLimit(int in) {
		this.patternsLimit = in;
	}

	public String getOutput() {
		return output;
	}

	public String getOrderedOuput() {
		return orderedPatternOutput;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setOrderedOutput(String orderedPatternOutput) {
		this.orderedPatternOutput = orderedPatternOutput;
		orderedCount = 0;
	}

	public HashMap<Character, String> findUniqueCharacters(String pattern) {
		this.pattern = pattern;
		HashMap<Character, String> uniqueCharacters = new HashMap<Character, String>();

		// Check if pattern is even usable with current input string
		if (input.length() < pattern.length()) {
			return new HashMap<Character, String>();
		}

		// Find each unique character in pattern and add them to map
		for (int i = 0; i < pattern.length(); i++) {
			char current = pattern.charAt(i);
			if (!uniqueCharacters.containsKey(current)) {
				uniqueCharacters.put(current, "");
				charactersOrder.add(current);
			}
		}

		return uniqueCharacters;
	}

	public int anyPattern(HashMap<Character, String> uniqueCharacters) {
		int totalOccurances = 0;
		if (uniqueCharacters.isEmpty()) {
			output += "Pattern is longer than input string" + "\r\n";
			return 0;
		}

		int first = 0;
		int second = 1;

		while (input.substring(first, second).length() <= input.length()) {
			String substring = input.substring(first, second);
			ArrayList<ArrayList<String>> temp = factorComplexity(substring, substring.length(), true);
			ArrayList<String> substringDivisions = new ArrayList<String>();
			for (ArrayList<String> currentArray : temp) {
				if (currentArray.size() >= pattern.length()) {
					for (String currentString : currentArray) {
						substringDivisions.add(currentString);
					}
				}
			}

			if (substringDivisions.size() > 0) {
				String[] test = new String[substringDivisions.size()];
				for (int i = 0; i < substringDivisions.size(); i++) {
					test[i] = substringDivisions.get(i);
				}

				int uniqueCharsSize = uniqueCharacters.size();
				int testLength = test.length;
				if (substring.length() >= pattern.length()) {
					printCombination(test, testLength, uniqueCharsSize, uniqueCharacters, substring);
				}
			}
			second = second + 1;
			if (second > input.length()) {
				first++;
				second = first + uniqueCharacters.size();
				if (second > input.length()) {
					break;
				}
			}
		}
		samples.clear();
		charactersOrder.clear();
		for (String key : occurances.keySet()) {
			totalOccurances += occurances.get(key);
		}
		if (!complexity) {
			output = "Total unique substrings containing " + pattern + ": " + totalOccurances + "\r\n" + output;
		}
		orderedPatternOutput = "Total unique substrings containing " + pattern + ": " + orderedCount + "\r\n"
				+ orderedPatternOutput;
		occurances.clear();
		input = null;
		return totalOccurances;
	}

	public void combinationUtil(String[] arr, String data[], int start, int end, int index, int r,
			HashMap<Character, String> uniqueCharacters, String substring) {
		if (index == r) {
			int len = 0;
			// for ( int j = 0; j < r; j++ )

			// Checks for overlap between indexes so we don't have 4 and 43 from 4312 input
			Set<Integer> indexes = new HashSet<>();
			for (int k = 0; k < data.length; k++) {
				len += data[k].length();
				int firstOccurance = substring.indexOf(data[k]);

				for (int i = firstOccurance; i < substring.length(); i++) {
					if (substring.length() - i < data[k].length()) {
						break;
					}
					if (substring.indexOf(data[k], i) >= 0)
						indexes.add(substring.indexOf(data[k], i));
				}
			}
			if (len <= substring.length() && indexes.size() >= data.length) {
				checkForPattern(data, uniqueCharacters, substring);
			}
			return;
		}
		for (int i = 0; i <= end && end - i + 1 >= r - index; i++) {
			if (arr[i].length() <= patternsLimit) {
				data[index] = arr[i];
				combinationUtil(arr, data, i + 1, end, index + 1, r, uniqueCharacters, substring);
			}
		}
	}

	public void printCombination(String arr[], int n, int r, HashMap<Character, String> uniqueCharacters,
			String substring) {
		String data[] = new String[r];

		combinationUtil(arr, data, 0, n - 1, 0, r, uniqueCharacters, substring);
	}

	public void checkForPattern(String[] data, HashMap<Character, String> uniqueCharacters, String substring) {
		String sample = "";
		ArrayList<String> temp = new ArrayList<String>();
		// Set<Integer> positionInSample = new HashSet<>();

		for (int j = 0; j < data.length; j++) {
			if (!uniqueCharacters.containsValue(data[j])) {
				// inputIndexes.put( charactersOrder.get( j ), input.indexOf( data[ j ] ) );
				uniqueCharacters.put(charactersOrder.get(j), data[j]);
			}
		}

		for (int j = 0; j < pattern.length(); j++) {
			if (uniqueCharacters.get(pattern.charAt(j)).isEmpty()) {
				sample = "";
				break;
			} else {
				sample += (uniqueCharacters.get(pattern.charAt(j)));
				temp.add(uniqueCharacters.get(pattern.charAt(j)));
			}
		}

		if ((substring.contains(sample) && !sample.isEmpty()) && !samples.contains(temp)) {
			ArrayList<Integer> patternValues = new ArrayList<Integer>();
			ArrayList<Integer> numericValues = new ArrayList<Integer>();
			int first = 0;
			int second = sample.length();
			int count = 0;
			while (second < input.length() + 1) {
				if (sample.equals(input.substring(first, second))) {
					if (!complexity) {
						output += "Pattern found " + sample + "\r\n";
					}
					count++;
				}
				first++;
				second++;
			}

			for (int i = 0; i < uniqueCharacters.size(); i++) {
				char patternChar = charactersOrder.get(i);
				String value = uniqueCharacters.get(charactersOrder.get(i));
				if (!complexity) {
					output += patternChar + ": " + value + "\r\n";
				}
				if (value.chars().allMatch(Character::isDigit) && pattern.chars().allMatch(Character::isDigit)) {
					patternValues.add(Character.getNumericValue(patternChar));
					numericValues.add(Integer.valueOf(value));
				}
			}
			// Check if pattern is numeric before proceeding to do ordered patterns check
			if (pattern.chars().allMatch(Character::isDigit) && input.chars().allMatch(Character::isDigit)
					&& orderedPatterns) {
				HashMap<Integer, Integer> order = new HashMap<Integer, Integer>();
				boolean ordered = true;
				int previousPatternValue = 0;
				char[] previousValueDigits = null;
				char[] currentValueDigits = null;
				int j = 0;

				for (int i = 0; i < numericValues.size(); i++) {
					order.put(patternValues.get(i), numericValues.get(i));
				}
				for (Integer currentPatternValue : order.keySet()) {
					if (j == 0) {
						previousPatternValue = currentPatternValue;
						previousValueDigits = order.get(currentPatternValue).toString().toCharArray();
					} else if (j > 0 && currentPatternValue > previousPatternValue) {
						currentValueDigits = order.get(currentPatternValue).toString().toCharArray();
						for (int i = 0; i < currentValueDigits.length; i++) {
							for (int k = 0; k < previousValueDigits.length; k++) {
								if (Character.getNumericValue(currentValueDigits[i]) <= Character
										.getNumericValue(previousValueDigits[k])) {
									ordered = false;
									break;
								}
							}
						}
						previousPatternValue = currentPatternValue;
						previousValueDigits = currentValueDigits;
					} else {
						ordered = false;
						break;
					}
					j++;
				}
				if (ordered) {
					orderedPatternOutput += "Pattern found " + sample + "\r\n";
					for (int i = 0; i < uniqueCharacters.size(); i++) {
						char patternChar = charactersOrder.get(i);
						String value = uniqueCharacters.get(charactersOrder.get(i));
						orderedPatternOutput += patternChar + ": " + value + "\r\n";
					}
					orderedPatternOutput += "ORDERED" + "\r\n";
					orderedCount++;
				}
			}
			occurances.put(sample, count);
			samples.add(temp);
		}

	}

	public ArrayList<ArrayList<String>> factorComplexity(String in, int limiter, boolean unorderedPatterns) {
		in = in.replace("\n", "").replace("\r", "");
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		int factors = 1;
		while (factors < in.length() + 1 && factors <= limiter) {
			ArrayList<String> current_factors = new ArrayList<String>();
			if (factors == 1) {
				for (int i = 0; i < in.length(); i++) {
					if (!current_factors.contains(String.valueOf(in.charAt(i))) && !unorderedPatterns) {
						current_factors.add(String.valueOf(in.charAt(i)));
					} else if (unorderedPatterns) {
						current_factors.add(String.valueOf(in.charAt(i)));
					}
				}
				output.add(current_factors);
			} else if (factors != 1 && factors < in.length()) {
				int First = 0;
				int Second = factors;
				while (Second <= in.length()) {
					if (!current_factors.contains(in.substring(First, Second)) && !unorderedPatterns) {
						current_factors.add(in.substring(First, Second));
					} else if (unorderedPatterns) {
						current_factors.add(in.substring(First, Second));
					}
					First++;
					Second++;
				}
				output.add(current_factors);
			} else if (factors == in.length()) {
				current_factors.add(in);
				output.add(current_factors);
			}
			factors++;
		}

		return output;
	}

	public boolean palindromeCheck(String input) {
		if (input.length() == 0 || input.isEmpty())
			return false;
		String reversedInput = "";
		for (int i = input.length() - 1; i >= 0; i--) {
			reversedInput += input.charAt(i);
		}
		return input.equals(reversedInput);
	}

	public double characterCount(String input) {
		if (input == null || input.isEmpty())
			return 0;

		double count = 0;
		for (char c : input.toLowerCase().toCharArray()) {
			if (Character.isLetterOrDigit(c))
				count++;
		}
		return count;
	}

	public int punctuationCount(String input) {
		if (input == null || input.isEmpty())
			return 0;

		int count = 0;
		for (char c : input.toLowerCase().toCharArray()) {
			if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
				count++;
		}
		return count;
	}

	public int wordCount(String input) {
		if (input != null && !input.isEmpty())
			return input.split("\\s+").length;
		return 0;
	}

	public ArrayList<Integer> produceBinary(String input, int mod) {
		String text = input.toLowerCase();
		char[] characters = text.toCharArray();
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (char c : characters) {
			int temp = (int) c;
			output.add((temp) % mod);
		}
		return output;
	}

	public ArrayList<Integer> vowelAndConsonantBinary(String input) {
		String text = input.toLowerCase();
		char[] characters = text.toCharArray();
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (char c : characters) {
			boolean vowelGroup1 = (c == 'a' || c == 'e');
			boolean vowelGroup2 = (c == 'i' || c == 'o');
			boolean vowelGroup3 = (c == 'u' || c == '0');
			if ((vowelGroup1 || vowelGroup2) || vowelGroup3) {
				output.add(0);
			} else {
				output.add(1);
			}
		}
		return output;
	}

	public String extendToLength(int highestLength, String text) {
		String t = text;
		while (characterCount(t) < highestLength) {
			t += t;
		}
		return t.substring(0, highestLength);
	}

	public String morphisms(int itteration, String first, String second, JProgressBar pb) {
		if (first.equals(second)) {
			return "Please enter different values for 0 and 1";
		}
		String out = "";
		String output = "";
		HashMap<Character, String> mappings = new HashMap<Character, String>();
		mappings.put('0', first);
		mappings.put('1', second);

		double x = 100.0 / (double) itteration;
		for (int i = 0; i < itteration; i++) {
			// double currentPercent = ((double) i / (double) itteration) * 100;
			if (i == 0) {
				out = "0";
			} else {
				String current = "";
				for (int j = 0; j < out.length(); j++) {
					current += mappings.get(out.charAt(j));
				}
				out = current;
			}
			output += "Iteration " + i + ": " + out + "\r\n";
			pb.setValue(pb.getValue() + (int) x);
			updateBarColours(pb);
		}
		pb.setValue(100);
		pb.setBackground(new Color(0, 200, 0));
		return output;
	}

	public String keyWordText(String topic) {
		String wikiText = "Not Working";
		// try
		// {
		// org.jsoup.nodes.Document doc = Jsoup.connect(
		// "https://en.wikipedia.org/wiki/" + topic ).get();
		// org.jsoup.select.Elements content = doc.select( "div[id=content]" );
		// org.jsoup.select.Elements p = content.select( "p:not(:has(#coordinates))" );
		// wikiText = p.text();
		// }
		// catch ( IOException e )
		// {
		// wikiText = "!Exception!";
		// }
		return wikiText;
	}

	public String cleanWikiText(String in) {
		if (in.equals("!Exception!")) {
			return "Could not find an article on that, sorry. Press Clear and try again.";
		}
		if (in.contains(".")) {
			String formatted = "";
			int fullStopCount = 0;
			int currentCharPosition = 0;
			int sentenceStartPos = 0;

			while (true && fullStopCount < 3) {
				if (in.charAt(currentCharPosition) == '.') {
					currentCharPosition++;
					formatted += in.substring(sentenceStartPos, currentCharPosition);
					sentenceStartPos = currentCharPosition;
					fullStopCount++;
				} else
					currentCharPosition++;
			}
			return formatted;
		}
		return "Article does not seem to have at least three sentences.";
	}

	public synchronized double calculateComplexity(String input, String pattern, ArrayList<Boolean> options, int modulo,
			int fCLimit, JProgressBar pb, JLabel pl, double barIncrement) {
		boolean vCBinaryCheck = options.get(0);
		boolean moduloBinaryCheck = options.get(1);
		boolean factorComplexityCheck = options.get(2);
		boolean unorderedPatternsCheck = options.get(3);
		boolean orderedPatternsCheck = options.get(4);
		orderedPatterns = orderedPatternsCheck;

		complexity = true;
		double totalScore = 0;
		int numberOfFactorsVC = 0;
		int numberOfFactorsMod = 0;
		String binaryRepresentationVC = "";
		String binaryRepresentationMod = "";
		// 1
		if (vCBinaryCheck) {
			ArrayList<Integer> binaryArrayVC = vowelAndConsonantBinary(input);
			for (int i = 0; i < binaryArrayVC.size(); i++) {
				binaryRepresentationVC += binaryArrayVC.get(i).toString();
			}
		}

		if (moduloBinaryCheck) {
			ArrayList<Integer> binaryArrayMod = produceBinary(input, modulo);
			for (int i = 0; i < binaryArrayMod.size(); i++) {
				binaryRepresentationMod += binaryArrayMod.get(i).toString();
			}
		}
		pb.setValue(pb.getValue() + (int) barIncrement);
		updateBarColours(pb);

		// 2
		if (factorComplexityCheck && vCBinaryCheck) {
			pl.setText("Calculating factor complexity...");
			output += "Factor complexity result on binary produced using vowells and consonants. Limit set to "
					+ fCLimit + "\r\n";
			for (ArrayList<String> current : factorComplexity(binaryRepresentationVC, fCLimit, false)) {
				int p = 0;
				for (String s : current) {
					if (palindromeCheck(s))
						p++;
				}
				numberOfFactorsVC += current.size();
				double ratio = current.size() / (double) current.get(0).length();
				output += current + " with a ratio of: " + ratio + " and with " + p + " palindrome(s)" + "\r\n";
				totalScore += ratio;
			}
			output += "Ratio of unique factors up to length " + fCLimit + " / length of input: "
					+ (double) numberOfFactorsVC / binaryRepresentationVC.length() + "\r\n";
		}

		if (unorderedPatternsCheck && vCBinaryCheck) {
			setInput(binaryRepresentationVC);
			pl.setText("Finding pattern " + pattern + " in binary...");
			int total = anyPattern(findUniqueCharacters(pattern));
			totalScore += total;
			output = "Total unique substrings containing " + pattern + ": " + total + "\r\n" + output;
		}
		if (orderedPatternsCheck && vCBinaryCheck) {
			orderedCount = 0;
			setInput(binaryRepresentationVC);
			pl.setText("Finding pattern " + pattern + " in binary...");
			anyPattern(findUniqueCharacters(pattern));
			output = "Total unique substrings containing ordered " + pattern + ": " + orderedCount + "\r\n" + output;
			totalScore += orderedCount;
		}
		pb.setValue(pb.getValue() + (int) barIncrement);
		updateBarColours(pb);

		// 3
		if (factorComplexityCheck && moduloBinaryCheck) {
			pl.setText("Calculating factor complexity...");
			output += "Factor complexity result on binary produced using numeric values for letters and mod " + modulo
					+ ". Limit set to " + fCLimit + "\r\n";
			for (ArrayList<String> current : factorComplexity(binaryRepresentationMod, fCLimit, false)) {
				int p = 0;
				for (String s : current) {
					if (palindromeCheck(s))
						p++;
				}
				numberOfFactorsMod += current.size();
				double ratio = current.size() / (double) current.get(0).length();
				output += current + " with a ratio of: " + ratio + " and with " + p + " palindrome(s)" + "\r\n";
				totalScore += ratio;
			}
			output += "Ratio of unique factors up to length " + fCLimit + " / length of input: "
					+ (double) numberOfFactorsMod / binaryRepresentationMod.length() + "\r\n";
		}

		if (unorderedPatternsCheck && moduloBinaryCheck) {
			setInput(binaryRepresentationMod);
			pl.setText("Finding pattern " + pattern + " in binary...");
			int total = anyPattern(findUniqueCharacters(pattern));
			totalScore += total;
			output = "Total unique substrings containing " + pattern + ": " + total + "\r\n" + output;
		}
		if (orderedPatternsCheck && moduloBinaryCheck) {
			orderedCount = 0;
			setInput(binaryRepresentationMod);
			pl.setText("Finding pattern " + pattern + " in binary...");
			anyPattern(findUniqueCharacters(pattern));
			output = "Total unique substrings containing ordered " + pattern + ": " + orderedCount + "\r\n" + output;
			totalScore += orderedCount;
		}
		pb.setValue(pb.getValue() + (int) barIncrement);
		updateBarColours(pb);

		// 4
		int wc = wordCount(input);
		int cc = (int) characterCount(input);
		int pc = punctuationCount(input);
		Set<Character> alphabet = new HashSet<>();
		for (char c : input.toCharArray()) {
			alphabet.add(c);
		}
		pl.setText("Counting words, letters and punctoation marks...");
		output += "Total number of words: " + wc + "\r\n";
		totalScore += wc;
		output += "Total number of letters: " + cc + "\r\n";
		totalScore += cc;
		output += "Total number of punctoation marks: " + pc + "\r\n";
		totalScore += pc;
		output += "Estimate complexity for string entered is: " + totalScore + "\r\n";

		complexity = false;
		pb.setValue(pb.getValue() + (int) barIncrement);
		updateBarColours(pb);
		return totalScore;
	}

	private void updateBarColours(JProgressBar pb) {
		if (pb.getValue() <= 25) {
			pb.setForeground(new Color(255, 70, 70));
		} else if (pb.getValue() <= 50) {
			pb.setForeground(new Color(255, 170, 70));
		} else if (pb.getValue() <= 75) {
			pb.setForeground(new Color(210, 205, 0));
		} else {
			pb.setForeground(new Color(0, 200, 0));
		}
	}

}
