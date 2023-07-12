package com.cg.service.deposit;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepositService implements IDeposit {
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Override
    public Optional<Deposit> findById(Integer id) {
        return depositRepository.findById(id);
    }

    @Override
    public Deposit save(Deposit deposit) {
        return depositRepository.save(deposit);
    }

    @Override
    public void delete(Deposit deposit) {
        depositRepository.delete(deposit);
    }

    @Override
    public void deleteById(Integer id) {
        depositRepository.deleteById(id);
    }

    @Override
    public Customer deposit(Deposit deposit) {
        deposit.setId(-1);
        depositRepository.save(deposit);
        Customer customer = deposit.getCustomer();
        BigDecimal transactionAmount = deposit.getTransactionAmount();
        customerRepository.incrementBalance(customer.getId(), transactionAmount);
        BigDecimal newBalance = customer.getBalance().add(transactionAmount);
        customer.setBalance(newBalance);
        return customer;
    }
}
