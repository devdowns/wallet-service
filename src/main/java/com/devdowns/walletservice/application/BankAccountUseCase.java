package com.devdowns.walletservice.application;

import static java.util.Objects.isNull;

import com.devdowns.walletservice.domain.configuration.GlobalBankAccountSetting;
import com.devdowns.walletservice.domain.dto.BankDetails;
import com.devdowns.walletservice.domain.dto.SetBankDetailsResponse;
import com.devdowns.walletservice.domain.entity.BankAccount;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.infrastructure.inputport.BankAccountInputPort;
import com.devdowns.walletservice.infrastructure.outputport.BankAccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BankAccountUseCase implements BankAccountInputPort {

  private final BankAccountRepository bankAccountRepository;

  private GlobalBankAccountSetting globalBankAccountSetting;


  public BankAccountUseCase(BankAccountRepository bankAccountRepository,
      GlobalBankAccountSetting globalBankAccountSetting) {
    this.bankAccountRepository = bankAccountRepository;
    this.globalBankAccountSetting = globalBankAccountSetting;
  }

  @Override
  public SetBankDetailsResponse setBankAccount(BankDetails bankDetails) {
    verifyBankAccountDetails(bankDetails);

    final BankAccount bankAccount = findOrCreateBankAccount(bankDetails);

    globalBankAccountSetting.setBankAccountEntity(Optional.of(bankAccount));

    final String holderName = bankAccount.getName() + " " + bankAccount.getSurname();

    return SetBankDetailsResponse.builder()
        .holderName(holderName)
        .accountNumber(bankAccount.getAccountNumber())
        .routingNumber(bankAccount.getRoutingNumber())
        .bankName(bankAccount.getBankName())
        .build();
  }

  private void verifyBankAccountDetails(BankDetails bankDetails) {
    if (isNull(bankDetails.getAccountNumber()) ||
        isNull(bankDetails.getRoutingNumber()) ||
        isNull(bankDetails.getBankName()) ||
        isNull(bankDetails.getName()) ||
        isNull(bankDetails.getSurname()) ||
        isNull(bankDetails.getNationalId())) {
      globalBankAccountSetting.setBankAccountEntity(Optional.ofNullable(null));
      throw new MalformedRequestException("Bank account details are incomplete.");
    }
  }


  private BankAccount findOrCreateBankAccount(BankDetails bankDetails) {
    return bankAccountRepository.findByAccountNumberAndRoutingNumber(
        bankDetails.getAccountNumber(),
        bankDetails.getRoutingNumber()
    ).orElseGet(() ->
        bankAccountRepository.save(BankAccount.builder()
            .accountNumber(bankDetails.getAccountNumber())
            .routingNumber(bankDetails.getRoutingNumber())
            .bankName(bankDetails.getBankName())
            .name(bankDetails.getName())
            .surname(bankDetails.getSurname())
            .nationalId(bankDetails.getNationalId())
            .balance(new BigDecimal("0.00"))
            .build()
        )
    );
  }


}
