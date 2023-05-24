package codecool.refactor.tzx;

import java.util.Map;

public abstract class AbstractTransaction implements AInterface{

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
