package com.devdowns.walletservice.infrastructure.inputadapter.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devdowns.walletservice.domain.dto.BalanceResponse;
import com.devdowns.walletservice.infrastructure.inputport.BankAccountInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletTransactionInputPort;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class WalletAPITest {

  private final WalletInputPort walletInputPort = mock(WalletInputPort.class);
  private final BankAccountInputPort bankAccountInputPort = mock(BankAccountInputPort.class);
  private final BalanceResponse response = BalanceResponse.builder()
      .userId(42L)
      .balance(BigDecimal.TEN)
      .build();
  private final WalletTransactionInputPort walletTransactionInputPort = mock(
      WalletTransactionInputPort.class);

  @Test
  void testGetBalanceReturnsCorrectJson() throws Exception {

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WalletAPI(walletInputPort,
            walletTransactionInputPort,
            bankAccountInputPort))
        .build();

    // Arrange
    when(walletInputPort.checkBalance(response.getUserId())).thenReturn(response);

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/wallets/balance?user_id=" + response.getUserId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(response.getBalance()))
        .andExpect(jsonPath("$.user_id").value(response.getUserId()));
  }

  @Test
  void testGetBalanceReturnsBadRequestWhenNoUserIdProvided() throws Exception {

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WalletAPI(walletInputPort,
            walletTransactionInputPort,
            bankAccountInputPort))
        .build();

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/wallets/balance"))
        .andExpect(status().isBadRequest());
  }

}
