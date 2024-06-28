package bitcamp.project1.vo;

import java.time.LocalDate;

public class Expense {
    public enum Category { 주거, 통신, 교통, 금융, 식비, 취미 }

    private LocalDate date;
    private int amount;
    private Category category;
    private String description;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
