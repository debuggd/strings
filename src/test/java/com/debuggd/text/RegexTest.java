package com.debuggd.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.debuggd.text.regex.Regex;
import com.debuggd.text.regex.RegexMatch;
import com.debuggd.text.regex.RegexMatches;

class RegexTest {

    @Test
    public void matchAllTest() {

        RegexMatches matches = null;
        String string = "adas ist ein matchAllTesti";

        Assertions.assertEquals(3, new Regex("[a]").countMatches(string), "case-sensitive count");
        Assertions.assertEquals(4, new Regex("(?i)[a]").countMatches(string), "case-insensitive count");

        matches = new Regex("[ai]").matchAll(string);
        Assertions.assertEquals(6, matches.size(), "matches: number matches");

        RegexMatch m_1 = matches.getMatch(0);
        RegexMatch m_2 = matches.getMatch(1);
        RegexMatch m_3 = matches.getMatch(2);
        RegexMatch m_4 = matches.getMatch(3);
        RegexMatch m_5 = matches.getMatch(4);
        RegexMatch m_6 = matches.getMatch(5);

        Assertions.assertEquals("", m_1.getBefore(), "m_1: getBefore()");
        Assertions.assertEquals("d", m_1.getAfter(), "m_1: getAfter()");
        Assertions.assertEquals("a", m_1.getMatch(), "m_1: getMatch()");

        // "adas ist ein matchAllTesti"
        Assertions.assertEquals("d", m_2.getBefore(), "m_2: getBefore()");
        Assertions.assertEquals("s ", m_2.getAfter(), "m_2: getAfter()");
        Assertions.assertEquals("a", m_2.getMatch(), "m_2: getMatch()");

        Assertions.assertEquals("s ", m_3.getBefore(), "m_3: getBefore()");
        Assertions.assertEquals("st e", m_3.getAfter(), "m_3: getAfter()");
        Assertions.assertEquals("i", m_3.getMatch(), "m_3: getMatch()");

        Assertions.assertEquals("st e", m_4.getBefore(), "m_4: getBefore()");
        Assertions.assertEquals("n m", m_4.getAfter(), "m_4: getAfter()");
        Assertions.assertEquals("i", m_4.getMatch(), "m_4: getMatch()");

        Assertions.assertEquals("n m", m_5.getBefore(), "m_5: getBefore()");
        Assertions.assertEquals("tchAllTest", m_5.getAfter(), "m_5: getAfter()");
        Assertions.assertEquals("a", m_5.getMatch(), "m_5: getMatch()");

        Assertions.assertEquals("tchAllTest", m_6.getBefore(), "m_6: getBefore()");
        Assertions.assertEquals("", m_6.getAfter(), "m_6: getAfter()");
        Assertions.assertEquals("i", m_6.getMatch(), "m_6: getMatch()");
    }
}