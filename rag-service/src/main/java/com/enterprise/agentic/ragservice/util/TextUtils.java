package com.enterprise.agentic.ragservice.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class TextUtils {

    private TextUtils() {
        // Utility class
    }

    //  Remove extra spaces, normalize case
    public static String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }

    //  Clean log message (remove noise)
    public static String cleanLog(String log) {
        if (log == null) {
            return "";
        }

        return log
                .replaceAll("\\n", " ")
                .replaceAll("\\r", " ")
                .replaceAll("\\t", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    //  Remove timestamps (common log pattern)
    public static String removeTimestamps(String log) {
        if (log == null) return "";

        return log.replaceAll(
                "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*?\\s",
                ""
        );
    }

    //  Remove special characters (keep meaningful text)
    public static String removeSpecialCharacters(String text) {
        if (text == null) return "";

        return text.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    //  Extract keywords (basic version)
    public static String extractKeywords(String text) {
        if (text == null) return "";

        return text
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\b(the|is|at|which|on|a|an)\\b", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    //  Truncate long logs (for embedding safety)
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";

        return text.length() <= maxLength
                ? text
                : text.substring(0, maxLength);
    }

    //  Clean text by removing newlines, tabs, and normalizing whitespace
    public static String cleanText(String text) {
        if (text == null) return null;
        if (text.isEmpty()) return "";

        return text
                .replaceAll("\\n", " ")
                .replaceAll("\\r", " ")
                .replaceAll("\\t", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    //  Full preprocessing pipeline (BEST PRACTICE)
    public static String preprocessLog(String log) {

        String cleaned = cleanLog(log);
        cleaned = removeTimestamps(cleaned);
        cleaned = normalize(cleaned);

        return truncate(cleaned, 1000); // limit for embeddings
    }
}