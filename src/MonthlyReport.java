public class MonthlyReport {

    private String item_name;
    private boolean is_expense;
    private int quantity;
    private int sum_of_one;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public boolean isIs_expense() {
        return is_expense;
    }

    public void setIs_expense(boolean is_expense) {
        this.is_expense = is_expense;
    }

    public int getSum_of_one() {
        return sum_of_one;
    }

    public void setSum_of_one(int sum_of_one) {
        this.sum_of_one = sum_of_one;
    }
    public String toString() {
        return getItem_name() + ", " + isIs_expense()  + ", " + getQuantity()  + ", " + getSum_of_one();
    }
}