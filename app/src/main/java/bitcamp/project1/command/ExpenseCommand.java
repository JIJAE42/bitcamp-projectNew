package bitcamp.project1.command;

import bitcamp.project1.vo.AccountBook;
import bitcamp.project1.vo.Expense;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;

public class ExpenseCommand {
    private AccountBook accountBook = new AccountBook();

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

    private void addExpense() {
        Expense expense = new Expense();
        expense.setDate(LocalDate.parse(Prompt.inputString("날짜 (YYYY-MM-DD): ")));
        expense.setAmount(Prompt.inputInt("금액: "));
        expense.setCategory(selectCategory());
        expense.setDescription(Prompt.inputString("설명: "));
        accountBook.getExpenses().add(expense);
        System.out.println("지출이 등록되었습니다.");
    }

    private void listExpenses() {
        for (Expense expense : accountBook.getExpenses()) {
            System.out.printf("%s, %,d원, %s, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getCategory(), expense.getDescription());
        }
    }

    private void viewExpense() {
        int index = Prompt.inputInt("조회할 지출 번호: ");
        if (index >= 0 && index < accountBook.getExpenses().size()) {
            Expense expense = accountBook.getExpenses().get(index);
            System.out.printf("날짜: %s\n", expense.getDate());
            System.out.printf("금액: %,d원\n", expense.getAmount());
            System.out.printf("카테고리: %s\n", expense.getCategory());
            System.out.printf("설명: %s\n", expense.getDescription());
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    private void updateExpense() {
        int index = Prompt.inputInt("변경할 지출 번호: ");
        if (index >= 0 && index < accountBook.getExpenses().size()) {
            Expense expense = accountBook.getExpenses().get(index);
            expense.setDate(LocalDate.parse(Prompt.inputString(String.format("날짜(%s): ", expense.getDate()))));
            expense.setAmount(Prompt.inputInt(String.format("금액(%d): ", expense.getAmount())));
            expense.setCategory(selectCategory());
            expense.setDescription(Prompt.inputString(String.format("설명(%s): ", expense.getDescription())));
            System.out.println("지출이 변경되었습니다.");
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    private void deleteExpense() {
        int index = Prompt.inputInt("삭제할 지출 번호: ");
        if (index >= 0 && index < accountBook.getExpenses().size()) {
            accountBook.getExpenses().remove(index);
            System.out.println("지출이 삭제되었습니다.");
        } else {
            System.out.println("유효하지 않은 번호입니다.");
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
                return Expense.Category.HOUSING;
            case 2:
                return Expense.Category.COMMUNICATION;
            case 3:
                return Expense.Category.TRANSPORTATION;
            case 4:
                return Expense.Category.FINANCE;
            case 5:
                return Expense.Category.FOOD;
            case 6:
                return Expense.Category.HOBBY;
            default:
                System.out.println("유효한 선택이 아닙니다.");
                return null;
        }
    }
}
