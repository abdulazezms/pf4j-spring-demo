package com.aziz.pf4jspringpaymentplugins.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bank_plugin", uniqueConstraints = {
        @UniqueConstraint(columnNames = "bankId"),
        @UniqueConstraint(columnNames = "pluginId")
})
@Getter
@Setter
public class BankPlugin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String bankId;

    @Column(nullable = false)
    private String pluginId;
}

