package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.wallet.BankDetails;
import com.devdowns.walletservice.domain.dto.wallet.SetBankDetailsResponse;

public interface BankAccountInputPort {

  SetBankDetailsResponse setBankAccount(BankDetails bankDetails);
}
