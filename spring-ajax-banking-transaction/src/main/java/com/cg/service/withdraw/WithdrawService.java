package com.cg.service.withdraw;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.repository.CustomerRepository;
import com.cg.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WithdrawService implements IWithdraw {
    @Autowired
    private WithdrawRepository withdrawRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    @Override
    public Optional<Withdraw> findById(Integer id) {
        return withdrawRepository.findById(id);
    }

    @Override
    public Withdraw save(Withdraw withdraw) {
        return withdrawRepository.save(withdraw);
    }

    @Override
    public void delete(Withdraw withdraw) {
        withdrawRepository.delete(withdraw);
    }

    @Override
    public void deleteById(Integer id) {
        withdrawRepository.deleteById(id);
    }
    @Override
    public Customer withdraw(Withdraw withdraw) {
        withdraw.setId(-1);
        withdrawRepository.save(withdraw);
        Customer customer = withdraw.getCustomer();
        BigDecimal withdraw1 = withdraw.getTransactionAmount();
        customerRepository.decrementBalance(customer.getId(),withdraw1);
        BigDecimal newBalance = customer.getBalance().subtract(withdraw1);
        customer.setBalance(newBalance);
        return customer;
    }
}
