package bitcamp.project1.command;

import bitcamp.project1.vo.AccountBook;
import bitcamp.project1.vo.Expense;
import bitcamp.project1.vo.Income;
import bitcamp.project1.util.Prompt;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccountBookCommand {
    private AccountBook accountBook;

    public AccountBookCommand(AccountBook accountBook) {
        this.accountBook = accountBook;
    }

    /**
     * 서브 메뉴 타이틀에 따라 해당 기능을 실행한다.
     *
     * @param subMenuTitle 서브 메뉴 타이틀
     */
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
            case "최근 1개월 거래 내역 조회":
                this.recentTransactions();
                break;
            default:
                System.out.printf("%s 명령을 처리할 수 없습니다.\n", subMenuTitle);
        }
    }

    /**
     * 사용자가 선택한 카테고리를 반환한다.
     *
     * @return 선택된 카테고리, 유효하지 않은 경우 null
     */
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

    /**
     * 선택한 카테고리에 따른 지출 내역을 조회하고 총 금액과 전체 지출 대비 비율을 출력한다.
     */
    public void typeViewList() {
        Expense.Category category = selectCategory();
        if (category == null) {
            return;
        }

        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getCategory() == category)
                .collect(Collectors.toList());

        int totalAmount = filteredExpenses.stream().mapToInt(Expense::getAmount).sum();
        int overallTotalAmount = accountBook.getExpenses().stream().mapToInt(Expense::getAmount).sum();
        double percentage = (overallTotalAmount == 0) ? 0 : (totalAmount * 100.0 / overallTotalAmount);

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }

        System.out.printf("총 금액: %,d원 (전체 지출의 %.2f%%)\n", totalAmount, percentage);
    }

    /**
     * 입력된 특정 날짜의 지출 내역을 조회한다.
     */
    public void dayViewList() {
        LocalDate date = Prompt.inputDate("조회할 날짜 (YYYY-MM-DD): ");
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().equals(date))
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    /**
     * 입력된 특정 월의 지출 내역을 조회한다.
     */
    public void monthViewList() {
        YearMonth month = YearMonth.parse(Prompt.inputString("조회할 월 (YYYY-MM): "));
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> YearMonth.from(expense.getDate()).equals(month))
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    /**
     * 입력된 특정 연도의 지출 내역을 조회한다.
     */
    public void yearViewList() {
        int year = Prompt.inputInt("조회할 연도 (YYYY): ");
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().getYear() == year)
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, %,d원, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    /**
     * 최근 1개월간의 소득과 지출 내역을 조회하여 일자 순으로 정렬하고 출력한다.
     */
    public void recentTransactions() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Income> recentIncomes = accountBook.getIncomes().stream()
                .filter(income -> income.getDate().isAfter(oneMonthAgo) || income.getDate().isEqual(oneMonthAgo))
                .collect(Collectors.toList());

        List<Expense> recentExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().isAfter(oneMonthAgo) || expense.getDate().isEqual(oneMonthAgo))
                .collect(Collectors.toList());

        List<Transaction> transactions = new ArrayList<>();
        recentIncomes.forEach(income -> transactions.add(new Transaction(income.getDate(), income.getAmount(), "소득", income.getDescription())));
        recentExpenses.forEach(expense -> transactions.add(new Transaction(expense.getDate(), expense.getAmount(), "지출", expense.getDescription())));

        transactions.sort(Comparator.comparing(Transaction::getDate));

        System.out.println("최근 1개월 거래 내역:");
        for (Transaction transaction : transactions) {
            System.out.printf("%s, %,d원, %s, %s\n",
                    transaction.getDate(), transaction.getAmount(), transaction.getType(), transaction.getDescription());
        }
    }

    /**
     * 거래 내역을 나타내기 위한 내부 클래스.
     */
    static class Transaction {
        private LocalDate date;
        private int amount;
        private String type;
        private String description;

        public Transaction(LocalDate date, int amount, String type, String description) {
            this.date = date;
            this.amount = amount;
            this.type = type;
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }

        public int getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }
}
