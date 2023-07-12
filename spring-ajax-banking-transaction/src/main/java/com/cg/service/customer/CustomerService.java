package com.cg.service.customer;

import com.cg.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cg.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService implements ICustomer {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAllByDeletedIs(Boolean boo) {
        return customerRepository.findAllByDeletedIs(boo);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
    @Override
    public Boolean existsByEmailAndIdNot(String email, Integer id){
        return customerRepository.existsByEmailAndIdNot(email,id);
    }

    @Override
    public void incrementBalance(Integer id, BigDecimal amount) {
        customerRepository.incrementBalance(id, amount);
    }
}
