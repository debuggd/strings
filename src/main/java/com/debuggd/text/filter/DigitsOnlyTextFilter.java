package com.debuggd.text.filter;

public class DigitsOnlyTextFilter implements TextFilter {

    @Override
    public boolean accept(char c) {
        return Character.isDigit(c);
    }
}
