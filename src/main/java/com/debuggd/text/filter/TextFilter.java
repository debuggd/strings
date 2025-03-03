package com.debuggd.text.filter;

@FunctionalInterface
public interface TextFilter {

	TextFilter LETTERS_ONLY = Character::isLetter;
	TextFilter DIGITS_ONLY = Character::isLetter;

	boolean accept( char c );
}
