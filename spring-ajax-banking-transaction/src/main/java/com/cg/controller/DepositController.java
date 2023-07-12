package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.service.customer.ICustomer;
import com.cg.service.deposit.IDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.query.criteria.internal.ValueHandlerFactory.isNumeric;

@Controller
@RequestMapping("/deposits")
public class DepositController {
    @Autowired
    private IDeposit depositService;
    @Autowired
    private ICustomer customerService;

    @GetMapping("/{id}")
    public String showDepositPage(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(Integer.parseInt(id));
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        model.addAttribute("customer", customer);
        Deposit deposit = new Deposit();
        model.addAttribute("deposit", deposit);
        return "deposit/deposit";
    }

    @PostMapping("/{id}")
    public String deposit(@PathVariable int id, @ModelAttribute Deposit deposit, Model model, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        } else {
            try {
                Customer customer = customerOptional.get();
                deposit.validate(deposit, bindingResult);

                if (bindingResult.hasFieldErrors()) {
                    model.addAttribute("hasError", true);
                    model.addAttribute("customer", customer);
                    return "deposit/deposit";
                }
                deposit.setCustomer(customer);
                depositService.deposit(deposit);
                model.addAttribute("customer", customer);
                model.addAttribute("deposit", new Deposit());
                model.addAttribute("success", true);
                model.addAttribute("message", "Nạp tiền thành công");
            } catch (Exception exception) {
                exception.printStackTrace();
                return "error/500";
            }
            return "deposit/deposit";
        }
    }
}

