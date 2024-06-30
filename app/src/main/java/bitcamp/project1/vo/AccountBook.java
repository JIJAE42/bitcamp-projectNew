package bitcamp.project1.vo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountBook {
    private List<Expense> expenses = new ArrayList<>();
    private List<Income> incomes = new ArrayList<>();
    private int balance = 0; // 잔액 필드 추가

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public int getBalance() {
        return balance;
    }

    public void addIncome(int amount) {
        balance += amount; // 소득 추가 시 잔액 증가
    }

    public void addExpense(int amount) {
        balance -= amount; // 지출 추가 시 잔액 감소
    }

    public int getCashExpenses() {
        return expenses.stream().filter(e -> e.getType() == Expense.Type.CASH).mapToInt(Expense::getAmount).sum();
    }

    public int getCardExpenses() {
        return expenses.stream().filter(e -> e.getType() == Expense.Type.CARD).mapToInt(Expense::getAmount).sum();
    }

    public int getMonthlyIncome(LocalDate month) {
        return incomes.stream()
                .filter(i -> i.getDate().getYear() == month.getYear() && i.getDate().getMonth() == month.getMonth())
                .mapToInt(Income::getAmount).sum();
    }

    public int getPreviousMonthIncome(LocalDate month) {
        LocalDate previousMonth = month.minusMonths(1);
        return incomes.stream()
                .filter(i -> i.getDate().getYear() == previousMonth.getYear() && i.getDate().getMonth() == previousMonth.getMonth())
                .mapToInt(Income::getAmount).sum();
    }

    public int getMonthlyExpenses(LocalDate month) {
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == month.getYear() && e.getDate().getMonth() == month.getMonth())
                .mapToInt(Expense::getAmount).sum();
    }

    public int getPreviousMonthExpenses(LocalDate month) {
        LocalDate previousMonth = month.minusMonths(1);
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == previousMonth.getYear() && e.getDate().getMonth() == previousMonth.getMonth())
                .mapToInt(Expense::getAmount).sum();
    }
}
