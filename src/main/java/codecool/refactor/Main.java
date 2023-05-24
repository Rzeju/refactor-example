package codecool.refactor;

import codecool.refactor.tzx.TotalTransactions;
import codecool.refactor.tzx.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        ArrayList<Transaction> mylistylist = new ArrayList<>();
        String csvFile = "D:\\Dev\\Projects\\SDA\\generalirefactor\\src\\main\\resources\\transactions.txt";
        String line;
        String csvSplitBy = ",";
        int skip = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                if (skip == 0) {
                    skip++;
                    continue;
                }
                String[] transactionData = line.split(csvSplitBy);
                Transaction aaa = new Transaction(
                        transactionData[0],
                        transactionData[1],
                        Double.parseDouble(transactionData[2]),
                        transactionData[3],
                        transactionData[4],
                        transactionData[5]
                );
                mylistylist.add(aaa);
                skip++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Transaction i : mylistylist) {
            System.out.println(i.getTransactionDate() + "\t" +
                    i.getDescription() + "\t" +
                    i.getAmount() + "\t" +
                    i.getCurrency() + "\t" +
                    i.getRecipient() + "\t" +
                    i.getPaymentMethod());
        }

        Map<String, BigDecimal> newdate = new HashMap<>();
        Map<String, Integer> kekw = new HashMap<>();
        String filename = "D:\\Dev\\Projects\\SDA\\generalirefactor\\src\\main\\resources\\transactions.txt";
        int firstline = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (firstline == 0) {
                    firstline++;
                    continue;
                }
                String[] fields = line.split(",");
                String currency = fields[3];
                BigDecimal amount = new BigDecimal(fields[2]);
                if (!newdate.containsKey(currency)) {
                    newdate.put(currency, amount);
                    kekw.put(currency, 1);
                } else {
                    newdate.put(currency, newdate.get(currency).add(amount));
                    kekw.put(currency, kekw.get(currency) + 1);
                }
                firstline++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
        System.out.println("Average transaction amounts by currency:");
        for (String currency : newdate.keySet()) {
            BigDecimal bigdecimal = newdate.get(currency);
            int count = kekw.get(currency);
            BigDecimal avge = bigdecimal.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
            System.out.println(currency + ": " + avge.toString());
        }

        TotalTransactions totalTransactions = new TotalTransactions(filename);
        totalTransactions.wow();

        temporaryMethodName(filename);
        //TODO
        // daysBetweenEarliestAndLatestTransactions(null);
    }


    public static void temporaryMethodName(String filename) {
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

    //TODO Non-static method cannot be referenced from a static context
    //TODO Commenting out for now
   /* public static long daysBetweenEarliestAndLatestTransactions(List<Transaction> transactions) {
        // Find the earliest and latest transaction dates
        LocalDate earliestDate = transactions.stream()
                .map(Transaction::getTransactionDate)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Transaction list is empty"));

        LocalDate latestDate = transactions.stream()
                .map(Transaction::getTransactionDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Transaction list is empty"));

        // Calculate the number of days between the two dates
        return Duration.between(earliestDate.atStartOfDay(), latestDate.atStartOfDay()).toDays();
    }

    */
}



