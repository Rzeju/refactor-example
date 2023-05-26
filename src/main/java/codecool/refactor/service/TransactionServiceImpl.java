package codecool.refactor.service;

import codecool.refactor.model.Transaction;
import codecool.refactor.utils.TransactionFileReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    private List<Transaction> transactions;

    public TransactionServiceImpl() {
        this.transactions = TransactionFileReader.readTransactions();
    }

    public void getRecipientsTotalAmountByCurrency() {
        Map<String, Map<String, BigDecimal>> recipientsTransactionAmountsByCurrency = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency, Collectors.groupingBy(
                                        Transaction::getRecipient, Collectors.reducing(
                                                BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                                )
                        )
                );

        List<String> recipients = new ArrayList<>();
        for (Map.Entry<String, Map<String, BigDecimal>> entry : recipientsTransactionAmountsByCurrency.entrySet()) {
            entry.getValue().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(e -> entry.getKey() + ":" + e.getKey() + ":" + e.getValue())
                    .ifPresent(recipients::add);
        }

        System.out.println("Highest Total Amount Recipient by Currency");
        System.out.println(recipients);
    }

    public void getAverageTransactionsAmountByCurrency() {
        Map<String, List<BigDecimal>> transactionAmountsByCurrency = new HashMap<>();
        for (Transaction transaction : transactions) {
            transactionAmountsByCurrency.compute(transaction.getCurrency(), (k, v) -> {
                v = v != null ? v : new ArrayList<>();
                v.add(transaction.getAmount());
                return v;
            });
        }

        Map<String, BigDecimal> result = new HashMap<>();
        for (Map.Entry<String, List<BigDecimal>> entry : transactionAmountsByCurrency.entrySet()) {
            BigDecimal sum = entry.getValue().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal average = sum.divide(BigDecimal.valueOf(entry.getValue().size()), RoundingMode.DOWN);

            result.put(entry.getKey(), average);
        }

        System.out.println("Average transaction amounts by currency:");
        System.out.println(result);
    }


    public void getTheMostOccurringPaymentMethodByCurrency() {
        Map<String, Map<String, Long>> paymentMethodsByCurrency = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency, Collectors.groupingBy(
                                Transaction::getPaymentMethod, Collectors.counting())
                        )
                );

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Long>> entry : paymentMethodsByCurrency.entrySet()) {
            entry.getValue().entrySet().stream()
                    .max(Comparator.comparingLong(Map.Entry::getValue))
                    .ifPresent(it -> result.put(entry.getKey(), it.getKey()));
        }

        System.out.println("Most Occurring Payment Method By Currency");
        System.out.println(result);
    }
}
