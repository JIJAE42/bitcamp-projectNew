package bitcamp.project1.command;

import bitcamp.project1.vo.Income;
import bitcamp.project1.util.Prompt;
import java.time.LocalDate;
import java.util.ArrayList;

public class IncomeCommand {

    private ArrayList<Income> incomeList = new ArrayList<>();

    public void executeIncomeCommand(String subMenuTitle) {
        switch (subMenuTitle) {
            case "등록":
                this.addIncome();
                break;
            case "목록":
                this.listIncomes();
                break;
            case "조회":
                this.viewIncome();
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

    public void addIncome() {
        LocalDate date = Prompt.inputDate("날짜 입력 (YYYY-MM-DD): ");
        long amount = Prompt.inputLong("금액 입력: ");
        String description = Prompt.inputString("설명 입력: ");
        incomeList.add(new Income(date, amount, description));
        System.out.println("소득이 등록되었습니다.");
    }

    public void listIncomes() {
        for (Income income : incomeList) {
            System.out.printf("%s - %,d원 - %s\n", income.getDate(), income.getAmount(), income.getDescription());
        }
    }

    public void viewIncome() {
        int index = Prompt.inputInt("조회할 소득 번호: ");
        if (index < 0 || index >= incomeList.size()) {
            System.out.println("유효한 소득 번호가 아닙니다.");
            return;
        }

        Income income = incomeList.get(index);
        System.out.printf("날짜: %s\n", income.getDate());
        System.out.printf("금액: %,d원\n", income.getAmount());
        System.out.printf("설명: %s\n", income.getDescription());
    }

    public void updateIncome() {
        int index = Prompt.inputInt("변경할 소득 번호: ");
        if (index < 0 || index >= incomeList.size()) {
            System.out.println("유효한 소득 번호가 아닙니다.");
            return;
        }

        Income income = incomeList.get(index);
        LocalDate date = Prompt.inputDate(String.format("날짜(%s): ", income.getDate()));
        long amount = Prompt.inputLong(String.format("금액(%,d원): ", income.getAmount()));
        String description = Prompt.inputString(String.format("설명(%s): ", income.getDescription()));

        income.setDate(date);
        income.setAmount(amount);
        income.setDescription(description);

        System.out.println("소득이 변경되었습니다.");
    }

    public void deleteIncome() {
        int index = Prompt.inputInt("삭제할 소득 번호: ");
        if (index < 0 || index >= incomeList.size()) {
            System.out.println("유효한 소득 번호가 아닙니다.");
            return;
        }

        incomeList.remove(index);
        System.out.println("소득이 삭제되었습니다.");
    }
}
