package com.cg.service.deposit;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.service.IGeneralService;

public interface IDeposit extends IGeneralService<Deposit,Integer> {
    Customer deposit(Deposit deposit);
}
