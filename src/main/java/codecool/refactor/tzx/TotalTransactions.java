package codecool.refactor.tzx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TotalTransactions extends AbstractTransaction {

    public TotalTransactions(String filename) {
        this.filename = filename;
    }

    String filename = null;


    public void wow() {
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
}
