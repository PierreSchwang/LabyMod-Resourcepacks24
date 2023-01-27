package de.pierreschwang.labymod.resourcepacks.api.util;

public class StringUtil {

    public static String capitalize(String string) {
        StringBuilder builder = new StringBuilder();
        boolean startOfWord = true;
        for (char c : string.toCharArray()) {
            if (startOfWord) {
                builder.append(Character.toUpperCase(c));
                startOfWord = false;
            } else {
                builder.append(c);
            }
            if (Character.isWhitespace(c)) {
                startOfWord = true;
            }
        }
        return builder.toString();
    }

}
