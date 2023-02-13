package edu.zhuravlev.busanalyzerbot.illustrator;

class HtmlStyleTelegramFormatter {
    private HtmlStyleTelegramFormatter() {};

    public static String format(String text, Styles style) {
        String result;

        switch (style) {
            case BOLD -> result = withBold(text);
            case ITALIC -> result = withItalic(text);
            case UNDERLINE -> result = withUnderline(text);
            case STRIKETHROUGH -> result = withStrikethrough(text);
            case SPOILER -> result = withSpoiler(text);
            default -> result = text;
        }

        return result;
    }

    private static String withBold(String text) {
        return "<b>" + text + "</b>";
    }

    private static String withItalic(String text) {
        return "<i>" + text + "</i>";
    }

    private static String withUnderline(String text) {
        return "<u>" + text + "</u>";
    }

    private static String withStrikethrough(String text) {
        return "<s>" + text + "</s>";
    }

    private static String withSpoiler(String text) {
        return "<tg-spoiler>" + text + "</tg-spoiler>";
    }

    public enum Styles {
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        SPOILER
    }
}
