package codecool.refactor;

import codecool.refactor.service.TransactionServiceImpl;
import codecool.refactor.utils.TransactionFileReader;

public class Main {

    public static void main(String[] args) {
        TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();

        transactionServiceImpl.getRecipientsTotalAmountByCurrency();
        transactionServiceImpl.getTheMostOccurringPaymentMethodByCurrency();
        transactionServiceImpl.getAverageTransactionsAmountByCurrency();

        //TODO
        // daysBetweenEarliestAndLatestTransactions(null);
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



