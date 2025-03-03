package com.debuggd.text;

import com.debuggd.text.filter.TextFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

public final class Text implements CharSequence {

    private StringBuilder sb;

    public Text() {
        this("");
    }

    public Text(String str) {
        this(new StringBuilder(str));
    }

    public Text(StringBuilder stringBuilder) {
        sb = stringBuilder;
    }

    public Text(Text fromText) {
        sb = new StringBuilder(fromText.sb);
    }

    @Override
    public String toString() {
        return String.valueOf(sb);
    }

    public char[] toCharArray() {
        char[] chars = new char[length()];
        for (int i = 0; i < length(); i++)
            chars[i] = sb.charAt(i);
        return chars;
    }

    public ArrayList<Integer> indicesOf(String str) {
        ArrayList<Integer> indices = new ArrayList<>();
        if (!anyBlank(str)) {
            int i = 0;
            while ((i = sb.indexOf(str, i)) != -1) {
                indices.add(i);
                i = i + str.length();
            }
        }
        return indices;
    }

    public Text trim() {
        return trimLeft().trimRight();
    }

    public Text trimLeft() {
        while (notEmpty() && isSpace(firstChar()))
            deleteFirstChar();
        return this;
    }

    public Text trimRight() {
        while (notEmpty() && isSpace(lastChar()))
            deleteLastChar();
        return this;
    }

    public Text deleteBefore(String strToDelete) {
        return cutBeforeImpl(strToDelete, false);
    }

    public Text deleteBeforeIncl(String strToDelete) {
        return cutBeforeImpl(strToDelete, true);
    }

    private Text cutBeforeImpl(String cutStr, boolean inclCutString) {
        if (!anyBlank(cutStr)) {
            int i = sb.indexOf(cutStr);
            if (i > -1)
                delete(0, inclCutString ? i + cutStr.length() : i);
        }
        return this;
    }

    public Text deleteAfter(String cutStr) {
        return deleteAfterImpl(cutStr, false);
    }

    public Text deleteAfterIncl(String cutStr) {
        return deleteAfterImpl(cutStr, true);
    }

    private Text deleteAfterImpl(String cutStr, boolean inclCutString) {
        if (!anyBlank(cutStr)) {
            int i = sb.indexOf(cutStr);
            if (i > -1)
                delete(inclCutString ? i : i + cutStr.length(), length());
        }
        return this;
    }

    public char firstChar() {
        return charAt(0);
    }

    public char lastChar() {
        return charAt(length() - 1);
    }

    public boolean isEmpty() {
        return length() == 0;
    }

    public boolean notEmpty() {
        return !isEmpty();
    }

    public Text deleteFirstChar() {
        if (notEmpty())
            sb.deleteCharAt(0);
        return this;
    }

    public Text deleteLastChar() {
        if (notEmpty())
            sb.deleteCharAt(length() - 1);
        return this;
    }

    public int length() {
        return sb.length();
    }

    @Override
    public char charAt(int index) {
        return sb.charAt(index);
    }

    public Text insert(String inStr, int offset) {
        sb.insert(offset, inStr);
        return this;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return sb.subSequence(start, end);
    }

    public int count(String str) {
        if (anyBlank(str))
            return -1;
        int count = 0, i = 0;
        while ((i = sb.indexOf(str, i)) != -1) {
            count++;
            i++;
        }
        return count;
    }

    public Text deleteFirst(String rmStr) {
        if (!anyBlank(rmStr)) {
            int i = sb.indexOf(rmStr);
            if (i != -1)
                delete(i, i + rmStr.length());
        }
        return this;
    }

    public Text deleteLast(String rmStr) {
        if (!anyBlank(rmStr)) {
            int i = sb.lastIndexOf(rmStr);
            if (i != -1)
                delete(i, i + rmStr.length());
        }
        return this;
    }

    public Text deleteBetween(String leftBorder, String rightBorder) {
        if (leftBorder == null || leftBorder.isEmpty()
                || rightBorder == null || rightBorder.isEmpty())
            return this;
        int start = indexOf(leftBorder);
        if (start < 0)
            return this;
        int end = lastIndexOf(rightBorder);
        if (end < 0)
            return this;
        delete(start, end);
        return this;
    }

    public Text deleteBetweenOrEmpty(String leftBorder, String rightBorder) {
        if (leftBorder == null || leftBorder.isEmpty()
                || rightBorder == null || rightBorder.isEmpty()) {
            return this;
        }
        int start = indexOf(leftBorder);
        if (start < 0) {
            clear();
            return this;
        }
        int end = lastIndexOf(rightBorder);
        if (end < 0) {
            clear();
            return this;
        }
        delete(start, end);
        return this;
    }

