package com.debuggd.text;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class TextTest {

    @Test
    public void prepend() {
        Text t = new Text("test");
        assertEquals("test_test", t.prepend("test_").toString());
    }

    @Test
    public void filter() {
        Text t = new Text("!§$%das-.,ist?=)ein;:;:test^^^´");
        assertEquals("dasisteintest", t.filter(Character::isLetter).toString());
    }

    @Test
    public void enumerateIndices() {
        Text text = new Text("0");
        assertArrayEquals(new int[]{0}, text.enumerateIndices());
        text = new Text("00");
        assertArrayEquals(new int[]{0, 1}, text.enumerateIndices());
    }

    @Test
    public void contains() {
        Text text = new Text("text to test");
        assertFalse(text.contains(null));
        assertFalse(text.contains(""));
        assertFalse(text.contains("textt"));
        assertTrue(text.contains("t to"));
    }

    @Test
    public void toSingleLine() {
        Text text = new Text("\n\rtext \u000b\u0085\u2029to\u000c \u2028test\r\n");
        assertEquals("text to test", text.toSingleLine().toString());
    }

    @Test
    public void replacePattern() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.replaceUsingPattern(Pattern.compile(""), "").toString());
        assertEquals("texttest", text.replaceUsingPattern(Pattern.compile(".to."), "").toString());
        assertEquals("textt", text.replaceUsingPattern(Pattern.compile("est$"), null).toString());
    }

    @Test
    public void normalizeWhiteSpaces() {
        String[] spaces = new String[]{
                "\u0009", // CHARACTER TABULATION
                "\u0020", // SPACE
                "\u00A0", // NO-BREAK SPACE
                "\u1680", // OGHAM SPACE MARK
                "\u180E", // MONGOLIAN VOWEL SEPARATOR
                "\u2000", // EN QUAD
                "\u2001", // EM QUAD
                "\u2002", // EN SPACE
                "\u2003", // EM SPACE
                "\u2004", // THREE-PER-EM SPACE
                "\u2005", // FOUR-PER-EM SPACE
                "\u2006", // SIX-PER-EM SPACE
                "\u2007", // FIGURE SPACE
                "\u2008", // PUNCTUATION SPACE
                "\u2009", // THIN SPACE
                "\u200A", // HAIR SPACE
                "\u200B", // ZERO WIDTH SPACE
                "\u202F", // NARROW NO-BREAK SPACE
                "\u205F", // MEDIUM MATHEMATICAL SPACE
                "\u3000", // IDEOGRAPHIC SPACE
                "\uFEFF" // ZERO WIDTH NO-BREAK SPACE
        };
        StringBuilder sb = new StringBuilder(" start");
        for (String space : spaces) {
            sb.append(space);
        }
        sb.append("end ");
        Text textOhneWhitespaces = new Text(sb).normalizeWhiteSpaces();
        assertEquals(" start end ", textOhneWhitespaces.toString());
    }

    @Test
    public void replaceAll() {
        Text text;

        text = new Text("tttt");
        assertEquals("dddd", text.replaceAll("t", "d").toString());
        text = new Text("tttt");
        assertEquals("dt", text.replaceAll("ttt", "d").toString());
        text = new Text("ttttd");
        assertEquals("td", text.replaceAll("tttd", "d").toString());
        text = new Text("tttt");
        assertEquals("d", text.replaceAll("tttt", "d").toString());
        text = new Text("tttt");
        assertEquals("tttt", text.replaceAll(null, "xx").toString());

        // 'replaceWhat' werden mit "" und null gelöscht
        text = new Text("tttt");
        assertEquals("", text.replaceAll("t", "").toString());
        text = new Text("tttt");
        assertEquals("", text.replaceAll("t", null).toString());
        text = new Text("tttt");
        assertEquals("t", text.replaceAll("ttt", null).toString());
        text = new Text("tttt");
        assertEquals("t", text.replaceAll("ttt", "").toString());

        // ungültige 'replaceWhat' werden nicht akzeptiert
        text = new Text("tttt");
        assertEquals("tttt", text.replaceAll(null, "xx").toString());
        text = new Text("tttt");
        assertEquals("tttt", text.replaceAll("", "xx").toString());

    }

    @Test
    public void indicesOf() {
        Text text = new Text("yxxxy");
        assertArrayEquals(new Object[]{}, text.indicesOf("t").toArray());
        assertArrayEquals(new Object[]{}, text.indicesOf("yxy").toArray());
        assertArrayEquals(new Object[]{0, 4}, text.indicesOf("y").toArray());
        assertArrayEquals(new Object[]{0}, text.indicesOf("yxxxy").toArray());
        assertArrayEquals(new Object[]{1, 2, 3}, text.indicesOf("x").toArray());
        assertArrayEquals(new Object[]{2}, text.indicesOf("xxy").toArray());
        assertArrayEquals(new Object[]{}, text.indicesOf("").toArray());
        assertArrayEquals(new Object[]{}, text.indicesOf(null).toArray());
        text = new Text("yxxxxy");
        assertArrayEquals(new Object[]{1, 3}, text.indicesOf("xx").toArray());

    }

    @Test
    public void replaceLast() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.replaceLast(null, "test").toString());
        assertEquals("text to test", text.replaceLast("", "test").toString());
        assertEquals("text to tess", text.replaceLast("t", "s").toString());
        assertEquals("dt to tess", text.replaceLast("tex", "d").toString());
        assertEquals("dt to ", text.replaceLast("tess", "").toString());
        assertEquals("dt t", text.replaceLast("o ", null).toString());
    }

    @Test
    public void replaceFirst() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.replaceFirst(null, "test").toString());
        assertEquals("text to test", text.replaceFirst("", "test").toString());
        assertEquals("test to test", text.replaceFirst("text", "test").toString());
        assertEquals("dtdest to test", text.replaceFirst("t", "dtd").toString());
        assertEquals("est to test", text.replaceFirst("dtd", "").toString());
        text = new Text("text to test");
        assertEquals("text to t", text.replaceFirst("est", null).toString());
    }

    @Test
    public void deleteAll() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.deleteAll("").toString());
        assertEquals("text to test", text.deleteAll(null).toString());
        assertEquals("text to test", text.deleteAll("tt").toString());
        assertEquals("ex o es", text.deleteAll("t").toString());
        text = new Text("ttttttttt");
        assertEquals("", text.deleteAll("t").toString());
    }

    @Test
    public void deleteLast() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.deleteLast("").toString());
        assertEquals("text to test", text.deleteLast(null).toString());
        assertEquals("text to st", text.deleteLast("te").toString());
        text = new Text("text to test");
        assertEquals("texttest", text.deleteLast(" to ").toString());
        text = new Text("text to test");
        assertEquals("to test", text.deleteLast("text ").toString());
    }

    @Test
    public void deleteFirst() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.deleteFirst("").toString());
        assertEquals("text to test", text.deleteFirst(null).toString());
        assertEquals("xt to test", text.deleteFirst("te").toString());
        text = new Text("text to test");
        assertEquals("texttest", text.deleteFirst(" to ").toString());
        text = new Text("text to test");
        assertEquals("text to", text.deleteFirst(" test").toString());
    }

    @Test
    public void delete() {
        Text text = new Text("text to test");
        assertEquals("text to test", text.delete(-1, 3).toString());
        assertEquals("text to test", text.delete(1, 0).toString());
        assertEquals("text to test", text.delete(0, 0).toString());
        assertEquals("ext to test", text.delete(0, 1).toString());
        assertEquals(" to test", text.delete(0, 3).toString());
        assertEquals(" to st", text.delete(4, 6).toString());
    }

    @Test
    public void count() {
        Text text = new Text("text to test");
        assertEquals(-1, text.count(""));
        assertEquals(-1, text.count(null));
        assertEquals(5, text.count("t"));
        assertEquals(2, text.count("te"));
        assertEquals(1, text.count("text"));
        assertEquals(0, text.count("textz"));
        text = new Text("tt");
        assertEquals(2, text.count("t"));
    }

    @Test
    public void cutAfterIncl() {
        Text text;
        text = new Text("text to test");
        assertEquals("text to test", text.deleteAfterIncl("").toString());
        assertEquals("text to test", text.deleteAfterIncl(null).toString());
        assertEquals("text ", text.deleteAfterIncl("to").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteAfterIncl("too").toString());
        text = new Text("text to test");
        assertEquals("text to ", text.deleteAfterIncl("test").toString());
    }

    @Test
    public void cutAfter() {
        Text text;
        text = new Text("text to test");
        assertEquals("text to test", text.deleteAfter("").toString());
        assertEquals("text to test", text.deleteAfter(null).toString());
        assertEquals("text to", text.deleteAfter("to").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteAfter("too").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteAfter("test").toString());

    }

    @Test
    public void cutBeforeIncl() {
        Text text;
        text = new Text("text to test");
        assertEquals("text to test", text.deleteBeforeIncl("").toString());
        assertEquals("text to test", text.deleteBeforeIncl(null).toString());
        assertEquals(" test", text.deleteBeforeIncl("to").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteBeforeIncl("too").toString());
        text = new Text("text to test");
        assertEquals(" to test", text.deleteBeforeIncl("text").toString());
    }

    @Test
    public void cutBefore() {
        Text text;
        text = new Text("text to test");
        assertEquals("text to test", text.deleteBefore("").toString());
        assertEquals("text to test", text.deleteBefore(null).toString());
        assertEquals("to test", text.deleteBefore("to").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteBefore("too").toString());
        text = new Text("text to test");
        assertEquals("text to test", text.deleteBefore("text").toString());
    }

    @Test
    public void trim() {
        Text text = new Text(" \u00a0 \n \r\n trimmed \u00a0 \r\n \n");
        assertEquals("trimmed", text.trim().toString());
    }

    @Test
    public void emtpyConstructorTest() {
        assertEquals("", new Text().toString());
    }

    @Test
    void stringConstructorTest() {
        assertEquals("test", new Text("test").toString());
    }

    @Test
    void stringBuilderConstructorTest() {
        assertEquals("test", new Text(new StringBuilder("test")).toString());
    }

    @Test
    void textConstructorTest() {
        Text text = new Text("test");
        Text textCopy = new Text(text);
        assertEquals("test", textCopy.toString());
    }

    @Test
    public void isNumber() {
        assertTrue(Text.isNumber("0"));
        assertTrue(Text.isNumber("-1"));
        assertTrue(Text.isNumber("0.4"));
        assertTrue(Text.isNumber("-234234.345"));
        assertFalse(Text.isNumber("44a"));
        assertFalse(Text.isNumber(""));
        assertFalse(Text.isNumber(null));
    }
}
