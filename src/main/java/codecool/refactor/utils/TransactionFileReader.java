package codecool.refactor.utils;

import codecool.refactor.model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileReader {

    private static final String FILE_PATH = "D:\\Dev\\Projects\\SDA\\generalirefactor\\src\\main\\resources\\transactions.txt";;
    private static final String CSV_FILE_DELIMITER = ",";

    public static List<Transaction> readTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String line;
        int skip = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (skip == 0) {
                    skip++;
                    continue;
                }
                String[] transactionData = line.split(CSV_FILE_DELIMITER);
                Transaction aaa = new Transaction(
                        transactionData[0],
                        transactionData[1],
                        Double.parseDouble(transactionData[2]),
                        transactionData[3],
                        transactionData[4],
                        transactionData[5]
                );
                transactions.add(aaa);
                skip++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Transaction i : transactions) {
            System.out.println(i.getTransactionDate() + "\t" +
                    i.getDescription() + "\t" +
                    i.getAmount() + "\t" +
                    i.getCurrency() + "\t" +
                    i.getRecipient() + "\t" +
                    i.getPaymentMethod());
        }

        return transactions;
    }
}
