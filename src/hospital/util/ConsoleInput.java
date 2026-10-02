package hospital.util;

import java.util.Scanner;

// Wraps Scanner so the rest of the program reads input safely.
// We ALWAYS use nextLine(), which avoids the famous nextInt() "skipped input" bug.
public class ConsoleInput {

    private Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // keeps asking until the user types a valid whole number
    public int readInt(String prompt) {
        while (true) {
            String text = readLine(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter digits only.");
            }
        }
    }

    // METHOD OVERLOADING: same name, but also checks the number is between min and max
    public int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("  ! Choose a number between " + min + " and " + max + ".");
        }
    }

    public void close() {
        scanner.close();
    }
}