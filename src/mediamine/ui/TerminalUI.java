package mediamine.ui;

public final class TerminalUI {

    public static final String TL = "+";
    public static final String TR = "+";
    public static final String BL = "+";
    public static final String BR = "+";
    public static final String H  = "-";
    public static final String V  = "|";
    public static final String TJ = "+";
    public static final String BJ = "+";
    public static final String LJ = "+";
    public static final String RJ = "+";
    public static final String CR = "+";

    public static final String BULLET = "-";
    public static final String BAR_FULL = "[";
    public static final String BAR_OPEN = "]";
    public static final String BAR_CHAR = "=";
    public static final String BAR_EMPTY = "-";

    public static String box(String title, String body) {
        int bodyWidth = 76;
        int titleLen = title.length();
        int titlePad = Math.max(0, (bodyWidth - titleLen) / 2);

        StringBuilder sb = new StringBuilder();
        sb.append(TL).append(repeat(H, bodyWidth + 2)).append(TR).append("\n");
        sb.append(V).append(spaces(titlePad + 1)).append(title.toUpperCase())
          .append(spaces(bodyWidth + 1 - titlePad - titleLen)).append(V).append("\n");
        sb.append(TJ).append(repeat(H, bodyWidth + 2)).append(RJ).append("\n");
        for (String line : body.split("\n")) {
            sb.append(V).append(" ").append(pad(line, bodyWidth)).append(" ").append(V).append("\n");
        }
        sb.append(BL).append(repeat(H, bodyWidth + 2)).append(BR);
        return sb.toString();
    }

    public static String menuBox(String[] items) {
        int w = 76;
        StringBuilder sb = new StringBuilder();
        sb.append(TL).append(repeat(H, w + 2)).append(TR).append("\n");
        sb.append(V).append(spaces((w - 13) / 2 + 1)).append("MEDIAMINE MENU")
          .append(spaces(w + 1 - ((w - 13) / 2 + 13))).append(V).append("\n");
        sb.append(TJ).append(repeat(H, w + 2)).append(RJ).append("\n");
        for (String it : items) {
            sb.append(V).append(" ").append(pad(it, w)).append(" ").append(V).append("\n");
        }
        sb.append(BL).append(repeat(H, w + 2)).append(BR);
        return sb.toString();
    }

    public static String stepDone(String label, String status) {
        int total = 62;
        int lbl = label.length();
        int dots = Math.max(2, total - lbl - status.length());
        return label + repeat(".", dots) + " " + status;
    }

    public static String sectionLine() {
        return repeat(H, 78);
    }

    public static String sectionHeader(String text) {
        return sectionLine() + "\n" + text.toUpperCase() + "\n" + sectionLine();
    }

    public static String numberedItem(int idx, String title, String meta) {
        String idxStr = "[" + String.format("%02d", idx) + "]";
        return idxStr + " " + title + "\n" + "     " + meta;
    }

    public static String bullet(String s) {
        return "  " + "-" + " " + s;
    }

    public static String labelValue(String label, String value) {
        return label + "\n" + value;
    }

    public static String barRow(String label, int value, int max, int maxBar) {
        int len = (max == 0) ? 0 : (int) Math.round(1.0 * value * maxBar / max);
        String bar = BAR_FULL + repeat(BAR_CHAR, len) + repeat(BAR_EMPTY, Math.max(0, maxBar - len)) + BAR_OPEN;
        return pad(label, 18) + " " + bar + " " + value;
    }

    public static String barRowWide(String label, long value, long max, int maxBar) {
        int len = (max == 0L) ? 0 : (int) Math.round(1.0 * value * maxBar / max);
        String bar = BAR_FULL + repeat(BAR_CHAR, len) + repeat(BAR_EMPTY, Math.max(0, maxBar - len)) + BAR_OPEN;
        return pad(label, 18) + " " + bar + " " + String.format("%,d", value);
    }

    public static String twoColRow(String a, String b, int colWidth) {
        return pad(a, colWidth) + pad(b, colWidth);
    }

    public static String pad(String s, int n) {
        if (s == null) s = "";
        int vl = s.length();
        if (vl >= n) return s.substring(0, n);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) sb.append(' ');
        return sb.toString();
    }

    public static String padR(String s, int n) {
        if (s == null) s = "";
        int vl = s.length();
        if (vl >= n) return s.substring(0, n);
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n - vl) sb.append(' ');
        sb.append(s);
        return sb.toString();
    }

    public static String spaces(int n) {
        if (n < 0) n = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(' ');
        return sb.toString();
    }

    public static String repeat(String s, int n) {
        if (n < 0) n = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    public static String prompt(String text) {
        return text + ":" + "\n> ";
    }
}
