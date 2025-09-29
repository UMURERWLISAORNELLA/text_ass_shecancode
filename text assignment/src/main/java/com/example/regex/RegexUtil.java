package com.example.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing methods for performing regular expression
 * operations.  It wraps Java's {@link Pattern} and {@link Matcher}
 * classes to simplify common tasks such as finding all matches and
 * replacing text.  By isolating regex logic here, the UI code in
 * {@code MainApp} remains clean and focused on presentation.
 */
public final class RegexUtil {
    private RegexUtil() {
        // Prevent instantiation
    }

    /**
     * Finds all non‑overlapping matches of the given pattern in the
     * supplied text.  The pattern must follow Java's regular
     * expression syntax.  A list containing each match (in the order
     * they appear in the text) is returned.  If no matches are
     * present, an empty list is returned.
     *
     * @param patternStr the regular expression pattern
     * @param text       the text to search
     * @return a list of matched substrings
     */
    public static List<String> findMatches(String patternStr, String text) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(text);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Replaces every substring of the text that matches the given
     * regular expression pattern with the specified replacement.  The
     * replacement may refer to captured groups using the $1, $2, …
     * syntax.  If the pattern is invalid, a {@link
     * java.util.regex.PatternSyntaxException} will be thrown.
     *
     * @param patternStr  the regular expression pattern
     * @param replacement the string to replace matches with
     * @param text        the original text
     * @return the text with all occurrences of the pattern replaced
     */
    public static String replace(String patternStr, String replacement, String text) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }
}