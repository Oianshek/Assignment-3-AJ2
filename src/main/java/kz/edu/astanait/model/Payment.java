package kz.edu.astanait.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_history")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "payment_destination")
    private String paymentDestination;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Payment(String paymentDate, Long paymentAmount, String paymentDestination, Account account) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.paymentDestination = paymentDestination;
        this.account = account;
    }
}
