package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_sender", referencedColumnName = "id", nullable = false)
    private Customer sender;

    private String nameSender;

    @ManyToOne
    @JoinColumn(name = "id_recipient", referencedColumnName = "id", nullable = false)
    private Customer recipinet;

    private String nameRecipinet;

    private Date createAt=new Date();

    private double fees=10.0;

    @Column(name = "transfer_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transferAmount;

    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;


    @Override
    public boolean supports(Class<?> aClass) {
        return Transfer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Transfer transfer = (Transfer) target;
        BigDecimal transferAmount = transfer.getTransferAmount();
    }
}
