package springdb1.jdbc.domain;

import lombok.Data;

@Data
public class Bank {
    private String bankId;
    private int money;

    public Bank() {
    }

    public Bank(String bankId, int money) {
        this.bankId = bankId;
        this.money = money;
    }
}
