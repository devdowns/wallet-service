package com.devdowns.walletservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "surname", nullable = false, length = 50)
  private String surname;

  @Column(name = "national_id", nullable = false, length = 20)
  private String nationalId;

  @Column(name = "account_number", nullable = false, length = 50)
  private String accountNumber;

  @Column(name = "routing_number", nullable = false, length = 50)
  private String routingNumber;

  @Column(name = "bank_name", nullable = false, length = 50)
  private String bankName;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;
}