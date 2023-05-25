package codecool.refactor.model;

import java.math.BigDecimal;

public class Transaction {
    private String transactionDate;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String recipient;
    private String paymentMethod;

    public Transaction(String transactionDate, String description, BigDecimal amount, String currency, String recipient, String paymentMethod) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.recipient = recipient;
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
