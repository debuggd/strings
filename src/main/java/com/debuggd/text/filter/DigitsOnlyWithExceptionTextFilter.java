package com.debuggd.text.filter;

import java.util.HashSet;

public class DigitsOnlyWithExceptionTextFilter implements TextFilter {


    private HashSet<Character> exceptionChars = new HashSet<>();

    public DigitsOnlyWithExceptionTextFilter(char... exceptions) {
        if (exceptions != null) {
            for (char exceptionChar : exceptions) {
                exceptionChars.add(exceptionChar);
            }
        }
    }

    @Override
    public boolean accept(char c) {
        return Character.isDigit(c) || exceptionChars.contains(c);
    }
}
