package vc4.api.util;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {

	boolean inQuotes = false;
	boolean q;

	public StringSplitter(boolean leaveQuotes) {
		q = leaveQuotes;
	}

	public static String[] splitString(String s, boolean leaveQuotes) {
		List<String> parts = new ArrayList<String>();
		boolean insideQuotes = false;
		StringBuilder lastWord = new StringBuilder();
		for (int dofor = 0; dofor < s.length(); ++dofor) {
			char c = s.charAt(dofor);
			if (c == ' ' && !insideQuotes) {
				if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
				lastWord = new StringBuilder();
			} else if (c == '"') {
				if (insideQuotes && leaveQuotes) lastWord.append(c);
				if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
				lastWord = new StringBuilder();
				if (!insideQuotes && leaveQuotes) lastWord.append(c);
				insideQuotes = !insideQuotes;

			} else {
				lastWord.append(c);
			}
		}
		if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
		String[] result = new String[parts.size()];
		result = parts.toArray(result);
		return result;
	}

	public String[] split(String s) {
		boolean leaveQuotes = q;
		List<String> parts = new ArrayList<String>();
		boolean insideQuotes = inQuotes;
		StringBuilder lastWord = new StringBuilder();
		for (int dofor = 0; dofor < s.length(); ++dofor) {
			char c = s.charAt(dofor);
			if (c == ' ' && !insideQuotes) {
				if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
				lastWord = new StringBuilder();
			} else if (c == '"') {
				if (insideQuotes && leaveQuotes) lastWord.append(c);
				if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
				lastWord = new StringBuilder();
				if (!insideQuotes && leaveQuotes) lastWord.append(c);
				insideQuotes = !insideQuotes;

			} else {
				lastWord.append(c);
			}
		}
		if (!lastWord.toString().trim().isEmpty()) parts.add(lastWord.toString());
		inQuotes = insideQuotes;
		String[] result = new String[parts.size()];
		result = parts.toArray(result);
		return result;
	}

	public void reset() {
		inQuotes = false;
	}

	public boolean isInsideQuotes() {
		return inQuotes;
	}
}
