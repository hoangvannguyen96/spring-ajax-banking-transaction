package com.cg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO implements Validator {
    private String transferAmount;
    private Integer idRecipient;

    @Override
    public boolean supports(Class<?> aClass) {
        return TransferDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TransferDTO transferDTO = (TransferDTO) o;

        String transferAmountStr = transferDTO.transferAmount;

        if (transferAmountStr == null) {
            errors.rejectValue("transferAmount", "transferAmount.length", "Số tiền cần chuyển là bắt buộc");
            return;
        }

        if (transferAmountStr.length() == 0) {
            errors.rejectValue("transferAmount", "transferAmount.length", "Vui lòng nhập số tiền muốn chuyển");
        } else {
            if (!transferAmountStr.matches("\\d+")) {
                errors.rejectValue("transferAmount", "transferAmount.matches", "Vui lòng nhập giá trị tiền bằng chữ số");
            } else {
                BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferAmountStr));

                if (transferAmount.compareTo(BigDecimal.valueOf(100000L)) <= 0) {
                    errors.rejectValue("transferAmount", "transferAmount.min", "Số tiền muốn chuyển thấp nhất là 100.000 VND");
                } else {
                    if (transferAmount.compareTo(BigDecimal.valueOf(500000000L)) > 0) {
                        errors.rejectValue("transferAmount", "transferAmount.max", "Số tiền muốn chuyển tối đa chuyển trong ngày  là 500.000.000 VND");
                    }
                }
            }
        }
    }
}
