package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.service.customer.ICustomer;
import com.cg.service.transfer.ITransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private ICustomer customerService;
    @Autowired
    private ITransfer transferService;

    @GetMapping("/{id}")
    public String showTransferPage(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(Integer.parseInt(id));
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer sender = customerOptional.get();
        model.addAttribute("sender", sender);
        List<Customer> customers = new ArrayList<>(customerService.findAll());
        customers.removeIf(customer -> customer.getId() == sender.getId());
        model.addAttribute("customers", customers);
        Transfer transfer = new Transfer();
        model.addAttribute("transfer", transfer);
        return "transfer/transfer";
    }

    @PostMapping("/{id}")
    public String transfer(@PathVariable int id, @RequestParam("idRecipient") int idRecipient, Model model, @ModelAttribute Transfer transfer, BindingResult bindingResult) {
        Optional<Customer> senderOptional = customerService.findById(id);
        Optional<Customer> recipientOptional = customerService.findById(idRecipient);

        if (senderOptional.isEmpty() || recipientOptional.isEmpty()) {
            return "redirect:/errors/404";
        }

        Customer sender = senderOptional.get();
        Customer recipient = recipientOptional.get();
        transfer.validate(transfer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("sender", sender);
            model.addAttribute("transfer", transfer);
            List<Customer> customers = new ArrayList<>(customerService.findAll());
            customers.removeIf(customer -> customer.getId() == sender.getId());
            model.addAttribute("customers", customers);
            model.addAttribute("tempIDRecipient", idRecipient);
            model.addAttribute("tempNameRecipient", recipient.getFullName());

            return "transfer/transfer";
        }

        if (!bindingResult.hasFieldErrors()) {
            if (sender.getBalance().compareTo(transfer.getTransactionAmount()) <= 0) {
                model.addAttribute("success", true);
                model.addAttribute("message", "Số dư của bạn không đủ để thực hiện chuyển khoản!");
                model.addAttribute("sender", sender);
                model.addAttribute("transfer", transfer);
                List<Customer> customers = new ArrayList<>(customerService.findAll());
                customers.removeIf(customer -> customer.getId() == sender.getId());
                model.addAttribute("customers", customers);
                model.addAttribute("tempIDRecipient", idRecipient);
                model.addAttribute("tempNameRecipient", recipient.getFullName());
            } else {
                transfer.setSender(sender);
                transfer.setRecipinet(recipient);
                transferService.transfer(transfer);
                model.addAttribute("sender", sender);
                model.addAttribute("transfer", new Transfer());
                List<Customer> customers = new ArrayList<>(customerService.findAll());
                customers.removeIf(customer -> customer.getId() == sender.getId());
                model.addAttribute("customers", customers);
                model.addAttribute("tempIDRecipient", idRecipient);
                model.addAttribute("tempNameRecipient", recipient.getFullName());
                model.addAttribute("success", true);
                model.addAttribute("message", "Chuyển tiền thành công");
            }
        }
        return "transfer/transfer";
    }
}
