package ru.maximoff.namegen;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class NameGenerator {
	private final int diff = 26;
	private final int startChar = 97;
	private String lastName = "";
	private int length = 7;
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

	public void setLength(int length) {
		if (length < 5 || length > 15) {
			length = 7;
		}
		this.length = length;
	}

	public String getName() {
		for (;;) {
			Random random = new Random(System.currentTimeMillis());
			char[] charArray = new char[length];
			for (int i = 0; i < length; i++) {
				if (positionIsOdd(i)) {
					charArray[i] = getVowel(random);
				} else {
					charArray[i] = getConsonant(random);
				}
			}
			charArray[0] = Character.toUpperCase(charArray[0]);
			String generatedName = new String(charArray);
			if (!generatedName.equals(lastName)) {
				lastName = generatedName;
				return generatedName;
			}
		}
	}

	private boolean positionIsOdd(int i) {
		return i % 2 == 0;
	}

	private char getConsonant(Random random) {
		for (;;) {
			char currentChar = (char) (random.nextInt(diff) + startChar);
			if (vowels.contains(currentChar))
				continue;
			else
				return currentChar;
		}
	}

	private char getVowel(Random random) {
		return vowels.get(random.nextInt(vowels.size()));
	}
}
 
