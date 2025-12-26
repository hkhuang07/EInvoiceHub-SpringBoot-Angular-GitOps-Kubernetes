package com.einvoicehub.core.entity.mysql;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import java.time.LocalDateTime;
import com.einvoicehub.core.enums.EntityStatus;
import com.einvoicehub.core.enums.SubscriptionPlan;

@Entity
@Table(name = "merchants")
@Data
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID externalId = UUID.randomUUID();

    @Column(unique = true, length = 20, nullable = false)
    private String taxCode;
    private String companyName;
    private String address;
    private String email;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
    @Enumerated(EnumType.STRING)
    private EntityStatus status = EntityStatus.ACTIVE;

    private Boolean isDeleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}


