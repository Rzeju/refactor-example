package codecool.refactor.tzx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Transaction {
    private String transactionDate;
    private String description;
    private double amount;
    private String currency;
    private String recipient;
    private String paymentMethod;

    public Transaction(String transactionDate, String description, double amount, String currency, String recipient, String paymentMethod) {
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

    public double getAmount() {
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