    public Text deleteAll(String rmStr) {
        if (!anyBlank(rmStr)) {
            ArrayList<Integer> indices = indicesOf(rmStr);
            deleteAtIndicesBackwards(indices, rmStr.length());
        }
        return this;
    }

    public Text deleteCharAt(int index) {
        sb.deleteCharAt(index);
        return this;
    }

    private void deleteAtIndicesBackwards(ArrayList<Integer> indices, int length) {
        for (int i = indices.size() - 1; i >= 0; i--)
            delete(indices.get(i), indices.get(i) + length);
    }

    public Text delete(int start, int end) {
        if (start > -1 && end > -1 && start <= end) {
            sb.delete(start, end);
        }
        return this;
    }

    public Text replaceFirst(String replaceWhat, String replaceWith) {
        if (anyBlank(replaceWhat))
            return this;
        if (anyBlank(replaceWith))
            replaceWith = "";
        int i = sb.indexOf(replaceWhat);
        if (i != -1)
            sb.replace(i, i + replaceWhat.length(), replaceWith);
        return this;
    }

    public Text replaceLast(String replaceWhat, String replaceWith) {
        if (!anyBlank(replaceWhat)) {
            if (anyBlank(replaceWith))
                replaceWith = "";
            int i = sb.lastIndexOf(replaceWhat);
            if (i != -1)
                sb.replace(i, i + replaceWhat.length(), replaceWith);
        }
        return this;
    }

    public Text replaceAll(String replaceWhat, String replaceWith) {
        if (!anyBlank(replaceWhat)) {
            if (anyBlank(replaceWith))
                replaceWith = "";
            ArrayList<Integer> indices = indicesOf(replaceWhat);
            replaceAtIndicesBackwards(replaceWhat, replaceWith, indices);
        }
        return this;
    }

    private void replaceAtIndicesBackwards(String what, String with, ArrayList<Integer> indices) {
        for (int i = indices.size() - 1; i > -1; i--)
            sb.replace(indices.get(i), indices.get(i) + what.length(), with);
    }

    public Text replaceUsingPattern(Pattern replaceWhat, String replaceWith) {
        if (replaceWhat != null && !anyBlank(replaceWhat.pattern())) {
            if (anyBlank(replaceWith))
                replaceWith = "";
            sb = new StringBuilder(toString().replaceAll(replaceWhat.pattern(), replaceWith));
        }
        return this;
    }

