package bitcamp.project1.command;

import bitcamp.project1.vo.AccountBook;
import bitcamp.project1.vo.Expense;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ExpenseCommand {
    private AccountBook accountBook;

    public ExpenseCommand(AccountBook accountBook) {
        this.accountBook = accountBook;
    }

    public void executeExpenseCommand(String subMenuTitle) {
        switch (subMenuTitle) {
            case "등록":
                this.addExpense();
                break;
            case "목록":
                this.listExpenses();
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
        try {
            expense.setDate(LocalDate.parse(Prompt.inputString("날짜 (YYYY-MM-DD): ")));
        } catch (DateTimeParseException e) {
            System.out.println("날짜 유형으로 입력해주세요");
            return;
        }
        expense.setAmount(Prompt.inputInt("금액: "));
        expense.setCategory(selectCategory());
        expense.setDescription(Prompt.inputString("설명: "));
        expense.setType(selectType());
        accountBook.getExpenses().add(expense);
        accountBook.addExpense(expense.getAmount());
        System.out.println("지출이 등록되었습니다.");
        System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
    }

    private void listExpenses() {
        System.out.println("번호, 날짜, 금액, 분류, 설명, 유형");
        for (int i = 0; i < accountBook.getExpenses().size(); i++) {
            Expense expense = accountBook.getExpenses().get(i);
            System.out.printf("%d. %s, \033[34m%,d원\033[0m, %s, %s, %s\n",
                    i + 1, expense.getDate(), expense.getAmount(), expense.getCategory(), expense.getDescription(), expense.getType());
        }
    }

    private void updateExpense() {
        int index = Prompt.inputInt("변경할 지출 번호: ") - 1;
        if (index >= 0 && index < accountBook.getExpenses().size()) {
            Expense expense = accountBook.getExpenses().get(index);
            int oldAmount = expense.getAmount();
            try {
                expense.setDate(LocalDate.parse(Prompt.inputString(String.format("날짜(%s): ", expense.getDate()))));
            } catch (DateTimeParseException e) {
                System.out.println("날짜 유형으로 입력해주세요");
                return;
            }
            expense.setAmount(Prompt.inputInt(String.format("금액(%d): ", expense.getAmount())));
            expense.setCategory(selectCategory());
            expense.setDescription(Prompt.inputString(String.format("설명(%s): ", expense.getDescription())));
            expense.setType(selectType());
            accountBook.addExpense(expense.getAmount() - oldAmount);
            System.out.println("지출이 변경되었습니다.");
            System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    private void deleteExpense() {
        int index = Prompt.inputInt("삭제할 지출 번호: ") - 1;
        if (index >= 0 && index < accountBook.getExpenses().size()) {
            Expense expense = accountBook.getExpenses().remove(index);
            accountBook.addExpense(-expense.getAmount());
            System.out.println("지출이 삭제되었습니다.");
            System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
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

    private Expense.Type selectType() {
        System.out.println("1. 현금");
        System.out.println("2. 카드");

        int typeChoice = Prompt.inputInt("유형을 선택하세요: ");
        switch (typeChoice) {
            case 1:
                return Expense.Type.CASH;
            case 2:
                return Expense.Type.CARD;
            default:
                System.out.println("유효한 선택이 아닙니다.");
                return null;
        }
    }
}
