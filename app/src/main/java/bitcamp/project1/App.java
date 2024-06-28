package bitcamp.project1;

import bitcamp.project1.command.AccountBookCommand;
import bitcamp.project1.command.ExpenseCommand;
import bitcamp.project1.command.IncomeCommand;
import bitcamp.project1.util.Prompt;

public class App {

    String[] mainMenus = {"소득", "지출", "지출 관리", "거래 내역 조회", "종료"};
    String[][] subMenus = {
            {"등록", "목록", "변경", "삭제"},
            {"등록", "목록", "변경", "삭제"},
            {"용도별 조회", "일별 조회", "월별 조회", "연도별 조회"}
    };

    IncomeCommand incomeCommand = new IncomeCommand();
    ExpenseCommand expenseCommand = new ExpenseCommand();
    AccountBookCommand accountBookCommand = new AccountBookCommand();

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
        for (int i = 0; i < mainMenus.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), mainMenus[i]);
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
                        case "소득":
                            incomeCommand.executeIncomeCommand(subMenuTitle);
                            break;
                        case "지출":
                            expenseCommand.executeExpenseCommand(subMenuTitle);
                            break;
                        case "지출 관리":
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
}
