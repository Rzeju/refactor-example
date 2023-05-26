package codecool.refactor.service;

public interface TransactionService {

    void getRecipientsTotalAmountByCurrency();

    void getAverageTransactionsAmountByCurrency();

    void getTheMostOccurringPaymentMethodByCurrency();
}
