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
@Table(name = "withdraws")
public class Withdraw extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id", nullable = false)
    private Customer customer;
    private Date createAt = new Date();
    @Column(name = "withdraw", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return Withdraw.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Withdraw withdraw = (Withdraw) o;

        BigDecimal transactionAmount =withdraw.getTransactionAmount();
    }
}
