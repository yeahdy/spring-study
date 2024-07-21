package springdb1.jdbc.repository;

import springdb1.jdbc.domain.Bank;

public interface BankRepository3 {
    Bank save (Bank bank);
    Bank findById(String bankId);
    void update(String bankId, int money);
    void delete(String bankId);
}
