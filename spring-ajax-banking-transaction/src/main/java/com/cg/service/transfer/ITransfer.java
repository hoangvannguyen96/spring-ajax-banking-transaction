package com.cg.service.transfer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.service.IGeneralService;

public interface ITransfer extends IGeneralService<Transfer, Integer> {
    public void transfer(Transfer transfer);
}
