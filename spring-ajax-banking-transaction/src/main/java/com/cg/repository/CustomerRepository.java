package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Integer id);

    List<Customer> findAllByDeletedIs(Boolean boo);
    @Modifying
    @Query("UPDATE Customer AS customer SET customer.balance = customer.balance + :amount WHERE customer.id = :id")
    void incrementBalance(@Param("id") int id, @Param("amount") BigDecimal amount);
    @Modifying
    @Query("UPDATE Customer AS customer SET customer.balance = customer.balance - :amount WHERE customer.id = :id")
    void decrementBalance(@Param("id") int id, @Param("amount") BigDecimal amount);
}
