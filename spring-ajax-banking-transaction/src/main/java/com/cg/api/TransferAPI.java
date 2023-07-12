package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.DepositCreReqDTO;
import com.cg.model.dto.TransferDTO;
import com.cg.service.customer.ICustomer;
import com.cg.service.deposit.IDeposit;
import com.cg.service.transfer.ITransfer;
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
@RequestMapping("/api/transfers")
public class TransferAPI {
    @Autowired
    private ICustomer customerService;

    @Autowired
    private ITransfer transferService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;
    @PostMapping("/{customerId}")
    public ResponseEntity<?> transfer(@PathVariable("customerId") String customerIdStr,
                                        @RequestBody TransferDTO transferDTO,
                                        BindingResult bindingResult) {

        new TransferDTO().validate(transferDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (!validateUtils.isNumberValid(customerIdStr)) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Integer customerId = Integer.parseInt(customerIdStr);
        Integer recepientId= transferDTO.getIdRecipient();

        Optional<Customer> senderOptional = customerService.findById(customerId);
        Optional<Customer> recipientOptional = customerService.findById(recepientId);

        if (senderOptional.isEmpty()||recipientOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer sender = senderOptional.get();
        Customer recepient = recipientOptional.get();

        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferDTO.getTransferAmount()));
        BigDecimal transactionAmount=transferAmount.multiply(BigDecimal.valueOf(0.1)).add(transferAmount);

        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        transfer.setTransactionAmount(transactionAmount);
        transfer.setSender(sender);
        transfer.setRecipinet(recepient);

        transferService.transfer(transfer);

        List<Customer> customers=new ArrayList<>();
        customers.add(transfer.getSender());
        customers.add(transfer.getRecipinet());
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }
}
