package dev.proststuff.utilitary.utility;

import java.util.Locale;

public class StringUtil {
    public static String snakeCaseToCamelCase(String text) {
        StringBuilder builder = new StringBuilder();
        builder.append(text.substring(0, 1).toUpperCase(Locale.ROOT));

        for (int i = 1; i < text.length(); i++) {
            int j = text.indexOf('_', i);

            if (j == -1) {
                builder.append(text.substring(i));
                break;
            }

            builder.append(text.substring(i, j).toLowerCase(Locale.ROOT));
            builder.append(text.substring(j + 1, j + 2).toUpperCase(Locale.ROOT));

            i = j + 1;
        }

        return builder.toString();
    }

    public static String camelCaseToSnakeCase(String text) {
        StringBuilder builder = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append('_');
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }

        if (!builder.isEmpty() && builder.charAt(0) == '_')
            builder.deleteCharAt(0);

        return builder.toString();
    }

    public static String format(String text) {
        if (text == null || text.isEmpty())
            return text;

        String normalized = text.replace('_', ' ')
                .replace('-', ' ')
                .replace('.', ' ');

        String spaced = normalized.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        spaced = spaced.trim().replaceAll("\\s+", " ");
        String[] words = spaced.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) continue;
            builder.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1)
                builder.append(word.substring(1).toLowerCase(Locale.ROOT));
            if (i < words.length - 1)
                builder.append(' ');
        }

        return builder.toString();
    }

    public static String toIdentifierPath(String text) {
        if (text == null || text.isEmpty())
            return text;

        String normalized = text.replaceAll("[\\s\\-.]+", "_");
        normalized = normalized.replaceAll("(?<=[a-z])(?=[A-Z])", "_");
        normalized = normalized.toLowerCase(Locale.ROOT);
        normalized = normalized.replaceAll("_+", "_");
        return normalized.replaceAll("^_+|_+$", "");
    }
}