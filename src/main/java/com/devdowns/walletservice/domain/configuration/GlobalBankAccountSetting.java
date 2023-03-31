package com.devdowns.walletservice.domain.configuration;

import com.devdowns.walletservice.domain.entity.BankAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GlobalBankAccountSetting {

  @JsonIgnore
  private Optional<BankAccount> bankAccountEntity;

}
