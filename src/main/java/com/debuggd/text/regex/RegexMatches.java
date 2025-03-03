package com.debuggd.text.regex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegexMatches implements Iterable<RegexMatch> {
	private ArrayList<RegexMatch> regexMatches;
	private ArrayList<String> misMatches;

	RegexMatches(ArrayList<RegexMatch> regexMatches) {
		this.regexMatches = regexMatches;
		this.misMatches = new ArrayList<>();
		RegexMatch last = null;
		for (RegexMatch match : regexMatches) {
			misMatches.add(match.getBefore());
			last = match;
		}
		if (last != null && last.getAfter() != null) {
			misMatches.add(last.getAfter());
		}
	}

	public boolean hasMatches() {
		return !regexMatches.isEmpty();
	}

	@Override
	public Iterator<RegexMatch> iterator() {
		return regexMatches.iterator();
	}

	public int size() {
		return regexMatches.size();
	}

	public RegexMatch getMatch(int index) {
		return regexMatches.get(index);
	}

	public List<String> getMisMatches() {
		return misMatches;
	}
}
