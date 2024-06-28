package bitcamp.project1.vo;

import java.util.ArrayList;
import java.util.List;

public class AccountBook {
    private List<Expense> expenses = new ArrayList<>();
    private List<Income> incomes = new ArrayList<>();

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }
}
