package com.aziz.pf4jspringpaymentplugins.api.repository;

import com.aziz.pf4jspringpaymentplugins.api.entity.BankPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankPluginRepository extends JpaRepository<BankPlugin, String> {
    Optional<BankPlugin> findByBankId(String bankId);

    boolean existsByBankId(String bankId);
}
