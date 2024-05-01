public class Print {
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[38;5;255m";
    public static final String ANSI_L_GRAY = "\u001B[38;5;247m";
    public static final String ANSI_D_GRAY = "\u001B[38;5;240m";
    public static final String ANSI_BLACK = "\u001B[38;5;232m";
    public static final String ANSI_B_WHITE = "\u001B[48;5;255m";
    public static final String ANSI_B_L_GRAY = "\u001B[48;5;247m";
    public static final String ANSI_B_D_GRAY = "\u001B[48;5;240m";
    public static final String ANSI_B_BLACK = "\u001B[48;5;232m";
    public static int tabLevel = 0;

    public static String errorString(String message) {
        return ANSI_RED + message + ANSI_RESET;
    }
    public static void error(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
    public static void pass(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }
    public static String passString(String message) {
        return ANSI_GREEN + message + ANSI_RESET;
    }
    public static String warningString(String message) {
        return ANSI_YELLOW + message + ANSI_RESET;
    }
    public static void header(String message) {
        System.out.println(ANSI_BOLD + ANSI_UNDERLINE + message + ANSI_RESET);
    }
    public static void bold(String message) {
        System.out.print(ANSI_BOLD + message + ANSI_RESET);
    }
    public static void inLines(Sprite sprite) {
        if (!sprite.isPlane) {
            for (int i = 0; i < sprite.bitStream.length(); i += 2) {
                byte current = Byte.valueOf(sprite.bitStream.substring(i, i + 2), 2);
                switch (current) {
                    case 0 -> System.out.print(ANSI_WHITE + ANSI_B_WHITE  + current + " " + ANSI_RESET);
                    case 1 -> System.out.print(ANSI_L_GRAY + ANSI_B_L_GRAY + current + " " + ANSI_RESET);
                    case 2 -> System.out.print(ANSI_D_GRAY + ANSI_B_D_GRAY + current + " " + ANSI_RESET);
                    case 3 -> System.out.print(ANSI_BLACK + ANSI_B_BLACK + current + " " + ANSI_RESET);
                }
                if ((i / 2 % sprite.image.getWidth()) == sprite.image.getWidth() - 1) System.out.println();
            }
        } else {
            for (int i = 0; i < sprite.bitStream.length(); i += 1) {
                byte current = Byte.valueOf(sprite.bitStream.substring(i, i + 1), 2);
                switch (current) {
                    case 0 -> System.out.print(ANSI_WHITE + ANSI_B_WHITE + current + " " + ANSI_RESET);
                    case 1 -> System.out.print(ANSI_BLACK + ANSI_B_BLACK + current + " " + ANSI_RESET);
                }
                if (i % sprite.image.getWidth() == sprite.image.getWidth() - 1) System.out.println();
            }
        }
        System.out.println();
    }
    public static String tabString(){
        return "\t".repeat(tabLevel);
    }
    public static String tabUp(){
        return "\t".repeat(++tabLevel);
    }
    public static String tabDown(){
        return "\t".repeat(tabLevel--);
    }
    public static void tabReset(){
        tabLevel = 0;
    }
}
