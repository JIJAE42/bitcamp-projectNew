package bitcamp.project1.command;

import bitcamp.project1.vo.AccountBook;
import bitcamp.project1.vo.Income;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class IncomeCommand {
    private AccountBook accountBook;

    public IncomeCommand(AccountBook accountBook) {
        this.accountBook = accountBook;
    }

    public void executeIncomeCommand(String subMenuTitle) {
        switch (subMenuTitle) {
            case "등록":
                this.addIncome();
                break;
            case "목록":
                this.listIncomes();
                break;
            case "변경":
                this.updateIncome();
                break;
            case "삭제":
                this.deleteIncome();
                break;
            default:
                System.out.printf("%s 명령을 처리할 수 없습니다.\n", subMenuTitle);
        }
    }

    private void addIncome() {
        Income income = new Income();
        try {
            income.setDate(LocalDate.parse(Prompt.inputString("날짜 (YYYY-MM-DD): ")));
        } catch (DateTimeParseException e) {
            System.out.println("날짜 유형으로 입력해주세요");
            return;
        }
        income.setAmount(Prompt.inputInt("금액: "));
        income.setSource(Prompt.inputString("출처: "));
        income.setDescription(Prompt.inputString("설명: "));
        accountBook.getIncomes().add(income);
        accountBook.addIncome(income.getAmount());
        System.out.println("수입이 등록되었습니다.");
        System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
    }

    private void listIncomes() {
        System.out.println("번호, 날짜, 금액, 출처, 설명");
        for (int i = 0; i < accountBook.getIncomes().size(); i++) {
            Income income = accountBook.getIncomes().get(i);
            System.out.printf("%d. %s, \033[31m%,d원\033[0m, %s, %s\n",
                    i + 1, income.getDate(), income.getAmount(), income.getSource(), income.getDescription());
        }
    }

    private void updateIncome() {
        int index = Prompt.inputInt("변경할 수입 번호: ") - 1;
        if (index >= 0 && index < accountBook.getIncomes().size()) {
            Income income = accountBook.getIncomes().get(index);
            int oldAmount = income.getAmount();
            try {
                income.setDate(LocalDate.parse(Prompt.inputString(String.format("날짜(%s): ", income.getDate()))));
            } catch (DateTimeParseException e) {
                System.out.println("날짜 유형으로 입력해주세요");
                return;
            }
            income.setAmount(Prompt.inputInt(String.format("금액(%d): ", income.getAmount())));
            income.setSource(Prompt.inputString(String.format("출처(%s): ", income.getSource())));
            income.setDescription(Prompt.inputString(String.format("설명(%s): ", income.getDescription())));
            accountBook.addIncome(income.getAmount() - oldAmount);
            System.out.println("수입이 변경되었습니다.");
            System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    private void deleteIncome() {
        int index = Prompt.inputInt("삭제할 수입 번호: ") - 1;
        if (index >= 0 && index < accountBook.getIncomes().size()) {
            Income income = accountBook.getIncomes().remove(index);
            accountBook.addIncome(-income.getAmount());
            System.out.println("수입이 삭제되었습니다.");
            System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }
}
