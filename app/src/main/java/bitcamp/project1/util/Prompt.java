package bitcamp.project1.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Prompt {

    private static Scanner scanner = new Scanner(System.in);

    public static String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String inputString(String prompt) {
        return input(prompt);
    }

    public static int inputInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(input(prompt));
            } catch (NumberFormatException e) {
                System.out.println("유효한 숫자가 아닙니다. 다시 입력하세요.");
            }
        }
    }

    public static long inputLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(input(prompt));
            } catch (NumberFormatException e) {
                System.out.println("유효한 숫자가 아닙니다. 다시 입력하세요.");
            }
        }
    }

    public static LocalDate inputDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(input(prompt));
            } catch (DateTimeParseException e) {
                System.out.println("유효한 날짜 형식이 아닙니다. 다시 입력하세요 (YYYY-MM-DD).");
            }
        }
    }

    public static void close() {
        scanner.close();
    }
}
