// Debt.java
public class Debt {

    private Person debtor;
    private Person creditor;
    private double amount;

    public Debt(Person debtor, Person creditor, double amount) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public Person getDebtor() {
        return debtor;
    }

    public Person getCreditor() {
        return creditor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
