package org.example.customspinner;

import java.util.List;

import static java.lang.Thread.sleep;

public class Spinner implements CustomSpinner {
    private final List<String> spinnerItems = List.of("\\", "/", "_");
    private final static String BACKSPACE_CHAR = "\b";

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        final List<String> spinnerItems = List.of("-", "/", "-", "\\");
        while (true) {
            for (int i = 0; ;i++) {
                System.out.print(spinnerItems.get(i % 4));
                System.out.flush();
                System.out.print(BACKSPACE_CHAR);
                System.out.flush();
            }
        }
//        String text = "Hello, world!";
//        System.out.print(text);
//
//        // Ensure the output is flushed to the terminal
//        System.out.flush();
//
//        // Simulate a backspace to delete the last character
//        System.out.print("\b \b");  // Moves back one character, prints a space (to overwrite), and moves back again
//
//        // Print a new character or empty space to replace the deleted character
//        // For example, replacing the last character with an exclamation mark:
//        System.out.print("$");
//
//        // Flush the console to ensure the changes are displayed
//        System.out.flush();
    }
}
