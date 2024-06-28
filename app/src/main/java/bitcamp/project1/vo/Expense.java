package bitcamp.project1.vo;

import java.time.LocalDate;

public class Expense {

    public enum Category {
        HOUSING, COMMUNICATION, TRANSPORTATION, FINANCE, FOOD, HOBBY
    }

    private LocalDate date;
    private long amount;
    private String description;
    private Category category;

    public Expense(LocalDate date, long amount, String description, Category category) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
