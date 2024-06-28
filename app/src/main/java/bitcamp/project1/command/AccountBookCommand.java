package bitcamp.project1.command;

import bitcamp.project1.vo.Expense;
import bitcamp.project1.util.Prompt;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountBookCommand {
    protected ArrayList<Expense> expenseList = new ArrayList<>();

    public void executeAccountBookCommand(String subMenuTitle) {
        switch (subMenuTitle) {
            case "용도별 조회":
                this.typeViewList();
                break;
            case "일별 조회":
                this.dayViewList();
                break;
            case "월별 조회":
                this.monthViewList();
                break;
            case "연도별 조회":
                this.yearViewList();
                break;
            default:
                System.out.printf("%s 명령을 처리할 수 없습니다.\n", subMenuTitle);
        }
    }

    private Expense.Category selectCategory() {
        System.out.println("1. 주거");
        System.out.println("2. 통신");
        System.out.println("3. 교통");
        System.out.println("4. 금융");
        System.out.println("5. 식비");
        System.out.println("6. 취미");

        int categoryChoice = Prompt.inputInt("카테고리를 선택하세요: ");
        switch (categoryChoice) {
            case 1:
                return Expense.Category.주거;
            case 2:
                return Expense.Category.통신;
            case 3:
                return Expense.Category.교통;
            case 4:
                return Expense.Category.금융;
            case 5:
                return Expense.Category.식비;
            case 6:
                return Expense.Category.취미;
            default:
                System.out.println("유효한 선택이 아닙니다.");
                return null;
        }
    }

    public void typeViewList() {
        Expense.Category category = selectCategory();
        if (category == null) {
            return;
        }

        List<Expense> filteredExpenses = expenseList.stream()
                .filter(expense -> expense.getCategory() == category)
                .toList();

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void dayViewList() {
        LocalDate date = Prompt.inputDate("조회할 날짜 (YYYY-MM-DD): ");
        List<Expense> filteredExpenses = expenseList.stream()
                .filter(expense -> expense.getDate().equals(date))
                .toList();

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void monthViewList() {
        YearMonth month = YearMonth.parse(Prompt.inputString("조회할 월 (YYYY-MM): "));
        List<Expense> filteredExpenses = expenseList.stream()
                .filter(expense -> YearMonth.from(expense.getDate()).equals(month))
                .toList();

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void yearViewList() {
        int year = Prompt.inputInt("조회할 연도 (YYYY): ");
        List<Expense> filteredExpenses = expenseList.stream()
                .filter(expense -> expense.getDate().getYear() == year)
                .toList();

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }
}
