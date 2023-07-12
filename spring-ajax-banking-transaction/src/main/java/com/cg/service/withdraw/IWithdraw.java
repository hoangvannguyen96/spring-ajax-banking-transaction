package com.cg.service.withdraw;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.service.IGeneralService;

public interface IWithdraw extends IGeneralService<Withdraw,Integer> {
    Customer withdraw(Withdraw withdraw);

}
