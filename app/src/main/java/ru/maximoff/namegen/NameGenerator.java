package ru.maximoff.namegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {
	private final int MIN = 3; // минимальная длина
	private final int MAX = 15; // максимальная длина
	private final int DEF = 7; // длина по умолчанию
	
	private final int diff = 26;
	private final int startChar = 97;
	private String lastName = "";
	private int length = DEF;
	private char firstChar = '-';
	private boolean firstToUpper = true;
	private boolean allToUpper = false;
	private boolean doubleVow = false;
	private List<Character> vowels = new ArrayList<>();

	public NameGenerator() {
		char[] array = {
			'a', 'e', 'i', 'o', 'u'
		};
		for (char ch : array) {
			this.vowels.add(ch);
		}
	}

	public String getLast() {
		return lastName;
	}
	
	public int min() {
		return MIN;
	}
	
	public int max() {
		return MAX;
	}
	
	public int def() {
		return DEF;
	}

	public void setLength(int length) {
		if (length < MIN || length > MAX) {
			length = DEF;
		}
		this.length = length;
	}

	public void firstToUpper(boolean value) {
		this.firstToUpper = value;
	}

	public void allToUpper(boolean value) {
		this.allToUpper = value;
	}

	public void setFirstChar(String lett) {
		this.firstChar = Character.toLowerCase(lett.charAt(0));
	}
	
	public void setDouble(boolean value) {
		this.doubleVow = value;
	}

	public String getName() {
		while (true) {
			Random random = new Random();
			char[] charArray = new char[length];
			int i = 0, x = random.nextInt(2);
			if (firstChar != '-') {
				charArray[0] = firstChar;
				i++;
				x = 0;
				if (vowels.contains(firstChar)) {
					x++;
				}
			}
			for (; i < length; i++,x++) {
				if (checkPosition(x)) {
					char vow = getVowel(random);
					charArray[i] = vow;
					if (doubleVow && i > 0 && i < (length - 1) && (vow == 'e' || vow == 'o')) {
						int z = random.nextInt(99);
						if (checkPosition(z)) {
							i++;
							doubleVow = false;
							charArray[i] = vow;
						}
					}
				} else {
					charArray[i] = getRandChar(random);
				}
			}
			if (firstToUpper) {
				charArray[0] = Character.toUpperCase(charArray[0]);
			}
			String generatedName = new String(charArray);
			if (allToUpper) {
				generatedName = generatedName.toUpperCase();
			}
			if (!generatedName.equals(lastName)) {
				lastName = generatedName;
				return generatedName;
			} else {
				return getName();
			}
		}
	}

	private boolean checkPosition(int i) {
		return i % 2 == 0;
	}

	private char getRandChar(Random random) {
		while (true) {
			char currentChar = (char) (random.nextInt(diff) + startChar);
			if (vowels.contains(currentChar)) {
				continue;
			} else {
				return currentChar;
			}
		}
	}

	private char getVowel(Random random) {
		return vowels.get(random.nextInt(vowels.size()));
	}
}
 
