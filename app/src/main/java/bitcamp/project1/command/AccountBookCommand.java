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
            case "날짜 지정 조회":
                this.dateRangeViewList();
                break;
            case "최근 1개월 거래 내역 조회":
                this.recentTransactions();
                break;
            default:
                System.out.printf("%s 명령을 처리할 수 없습니다.\n", subMenuTitle);
        }
    }

    private Expense.Category selectCategory() {
        System.out.println("0. 전체 조회");
        System.out.println("1. 주거");
        System.out.println("2. 통신");
        System.out.println("3. 교통");
        System.out.println("4. 금융");
        System.out.println("5. 식비");
        System.out.println("6. 취미");

        int categoryChoice = Prompt.inputInt("카테고리를 선택하세요: ");
        switch (categoryChoice) {
            case 0:
                return null; // 전체 조회
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
        System.out.println("1. 전체 조회");
        System.out.println("2. 현금 조회");
        System.out.println("3. 카드 조회");

        int viewTypeChoice = Prompt.inputInt("조회 유형을 선택하세요: ");
        List<Expense> filteredExpenses;
        switch (viewTypeChoice) {
            case 1:
                filteredExpenses = accountBook.getExpenses(); // 전체 조회
                break;
            case 2:
                filteredExpenses = accountBook.getExpenses().stream()
                        .filter(expense -> expense.getType() == Expense.Type.CASH)
                        .collect(Collectors.toList());
                break;
            case 3:
                filteredExpenses = accountBook.getExpenses().stream()
                        .filter(expense -> expense.getType() == Expense.Type.CARD)
                        .collect(Collectors.toList());
                break;
            default:
                System.out.println("유효한 선택이 아닙니다.");
                return;
        }

        if (viewTypeChoice == 1) {
            viewAllExpensesByCategory(filteredExpenses);
        } else {
            viewExpensesByCategoryWithPercentage(filteredExpenses, viewTypeChoice == 2 ? "현금" : "카드");
        }
    }

    private void viewAllExpensesByCategory(List<Expense> expenses) {
        int overallTotalAmount = expenses.stream().mapToInt(Expense::getAmount).sum();
        for (Expense.Category category : Expense.Category.values()) {
            int totalAmount = expenses.stream()
                    .filter(expense -> expense.getCategory() == category)
                    .mapToInt(Expense::getAmount)
                    .sum();
            double percentage = (overallTotalAmount == 0) ? 0 : (totalAmount * 100.0 / overallTotalAmount);
            System.out.printf("%s: %,d원 (%.2f%%)\n", category, totalAmount, percentage);
        }

        System.out.printf("전체 잔액: %,d원\n", overallTotalAmount);
    }

    private void viewExpensesByCategoryWithPercentage(List<Expense> expenses, String type) {
        int overallTotalAmount = expenses.stream().mapToInt(Expense::getAmount).sum();
        for (Expense.Category category : Expense.Category.values()) {
            int totalAmount = expenses.stream()
                    .filter(expense -> expense.getCategory() == category)
                    .mapToInt(Expense::getAmount)
                    .sum();
            double percentage = (overallTotalAmount == 0) ? 0 : (totalAmount * 100.0 / overallTotalAmount);
            System.out.printf("%s: %,d원 (%.2f%%)\n", category, totalAmount, percentage);
        }

        System.out.printf("%s 전체 잔액: %,d원\n", type, overallTotalAmount);
    }

    public void dayViewList() {
        LocalDate date = Prompt.inputDate("조회할 날짜 (YYYY-MM-DD): ");
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().equals(date))
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, \033[34m%,d원\033[0m, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void monthViewList() {
        YearMonth month = YearMonth.parse(Prompt.inputString("조회할 월 (YYYY-MM): "));
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> YearMonth.from(expense.getDate()).equals(month))
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, \033[34m%,d원\033[0m, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void yearViewList() {
        int year = Prompt.inputInt("조회할 연도 (YYYY): ");
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().getYear() == year)
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, \033[34m%,d원\033[0m, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void dateRangeViewList() {
        LocalDate startDate = Prompt.inputDate("시작 날짜 (YYYY-MM-DD): ");
        LocalDate endDate = Prompt.inputDate("종료 날짜 (YYYY-MM-DD): ");
        List<Expense> filteredExpenses = accountBook.getExpenses().stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());

        for (Expense expense : filteredExpenses) {
            System.out.printf("%s, \033[34m%,d원\033[0m, %s\n",
                    expense.getDate(), expense.getAmount(), expense.getDescription());
        }
    }

    public void recentTransactions() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Income> recentIncomes = accountBook.getIncomes().stream()
                .filter(income -> income.getDate().isAfter(oneMonthAgo) || income.getDate().isEqual(oneMonthAgo))
                .collect(Collectors.toList());

        List<Expense> recentExpenses = accountBook.getExpenses().stream()
                .filter(expense -> expense.getDate().isAfter(oneMonthAgo) || expense.getDate().isEqual(oneMonthAgo))
                .collect(Collectors.toList());

        List<Transaction> transactions = new ArrayList<>();
        recentIncomes.forEach(income -> transactions.add(new Transaction(income.getDate(), income.getAmount(), "수입", income.getDescription())));
        recentExpenses.forEach(expense -> transactions.add(new Transaction(expense.getDate(), expense.getAmount(), "지출", expense.getDescription())));

        transactions.sort(Comparator.comparing(Transaction::getDate));

        System.out.println("최근 1개월 거래 내역:");
        for (Transaction transaction : transactions) {
            String colorCode = transaction.getType().equals("수입") ? "\033[31m" : "\033[34m";
            System.out.printf("%s%s\033[0m, %s, %s\n",
                    colorCode, String.format("%,d원", transaction.getAmount()), transaction.getDate(), transaction.getDescription());
        }

        System.out.printf("현재 잔액: %,d원\n", accountBook.getBalance());
    }

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
