package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import org.springframework.stereotype.Service;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomer extends IGeneralService<Customer,Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Integer id);
    void incrementBalance(Integer id, BigDecimal amount);
    List<Customer> findAllByDeletedIs(Boolean boo);

}
