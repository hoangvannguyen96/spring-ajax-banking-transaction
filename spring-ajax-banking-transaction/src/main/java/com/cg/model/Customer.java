package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    private String phone;

    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
        String email = customer.email;
        String fullName = customer.fullName;

        if (fullName == null || fullName.trim().length() == 0) {
            errors.rejectValue("fullName", "fullName.length", "Tên không được để trống");
        } else {
            if (!fullName.matches("([A-Z]+[a-z]*[ ]?)+$")) {
                errors.rejectValue("fullName", "fullName.number", "Tên bắt đầu bằng chữ hoa, không được chứa số và ký tự đặc biệt");
            } else {
                if (fullName.trim().length() < 5 || fullName.trim().length() > 20) {
                    errors.rejectValue("fullName", "fullName.length", "Tên từ 5-20 ký tự");
                }
            }
        }
        if (email == null || email.trim().length() == 0) {
            errors.rejectValue("email", "email.length", "Email không được để trống");
        } else {
            if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,9}$")) {
                errors.rejectValue("email", "email.regex", "Email không hợp lệ");
            } else {
                if (email.trim().length() > 30) {
                    errors.rejectValue("email", "email.maxlength", "Email tối đa 30 ký tự");
                }
            }
        }

    }
}
