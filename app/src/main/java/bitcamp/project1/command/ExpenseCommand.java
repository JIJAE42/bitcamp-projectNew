package bitcamp.project1.command;

import bitcamp.project1.vo.Expense;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpenseCommand {

    private ArrayList<Expense> expenseList = new ArrayList<>();

    public void executeExpenseCommand(String subMenuTitle) {
        switch (subMenuTitle) {
            case "등록":
                this.addExpense();
                break;
            case "목록":
                this.listExpenses();
                break;
            case "조회":
                this.viewExpense();
                break;
            case "변경":
                this.updateExpense();
                break;
            case "삭제":
                this.deleteExpense();
                break;
            default:
                System.out.printf("%s 명령을 처리할 수 없습니다.\n", subMenuTitle);
        }
    }

    public void addExpense() {
        LocalDate date = Prompt.inputDate("날짜 입력 (YYYY-MM-DD): ");
        long amount = Prompt.inputLong("금액 입력: ");
        String description = Prompt.inputString("설명 입력: ");
        Expense.Category category = selectCategory();
        expenseList.add(new Expense(date, amount, description, category));
        System.out.println("지출이 등록되었습니다.");
    }

    private Expense.Category selectCategory() {
        System.out.println("용도 선택:");
        System.out.println("1. 주거");
        System.out.println("2. 통신");
        System.out.println("3. 교통");
        System.out.println("4. 금융");
        System.out.println("5. 식비");
        System.out.println("6. 취미");

        int categoryChoice = Prompt.inputInt("옵션을 선택하세요: ");
        switch (categoryChoice) {
            case 1: return Expense.Category.HOUSING;
            case 2: return Expense.Category.COMMUNICATION;
            case 3: return Expense.Category.TRANSPORTATION;
            case 4: return Expense.Category.FINANCE;
            case 5: return Expense.Category.FOOD;
            case 6: return Expense.Category.HOBBY;
            default: throw new IllegalArgumentException("유효한 선택이 아닙니다.");
        }
    }

    public void listExpenses() {
        for (Expense expense : expenseList) {
            System.out.printf("%s - %,d원 - %s - %s\n",
                    expense.getDate(),
                    expense.getAmount(),
                    expense.getDescription(),
                    expense.getCategory());
        }
    }

    public void viewExpense() {
        int index = Prompt.inputInt("조회할 지출 번호: ");
        if (index < 0 || index >= expenseList.size()) {
            System.out.println("유효한 지출 번호가 아닙니다.");
            return;
        }

        Expense expense = expenseList.get(index);
        System.out.printf("날짜: %s\n", expense.getDate());
        System.out.printf("금액: %,d원\n", expense.getAmount());
        System.out.printf("설명: %s\n", expense.getDescription());
        System.out.printf("카테고리: %s\n", expense.getCategory());
    }

    public void updateExpense() {
        int index = Prompt.inputInt("변경할 지출 번호: ");
        if (index < 0 || index >= expenseList.size()) {
            System.out.println("유효한 지출 번호가 아닙니다.");
            return;
        }

        Expense expense = expenseList.get(index);
        LocalDate date = Prompt.inputDate(String.format("날짜(%s): ", expense.getDate()));
        long amount = Prompt.inputLong(String.format("금액(%,d원): ", expense.getAmount()));
        String description = Prompt.inputString(String.format("설명(%s): ", expense.getDescription()));
        Expense.Category category = selectCategory();

        expense.setDate(date);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCategory(category);

        System.out.println("지출이 변경되었습니다.");
    }

    public void deleteExpense() {
        int index = Prompt.inputInt("삭제할 지출 번호: ");
        if (index < 0 || index >= expenseList.size()) {
            System.out.println("유효한 지출 번호가 아닙니다.");
            return;
        }

        expenseList.remove(index);
        System.out.println("지출이 삭제되었습니다.");
    }
}
