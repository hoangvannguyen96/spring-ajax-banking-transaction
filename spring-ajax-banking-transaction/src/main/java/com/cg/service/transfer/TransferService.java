package com.cg.service.transfer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferService implements ITransfer {
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(Integer id) {
        return transferRepository.findById(id);
    }

    @Override
    public Transfer save(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public void delete(Transfer transfer) {
        transferRepository.delete(transfer);
    }

    @Override
    public void deleteById(Integer id) {
        transferRepository.deleteById(id);
    }

    @Override
    public void transfer(Transfer transfer) {
        transfer.setId(-1);
        transferRepository.save(transfer);
        customerRepository.decrementBalance(transfer.getSender().getId(),transfer.getTransactionAmount());
        Customer sender=transfer.getSender();
        sender.setBalance(sender.getBalance().subtract(transfer.getTransactionAmount()));
        customerRepository.incrementBalance(transfer.getRecipinet().getId(),transfer.getTransferAmount());
        Customer recipinet=transfer.getRecipinet();
        recipinet.setBalance(recipinet.getBalance().add(transfer.getTransferAmount()));
    }
}
