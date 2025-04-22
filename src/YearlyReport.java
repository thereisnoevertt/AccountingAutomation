public class YearlyReport {

    private byte month;
    private int amount;
    private boolean is_expense;

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isIs_expense() {
        return is_expense;
    }

    public void setIs_expense(boolean is_expense) {
        this.is_expense = is_expense;
    }
    public String toString() {
        return getMonth() + ", " + getAmount() + ", "+ isIs_expense();
    }
}
