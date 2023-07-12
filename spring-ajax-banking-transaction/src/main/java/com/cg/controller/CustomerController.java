package com.cg.controller;

import com.cg.model.Customer;
import com.cg.service.customer.ICustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomer customerService;

    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        List<Customer> customers = customerService.findAll();
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer/modalCreate";
    }

    @PostMapping("/create")
    public String create(Model model, @ModelAttribute Customer customer, BindingResult bindingResult) {
        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
        }
        String email = customer.getEmail();
        Boolean existsEmail = customerService.existsByEmail(email);

        if (existsEmail) {
            bindingResult.rejectValue("email", "email.exist");
        }

        model.addAttribute("customer", customer);

        if (!existsEmail && !bindingResult.hasErrors()) {
            customer.setBalance(new BigDecimal(0));
            customerService.save(customer);
            model.addAttribute("customer", new Customer());
            model.addAttribute("success", true);
            model.addAttribute("message", "Thêm thành công");
        }

        return "customer/modalCreate";
    }

    @GetMapping("/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(Integer.parseInt(id));
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        model.addAttribute("customer", customer);
        return "modalUpdate";
    }

    @PostMapping("/{id}")
    public String edit(@PathVariable int id, Model model, @ModelAttribute("customer") Customer editedCustomer, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Khách hàng không tồn tại");
        } else {
            editedCustomer.validate(editedCustomer,bindingResult);
            Customer existingCustomer = customerOptional.get();
            String email = editedCustomer.getEmail();
            if (bindingResult.hasErrors()) {
                model.addAttribute("hasError", true);
            }
            model.addAttribute("customer", editedCustomer);
            if (!bindingResult.hasErrors()) {
                editedCustomer.setId(id);
                editedCustomer.setBalance(existingCustomer.getBalance());
                customerService.save(editedCustomer);
                model.addAttribute("customer", editedCustomer);
                model.addAttribute("success", true);
                model.addAttribute("message", "Sửa thành công");
            }
        }
        return "customer_banking/edit";
    }

    @GetMapping("delete/{id}")
    public String showDeletePage(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(Integer.parseInt(id));
        if (customerOptional.isEmpty()) {
            return "redirect:/errors/404";
        }
        Customer customer = customerOptional.get();
        model.addAttribute("customer", customer);
        return "customer_banking/delete";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        List<Customer> customers = customerService.findAll();
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        model.addAttribute("customers", customers);
        return "redirect:/customers";
    }
}
