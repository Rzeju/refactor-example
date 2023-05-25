package codecool.refactor.service;

import codecool.refactor.model.Transaction;
import codecool.refactor.utils.TransactionFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

public class TransactionServiceImpl implements TransactionService {

    public TransactionServiceImpl(String filename) {
        this.filename = filename;
    }

    String filename = null;

    public void getRecipientsWithHighestAmountByCurrency() {
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

        List<Transaction> transactions = TransactionFileReader.readTransactions();
        Map<String, List<BigDecimal>> transactionAmountByCurrency = new HashMap<>();
        for (Transaction transaction : transactions) {
            transactionAmountByCurrency.compute(transaction.getCurrency(), (k, v) -> {
                v = v != null ? v : new ArrayList<>();
                v.add(transaction.getAmount());
                return v;
            });

//            transactionAmountByCurrency.computeIfPresent(transaction.getCurrency(), (k, v) -> {
//                v.add(transaction.getAmount());
//                return v;
//            });
//            transactionAmountByCurrency.putIfAbsent(transaction.getCurrency(), new ArrayList<>()).add(transaction.getAmount());
        }

        System.out.println("Average transaction amounts by currency:");
        for (String currency : transactionAmountByCurrency.keySet()) {
            List<BigDecimal> transactionAmounts = transactionAmountByCurrency.get(currency);
            BigDecimal sum = new BigDecimal(0);
            for (BigDecimal transactionAmount : transactionAmounts) {
                sum = sum.add(transactionAmount);
            }
            BigDecimal average = sum.divide(new BigDecimal(transactionAmounts.size()));
            System.out.println(currency + ": " + average.toString());
        }
    }


    public void getTheMostOccurringPaymentMethodByCurrency() {
        Map<String, Map<String, Integer>> countsByCurrencyAndPaymentMethod = new HashMap<>();
        int skip = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                if (skip == 0) {
                    skip++;
                    continue;
                }
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                String currency = fields[3];
                String paymentMethod = fields[5];
                if (!countsByCurrencyAndPaymentMethod.containsKey(currency)) {
                    countsByCurrencyAndPaymentMethod.put(currency, new HashMap<>());
                }
                Map<String, Integer> countsByPaymentMethod = countsByCurrencyAndPaymentMethod.get(currency);
                if (!countsByPaymentMethod.containsKey(paymentMethod)) {
                    countsByPaymentMethod.put(paymentMethod, 1);
                } else {
                    countsByPaymentMethod.put(paymentMethod, countsByPaymentMethod.get(paymentMethod) + 1);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
        System.out.println("Payment methods with the most total transactions by currency:");
        print(countsByCurrencyAndPaymentMethod);
    }

    public void print(Map<String, Map<String, Integer>> countsByCurrencyAndPaymentMethod) {
        for (String currency : countsByCurrencyAndPaymentMethod.keySet()) {
            int maxCount = 0;
            String maxPaymentMethod = "";
            for (String paymentMethod : countsByCurrencyAndPaymentMethod.get(currency).keySet()) {
                int count = countsByCurrencyAndPaymentMethod.get(currency).get(paymentMethod);
                if (count > maxCount) {
                    maxCount = count;
                    maxPaymentMethod = paymentMethod;
                }
            }
            System.out.println(currency + ": " + maxPaymentMethod);
        }
    }
}
