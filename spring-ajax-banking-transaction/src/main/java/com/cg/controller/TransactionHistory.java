package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomer;
import com.cg.service.deposit.IDeposit;
import com.cg.service.transfer.ITransfer;
import com.cg.service.withdraw.IWithdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactionHistory")
public class TransactionHistory {
    @Autowired
    private ICustomer customerService;
    @Autowired
    private IDeposit depositService;
    @Autowired
    private ITransfer transferService;
    @Autowired
    private IWithdraw withdrawService;

    @GetMapping
    public String showHistoryPage(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "transaction_history/transaction_history";
    }

    @GetMapping("/recharge/{id}")
    public String showRechargePage(@PathVariable String id, Model model) {
        List<Deposit> depositList = depositService.findAll();
        List<Deposit> deposits = new ArrayList<>();
        for (Deposit deposit : depositList) {
            if (deposit.getCustomer().getId() == Integer.parseInt(id)) {
                deposits.add(deposit);
            }
        }
        model.addAttribute("deposits", deposits);
        return "transaction_history/recharge_history";
    }

    @GetMapping("/transfer/{id}")
    public String showTransferPage(@PathVariable String id, Model model) {
        List<Transfer> transferList = transferService.findAll();
        List<Transfer> transfers = new ArrayList<>();
        for (Transfer transfer : transferList) {
            if (transfer.getSender().getId() == Integer.parseInt(id)) {
                transfers.add(transfer);
            }
        }
        model.addAttribute("transfers", transfers);
        return "transaction_history/transfer_history";
    }

    @GetMapping("/withdrawal/{id}")
    public String showWithdrawalPage(@PathVariable String id, Model model) {
        List<Withdraw> withdrawList = withdrawService.findAll();
        List<Withdraw> withdraws = new ArrayList<>();
        for (Withdraw withdraw : withdrawList) {
            if (withdraw.getCustomer().getId() == Integer.parseInt(id)) {
                withdraws.add(withdraw);
            }
        }
        model.addAttribute("withdraws", withdraws);
        return "transaction_history/withdraw_history";
    }
}
