package kz.edu.astanait.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    public Account(String dateOpened, Long amountOfMoney) {
        this.dateOpened = dateOpened;
        this.amountOfMoney = amountOfMoney;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_opened")
    private String dateOpened;

    @Column(name = "amount_of_money")
    private Long amountOfMoney;

}
