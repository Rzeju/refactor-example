package codecool.refactor.service;

public interface TransactionService {

    void getRecipientsWithHighestAmountByCurrency();

    void getAverageTransactionsAmountByCurrency();

    void getTheMostOccurringPaymentMethodByCurrency();
}
