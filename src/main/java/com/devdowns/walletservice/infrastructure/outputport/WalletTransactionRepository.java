package com.devdowns.walletservice.infrastructure.outputport;

import com.devdowns.walletservice.domain.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

  Page<WalletTransaction> findAll(Specification<WalletTransaction> spec, Pageable pageable);
}