    public Text toSingleLine() {
        ArrayList<Integer> indicesToDelete = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            char c = charAt(i);
            if (c == '\n' /* Line Feed (aka '\u000a') */ || c == '\r' /* Carriage Return (aka '\u000D') */
                    || c == '\u000b' /* Vertical Tab */ || c == '\u000c' /* Form Feed */
                    || c == '\u0085' /* Next Line */ || c == '\u2028' /* Line Separator */
                    || c == '\u2029' /* Paragraph Separator */) {
                indicesToDelete.add(i);
            }
        }
        deleteAtIndicesBackwards(indicesToDelete, 1);
        return this;
    }

    public Text deleteDigits() {
        return filter(c -> !Character.isDigit(c));
    }

    public Text deleteLetters() {
        return filter(c -> !Character.isLetter(c));
    }

    public Text deleteNonAphaNumeric() {
        return deleteNonAlphaNumeric(false);
    }

    public Text deleteNonAlphaNumeric(boolean leaveWhitespaces) {
        return filter(c -> !Character.isLetterOrDigit(c)
                || (leaveWhitespaces && Character.isWhitespace(c)));
    }

    public Text toLowerCase() {
        for (int i = 0; i < length(); i++)
            sb.setCharAt(i, Character.toLowerCase(charAt(i)));
        return this;
    }

    public Text toUpperCase() {
        return toUpperCase(enumerateIndices());
    }

    public Text toUpperCase(int... indices) {
        for (int i : indices)
            sb.setCharAt(i, Character.toUpperCase(charAt(i)));
        return this;
    }

    public int[] enumerateIndices() {
        int[] indices = new int[length()];
        int c = 0;
        for (int i = 0; i < indices.length; i++)
            indices[i] = c++;
        return indices;
    }

    public Text leaveBetween(String leftBorder, String rightBorder) {
        if (contains(leftBorder) && contains(rightBorder)) {
            deleteBeforeIncl(leftBorder).deleteAfterIncl(rightBorder);
        }
        return this;
    }

    public Text leaveBetweenOrEmpty(String leftBorder, String rightBorder) {
        if (contains(leftBorder) && contains(rightBorder)) {
            deleteBeforeIncl(leftBorder).deleteAfterIncl(rightBorder);
        } else {
            clear();
        }

        return this;
    }

    private void clear() {
        sb = new StringBuilder();
    }

    public boolean contains(String str) {
        return !anyBlank(str) && sb.indexOf(str) != -1;
    }

    public Text normalizeWhiteSpaces() {
        for (int i = 0; i < sb.length(); i++) {
            if (isSpace(charAt(i)))
                sb.setCharAt(i, ' ');
        }
        sb = new StringBuilder(toString().replaceAll(" +", " "));
        return this;
    }

    public static boolean isNumber(String str) {
        if (!anyBlank(str)) {
            Text asText = new Text(str);
            if (asText.firstChar() == '-')
                asText.deleteFirstChar();
            int pointCounter = 0;
            for (char c : asText.toCharArray()) {
                if (c == '.')
                    pointCounter++;
                else if (!isDigit(c))
                    return false;
            }
            return pointCounter < 2;
        }
        return false;
    }

    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    public static boolean isSpace(char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c)
                || c == '\u180E' // MONGOLIAN VOWEL SEPARATOR
                || c == '\u200B' // ZERO WIDTH SPACE
                || c == '\uFEFF'; // ZERO WIDTH NO-BREAK SPACE
    }

    public Text filter(TextFilter filterFunction) {
        if (filterFunction == null)
            return this;

        ArrayList<Integer> indicesToDelete = new ArrayList<>();
        for (int i = 0; i < length(); i++)
            if (!filterFunction.accept(charAt(i)))
                indicesToDelete.add(i);

        for (int i = indicesToDelete.size() - 1; i >= 0; i--)
            deleteCharAt(indicesToDelete.get(i));

        return this;
    }

    public static boolean anyBlank(String... strings) {
        if (strings == null || strings.length == 0)
            return true;
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }

    public boolean equals(String str) {
        return toString().equals(str);
    }

    public boolean equalsIgnoreCase(String str) {
        return toString().equalsIgnoreCase(str);
    }

    public boolean containsIgnoreCase(String str) {
        if (anyBlank(str))
            return false;
        return toString().toLowerCase().contains(str.toLowerCase());
    }

    public void append(char c) {
        sb.append(c);
    }

    public void append(String... strings) {
        for (String str : strings)
            if (!anyBlank(str))
                sb.append(str);
    }

    public static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    public Text prepend(String str) {
        if (!anyBlank(str))
            sb.insert(0, str);
        return this;
    }

    public int indexOf(char c) {
        int[] indices = enumerateIndices();
        for (int i = 0; i < indices.length; i++) {
            if (charAt(i) == c)
                return i;
        }
        return -1;
    }

    public int indexOf(String str) {
        return sb.indexOf(str);
    }

    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }

    public static Text textAfter(Text text, String str) {
        Text copy = new Text(text);
        return copy.deleteBeforeIncl(str);
    }

    public Text deleteLeading(char... chars) {
        HashSet<Character> charsToDelete = new HashSet<>();
        for (char c : chars) {
            charsToDelete.add(c);
        }
        while (!isEmpty() && charsToDelete.contains(firstChar())) {
            deleteFirstChar();
        }
        return this;
    }

    public Text deleteTrailing(char... chars) {
        HashSet<Character> charsToDelete = new HashSet<>();
        for (char c : chars) {
            charsToDelete.add(c);
        }
        while (!isEmpty() && charsToDelete.contains(lastChar())) {
            deleteLastChar();
        }
        return this;
    }

    public Text clearIfEquals(String str) {
        if (str != null && !str.isEmpty() && equals(str)) {
            clear();
        }
        return this;
    }

    public boolean endsWith(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // TODO optimiere, sb.toString ist hier unnötig!!!
        return sb.toString().endsWith(str);
    }

    public Text setIfEquals(String newValueStr, String compareStr)
            throws IllegalArgumentException {
        // TODO hier auf args-module einbauen
        if (compareStr == null || compareStr.isEmpty()) {
            throw new IllegalArgumentException("compareStr");
        }
        if (equals(compareStr)) {
            sb = new StringBuilder(newValueStr);
        }
        return this;
    }
}
