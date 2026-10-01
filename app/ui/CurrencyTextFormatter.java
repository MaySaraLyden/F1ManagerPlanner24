package app.ui;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

public class CurrencyTextFormatter {

    private static final int MAX_COST = 999_999_999;

    public static TextFormatter<String> create() {
        UnaryOperator<TextFormatter.Change> formatter = change -> {
            String newText = change.getControlNewText();
            String digits = newText.replaceAll("[^0-9]", "");
            
            if (digits.isEmpty()) {
                change.setRange(0, change.getControlText().length());
                change.setText("");
                change.setCaretPosition(0);
                return change;
            }
            
            long value;

            try {
                value = Long.parseLong(digits);
            } catch (NumberFormatException e) {
                return null;
            }

            if (value > MAX_COST) {
                return null;
            }

            String formatted = String.format("$%,d", value);
            int oldCaret = change.getCaretPosition();
            int digitsBeforeCaret = 0;

            for (int i = 0; i < oldCaret && i < newText.length(); i++) {
                if (Character.isDigit(newText.charAt(i))) {
                    digitsBeforeCaret++;
                }
            }

            change.setRange(0, change.getControlText().length());
            change.setText(formatted);

            int newCaret = 1;
            int digitsSeen = 0;

            while (newCaret < formatted.length()) {

                if (Character.isDigit(formatted.charAt(newCaret))) {
                    digitsSeen++;

                    if (digitsSeen == digitsBeforeCaret) {
                        newCaret++;
                        break;
                    }
                }
                newCaret++;
            }

            change.setCaretPosition(newCaret);
            change.setAnchor(newCaret);
            return change;
        };

        return new TextFormatter<String>(formatter);
    }

    public static int getValue(String text) {
        String digits = text.replaceAll("[^0-9]", "");

        if (digits.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(digits);
    }
}
