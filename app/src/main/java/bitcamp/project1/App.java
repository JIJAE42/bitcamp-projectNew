package bitcamp.project1;

import bitcamp.project1.command.AccountBookCommand;
import bitcamp.project1.command.ExpenseCommand;
import bitcamp.project1.command.IncomeCommand;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.vo.AccountBook;

import bitcamp.project1.vo.Expense;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

    String[] mainMenus = {"수입", "지출", "지출 관리", "거래 내역 조회", "종료"};
    String[][] subMenus = {
            {"등록", "목록", "변경", "삭제"},
            {"등록", "목록", "변경", "삭제"},
            {"용도별 조회", "일별 조회", "월별 조회", "연도별 조회", "날짜 지정 조회"},
            {"최근 1개월 거래 내역 조회", "자산 그래프"}
    };

    AccountBook accountBook = new AccountBook();
    IncomeCommand incomeCommand = new IncomeCommand(accountBook);
    ExpenseCommand expenseCommand = new ExpenseCommand(accountBook);
    AccountBookCommand accountBookCommand = new AccountBookCommand(accountBook);

    public static void main(String[] args) {
        new App().execute();
    }

    void execute() {
        printMenu();

        while (true) {
            try {
                String command = Prompt.inputString("메인> ");
                if (command.equals("menu")) {
                    printMenu();
                } else {
                    int menuNo = Integer.parseInt(command);
                    if (menuNo == 5) {
                        break;
                    }
                    String menuTitle = getMenuTitle(menuNo, mainMenus);
                    if (menuTitle == null) {
                        System.out.println("유효한 메뉴 번호가 아닙니다.");
                    } else {
                        processMenu(menuTitle, subMenus[menuNo - 1]);
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }

        System.out.println("종료합니다.");
        Prompt.close();
    }

    void printMenu() {
        System.out.println("---------------");
        System.out.println("[가계부]");
        System.out.println();
        printCurrentMonthStatus();
        System.out.println("---------------");
        for (int i = 0; i < mainMenus.length; i++) {
            if (i == mainMenus.length - 1) {
                System.out.printf("\033[31m%d. %s\033[0m\n", (i + 1), mainMenus[i]);
            } else {
                System.out.printf("%d. %s\n", (i + 1), mainMenus[i]);
            }
        }
        System.out.println("---------------");
    }

    void printSubMenu(String menuTitle, String[] menus) {
        System.out.printf("[%s]\n", menuTitle);
        for (int i = 0; i < menus.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), menus[i]);
        }
        System.out.println("9. 이전");
    }

    boolean isValidateMenu(int menuNo, String[] menus) {
        return menuNo >= 1 && menuNo <= menus.length;
    }

    String getMenuTitle(int menuNo, String[] menus) {
        return isValidateMenu(menuNo, menus) ? menus[menuNo - 1] : null;
    }

    void processMenu(String menuTitle, String[] menus) {
        printSubMenu(menuTitle, menus);
        while (true) {
            String command = Prompt.inputString(String.format("메인/%s> ", menuTitle));
            if (command.equals("menu")) {
                printSubMenu(menuTitle, menus);
                continue;
            } else if (command.equals("9")) {
                break;
            }

            try {
                int menuNo = Integer.parseInt(command);
                String subMenuTitle = getMenuTitle(menuNo, menus);
                if (subMenuTitle == null) {
                    System.out.println("유효한 메뉴 번호가 아닙니다.");
                } else {
                    switch (menuTitle) {
                        case "수입":
                            incomeCommand.executeIncomeCommand(subMenuTitle);
                            break;
                        case "지출":
                            expenseCommand.executeExpenseCommand(subMenuTitle);
                            break;
                        case "지출 관리":
                            accountBookCommand.executeAccountBookCommand(subMenuTitle);
                            break;
                        case "거래 내역 조회":
                            accountBookCommand.executeAccountBookCommand(subMenuTitle);
                            break;
                        default:
                            System.out.printf("%s 메뉴의 명령을 처리할 수 없습니다.\n", menuTitle);
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }
    }

    void printCurrentMonthStatus() {
        LocalDate now = LocalDate.now();
        int totalBalance = accountBook.getBalance();
        int currentMonthIncome = accountBook.getMonthlyIncome(now);
        int previousMonthIncome = accountBook.getPreviousMonthIncome(now);
        double incomeIncreaseRate = (previousMonthIncome == 0) ? 0 : ((currentMonthIncome - previousMonthIncome) * 100.0 / previousMonthIncome);
        int currentMonthExpenses = accountBook.getMonthlyExpenses(now);
        int previousMonthExpenses = accountBook.getPreviousMonthExpenses(now);
        int cashExpenses = accountBook.getCashExpenses();
        int cardExpenses = accountBook.getCardExpenses();

        Expense.Category highestCategory = getHighestExpenseCategory(now);

        System.out.println("[이번 달 가계 현황]");
        System.out.printf("총 잔액: %,d원\n", totalBalance);
        System.out.println();
        System.out.println("\033[31m수입\033[0m - ");
        System.out.printf("\033[31m이달의 수입: %,d원\033[0m\n", currentMonthIncome);
        System.out.printf("\033[31m저번 달 수입: %,d원\033[0m\n", previousMonthIncome);
        String increaseRateColor = incomeIncreaseRate >= 0 ? "\033[31m" : "\033[34m";
        System.out.printf("%s전월 비교 증가율: %.2f%%\033[0m\n", increaseRateColor, incomeIncreaseRate);
        System.out.println();
        System.out.println("\033[34m지출\033[0m - ");
        System.out.printf("\033[34m현금 지출: %,d원\033[0m\n", cashExpenses);
        System.out.printf("\033[34m카드 지출: %,d원\033[0m\n", cardExpenses);
        System.out.println();
        System.out.printf("\033[34m저번 달 보다 %,d원 %s 지출했어요!\033[0m\n", Math.abs(currentMonthExpenses - previousMonthExpenses),
                currentMonthExpenses > previousMonthExpenses ? "더" : "덜");
        if (highestCategory != null) {
            int highestCategoryAmount = accountBook.getExpenses().stream()
                    .filter(expense -> expense.getCategory() == highestCategory &&
                            expense.getDate().getYear() == now.getYear() &&
                            expense.getDate().getMonth() == now.getMonth())
                    .mapToInt(Expense::getAmount).sum();
            System.out.printf("\033[34m\"%s\"에 %,d원으로 가장 많이 지출했어요!\033[0m\n", highestCategory, highestCategoryAmount);
        }
    }

    private Expense.Category getHighestExpenseCategory(LocalDate now) {
        Map<Expense.Category, Integer> expenseCategoryTotals = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().getYear() == now.getYear() &&
                        expense.getDate().getMonth() == now.getMonth())
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingInt(Expense::getAmount)));

        return expenseCategoryTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
