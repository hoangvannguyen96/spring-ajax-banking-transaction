package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.DepositCreReqDTO;
import com.cg.service.customer.ICustomer;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/customers")

public class CustomerAPI {
    @Autowired
    private ICustomer customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.findAllByDeletedIs(false);

        List<CustomerResDTO> customerResDTOS = new ArrayList<>();

        for (Customer item : customers) {
            CustomerResDTO customerResDTO = new CustomerResDTO();
            customerResDTO.setId(item.getId());
            customerResDTO.setFullName(item.getFullName());
            customerResDTO.setEmail(item.getEmail());
            customerResDTO.setPhone(item.getPhone());
            customerResDTO.setBalance(item.getBalance());
            customerResDTO.setAddress(item.getAddress());

            customerResDTOS.add(customerResDTO);
        }

        return new ResponseEntity<>(customerResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Integer customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerResDTO customerResDTO, BindingResult bindingResult) {
        new CustomerResDTO().validate(customerResDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Map<String, String> data = new HashMap<>();

        Customer customer = new Customer();

        String email = customerResDTO.getEmail();

        if (customerService.existsByEmail(email)) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } else {
            customer.setFullName(customerResDTO.getFullName());
            customer.setEmail(email);
            customer.setPhone(customerResDTO.getPhone());
            customer.setAddress(customerResDTO.getAddress());
            customer.setBalance(BigDecimal.ZERO);
            customer.setId(-1);
            customer.setDeleted(false);
            Customer newCustomer = customerService.save(customer);
            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        }
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> edit(@PathVariable("customerId") Integer customerId,
                                  @RequestBody CustomerResDTO customerResDTO,
                                  BindingResult bindingResult) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        new CustomerResDTO().validate(customerResDTO, bindingResult);

        Map<String, String> data = new HashMap<>();

        String email = customerResDTO.getEmail();

        Customer customer = customerOptional.get();

        if (customerService.existsByEmailAndIdNot(email, customerId)) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        } else {
            customer.setFullName(customerResDTO.getFullName());
            customer.setEmail(email);
            customer.setPhone(customerResDTO.getPhone());
            customer.setAddress(customerResDTO.getAddress());

            customerService.save(customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> delete(@PathVariable("customerId") Integer customerId) {
        try {
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                Map<String, String> data = new HashMap<>();
                data.put("message", "Khách hàng không tồn tại");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }
            Customer customer = customerOptional.get();
            customer.setDeleted(true);
            customerService.save(customer);
            List<Customer> customers = customerService.findAllByDeletedIs(false);

            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> data = new HashMap<>();
            data.put("message", "Lỗi xóa khách hàng");
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
