package bitcamp.project1.command;

import bitcamp.project1.vo.AccountBook;
import bitcamp.project1.vo.Income;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;

public class IncomeCommand {
    private AccountBook accountBook = new AccountBook();

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
        income.setDate(LocalDate.parse(Prompt.inputString("날짜 (YYYY-MM-DD): ")));
        income.setAmount(Prompt.inputInt("금액: "));
        income.setSource(Prompt.inputString("출처: "));
        income.setDescription(Prompt.inputString("설명: "));
        accountBook.getIncomes().add(income);
        System.out.println("소득이 등록되었습니다.");
    }

    private void listIncomes() {
        System.out.println("번호, 날짜, 금액, 출처, 설명");
        for (int i = 0; i < accountBook.getIncomes().size(); i++) {
            Income income = accountBook.getIncomes().get(i);
            System.out.printf("%d. %s, %,d원, %s, %s\n",
                    i + 1, income.getDate(), income.getAmount(), income.getSource(), income.getDescription());
        }
    }

    private void updateIncome() {
        int index = Prompt.inputInt("변경할 소득 번호: ") - 1 ;
        if (index >= 0 && index < accountBook.getIncomes().size()) {
            Income income = accountBook.getIncomes().get(index);
            income.setDate(LocalDate.parse(Prompt.inputString(String.format("날짜(%s): ", income.getDate()))));
            income.setAmount(Prompt.inputInt(String.format("금액(%d): ", income.getAmount())));
            income.setSource(Prompt.inputString(String.format("출처(%s): ", income.getSource())));
            income.setDescription(Prompt.inputString(String.format("설명(%s): ", income.getDescription())));
            System.out.println("소득이 변경되었습니다.");
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    private void deleteIncome() {
        int index = Prompt.inputInt("삭제할 소득 번호: ") - 1 ;
        if (index >= 0 && index < accountBook.getIncomes().size()) {
            accountBook.getIncomes().remove(index);
            System.out.println("소득이 삭제되었습니다.");
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }
}
