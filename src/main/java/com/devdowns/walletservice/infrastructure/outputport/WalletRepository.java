package com.devdowns.walletservice.infrastructure.outputport;

import com.devdowns.walletservice.domain.entity.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

  Optional<Wallet> findByUserId(long userId);
}
