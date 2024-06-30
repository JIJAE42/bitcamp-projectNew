package bitcamp.project1.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Prompt {
    private static Scanner scanner = new Scanner(System.in);

    public static String inputString(String title) {
        System.out.print(title);
        return scanner.nextLine();
    }

    public static int inputInt(String title) {
        while (true) {
            try {
                return Integer.parseInt(inputString(title));
            } catch (NumberFormatException e) {
                System.out.println("유효한 숫자를 입력하세요.");
            }
        }
    }

    public static long inputLong(String title) {
        while (true) {
            try {
                return Long.parseLong(inputString(title));
            } catch (NumberFormatException e) {
                System.out.println("유효한 숫자를 입력하세요.");
            }
        }
    }

    public static LocalDate inputDate(String title) {
        while (true) {
            try {
                return LocalDate.parse(inputString(title));
            } catch (DateTimeParseException e) {
                System.out.println("날짜 유형으로 입력해주세요");
            }
        }
    }

    public static void close() {
        scanner.close();
    }
}
