package dev.proststuff.utilitary.utility.data;

public interface ILanguageGeneratable {
    String getTranslationKey();
    String getTranslation(String lang);

    /**
     * Convert something like `grass_block` to `Grass Block`
     * Does not turn spaced characters into capital characters.
     * @param input Base text
     * @return Formatted text
     */
    static String format(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] parts = input.trim().split("_+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;

            result.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                result.append(part.substring(1));
            }

            if (i < parts.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
