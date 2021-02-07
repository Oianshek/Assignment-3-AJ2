package kz.edu.astanait.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date_opened")
    private String dateOpened;

    @Column(name = "amount_of_money")
    private Long amountOfMoney;
}
