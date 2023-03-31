package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.BankDetails;
import com.devdowns.walletservice.domain.dto.SetBankDetailsResponse;

public interface BankAccountInputPort {

  SetBankDetailsResponse setBankAccount(BankDetails bankDetails);
}
