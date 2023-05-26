package codecool.refactor.service;

import codecool.refactor.model.Transaction;
import codecool.refactor.utils.TransactionFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    private String filename = null;

    private List<Transaction> transactions;

    public TransactionServiceImpl(String filename) {
        this.filename = filename;
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


        int skip = 0;
        Map<String, Map<String, Double>> totalsByCurrencyAndRecipient = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (skip == 0) {
                    skip++;
                    continue;
                }
                String[] fields = line.split(",");
                String currency = fields[3];
                String recipient = fields[4];
                Double amount = Double.parseDouble(fields[2]);
                if (!totalsByCurrencyAndRecipient.containsKey(currency)) {
                    totalsByCurrencyAndRecipient.put(currency, new HashMap<>());
                }
                Map<String, Double> totalsByRecipient = totalsByCurrencyAndRecipient.get(currency);
                if (!totalsByRecipient.containsKey(recipient)) {
                    totalsByRecipient.put(recipient, amount);
                } else {
                    totalsByRecipient.put(recipient, totalsByRecipient.get(recipient) + amount);
                }
                skip++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
        //TODO add logging
        try {
            PrintWriter writer = new PrintWriter(new File("output.txt"));
            for (String currency : totalsByCurrencyAndRecipient.keySet()) {
                double maxTotal = 0;
                String max = "";
                for (String recipient : totalsByCurrencyAndRecipient.get(currency).keySet()) {
                    double total = totalsByCurrencyAndRecipient.get(currency).get(recipient);
                    if (total > maxTotal) {
                        maxTotal = total;
                        max = recipient;
                    }
                }
                writer.println(currency + "," + max + "," + maxTotal);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
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

        System.out.println("Average transaction amounts by currency:");
        for (String currency : transactionAmountsByCurrency.keySet()) {
            List<BigDecimal> transactionAmounts = transactionAmountsByCurrency.get(currency);
            BigDecimal sum = new BigDecimal(0);
            for (BigDecimal transactionAmount : transactionAmounts) {
                sum = sum.add(transactionAmount);
            }
            BigDecimal average = sum.divide(new BigDecimal(transactionAmounts.size()), RoundingMode.DOWN);
            System.out.println(currency + ": " + average.toString());
        }
    }


    public void getTheMostOccurringPaymentMethodByCurrency() {
        Map<String, Map<String, Long>> paymentMethodsByCurrency = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency, Collectors.groupingBy(
                                Transaction::getPaymentMethod, Collectors.counting())
                        )
                );
        print(paymentMethodsByCurrency);
    }

    public void print(Map<String, Map<String, Long>> countsByCurrencyAndPaymentMethod) {
        for (String currency : countsByCurrencyAndPaymentMethod.keySet()) {
            long maxCount = 0;
            String maxPaymentMethod = "";
            for (String paymentMethod : countsByCurrencyAndPaymentMethod.get(currency).keySet()) {
                long count = countsByCurrencyAndPaymentMethod.get(currency).get(paymentMethod);
                if (count > maxCount) {
                    maxCount = count;
                    maxPaymentMethod = paymentMethod;
                }
            }
            System.out.println(currency + ": " + maxPaymentMethod);
        }
    }
}
