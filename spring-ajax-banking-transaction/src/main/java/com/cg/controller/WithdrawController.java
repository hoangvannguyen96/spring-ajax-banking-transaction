package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomer;
import com.cg.service.withdraw.IWithdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/withdraws")
public class WithdrawController {
    @Autowired
    private IWithdraw withdrawService;
    @Autowired
    private ICustomer customerService;

    @GetMapping("/{id}")
    public String showWithdrawPage(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(Integer.parseInt(id));
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        model.addAttribute("customer", customer);
        Withdraw withdraw = new Withdraw();
        model.addAttribute("withdraw", withdraw);
        return "withdraw/withdraw";
    }

    @PostMapping("/{id}")
    public String edit(@PathVariable int id, @ModelAttribute Withdraw withdraw, Model model, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        withdraw.validate(withdraw, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("customer", customer);
            return "withdraw/withdraw";
        }
        if (customer.getBalance().compareTo(withdraw.getTransactionAmount()) <= 0) {
            model.addAttribute("success", true);
            model.addAttribute("message", "Số dư của bạn không đủ để thực hiện rút tiền!");
            model.addAttribute("customer", customer);
            model.addAttribute("withdraw", withdraw);
            return "withdraw/withdraw";
        }
        withdraw.setCustomer(customer);
        withdrawService.withdraw(withdraw);
        customer.setBalance(customer.getBalance().subtract(withdraw.getTransactionAmount()));
        customerService.save(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("withdraw", new Withdraw());
        model.addAttribute("success", true);
        model.addAttribute("message", "Rút tiền thành công");
        return "withdraw/withdraw";
    }
}
