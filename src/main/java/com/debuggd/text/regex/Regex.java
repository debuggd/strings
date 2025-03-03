package com.debuggd.text.regex;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    private final Pattern pattern;

    /**
     * @param pattern
     * @throws IllegalArgumentException If pattern is null or fails to compile
     */
    public Regex(String pattern) throws IllegalArgumentException {
        if (pattern == null) {
            throw new IllegalArgumentException("parameter 'pattern' cannot be null");
        }
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * @param pattern
     * @throws IllegalArgumentException If parameter {@code pattern} is {@code null}.
     */
    public Regex(Pattern pattern) throws IllegalArgumentException {
        if (pattern == null) {
            throw new IllegalArgumentException("parameter 'pattern' cannot be null");
        }
        this.pattern = pattern;
    }

    /**
     * If string is <code>null</code> or is empty -1 will be returned.
     *
     * @param inputString
     * @return
     */
    public int countMatches(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            return 0;
        }

        return matchAll(inputString).size();
    }

    public RegexMatch matchFirst(String inputString) {
        AtomicReference<RegexMatch> reference = new AtomicReference<>();
        matchIterator(inputString, regexMatch -> {
            reference.set(regexMatch);
            return false;
        });
        return reference.get();
    }

    public RegexMatches matchAll(String inputString) {
        ArrayList<RegexMatch> matches = new ArrayList<>();
        matchIterator(inputString, matches::add);
        return new RegexMatches(matches);
    }

    private void matchIterator(String inputString, Function<RegexMatch, Boolean> consumer) {
        Matcher matcher = pattern.matcher(inputString);
        RegexMatch lastMatch = null;
        int lastEndIndex = -1;

        while (matcher.find()) {
            int s = matcher.start();
            int e = matcher.end();

            RegexMatch regexMatch = new RegexMatch();

            regexMatch.setAfter(inputString.substring(e, inputString.length()));
            regexMatch.setMatch(inputString.substring(s, e));

            if (lastMatch != null) {
                lastMatch.setAfter(inputString.substring(lastEndIndex, s));
                regexMatch.setBefore(lastMatch.getAfter());
            } else {
                regexMatch.setBefore(inputString.substring(0, s));
            }

            if (!consumer.apply(regexMatch)) {
                break;
            }

            lastMatch = regexMatch;
            lastEndIndex = e;
        }
    }
}