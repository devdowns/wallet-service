package com.devdowns.walletservice.infrastructure.inputadapter.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devdowns.walletservice.domain.dto.wallet.BalanceResponse;
import com.devdowns.walletservice.infrastructure.inputport.TransactionInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class WalletAPITest {

  private MockMvc mockMvc;
  private WalletInputPort walletInputPort;
  private BalanceResponse response = BalanceResponse.builder()
      .userId(42L)
      .balance(BigDecimal.TEN)
      .build();

  @BeforeEach
  void setup() {
    walletInputPort = mock(WalletInputPort.class);
    TransactionInputPort transactionInputPort = mock(TransactionInputPort.class);
    mockMvc = MockMvcBuilders.standaloneSetup(new WalletAPI(walletInputPort, transactionInputPort))
        .build();
  }

  @Test
  void testGetBalanceReturnsCorrectJson() throws Exception {

    // Arrange
    when(walletInputPort.checkBalance(response.getUserId())).thenReturn(response);

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/wallets/balance?user_id=" + response.getUserId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(response.getBalance()))
        .andExpect(jsonPath("$.user_id").value(response.getUserId()));
  }
//
//  @Test
//  void getBalance_returnsCorrectJson_whenBalanceRetrieved() throws Exception {
//
//    // Arrange
//    when(walletInputPort.checkBalance(response.getUserId())).thenReturn(response);
//
//    // Act & Assert
//    mockMvc.perform(MockMvcRequestBuilders.get("/wallets/balance?user_id=" + response.getUserId()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.balance").value(response.getBalance()))
//        .andExpect(jsonPath("$.user_id").value(response.getUserId()));
//  }

}
