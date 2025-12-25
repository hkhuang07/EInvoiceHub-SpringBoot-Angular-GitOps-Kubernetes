package com.einvoicehub.core.entity.mysql;

@Entity
@Table(name = "merchants")
@Data
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY);
    private uuid id;
    @Column(unique = true, lenght = 20)
    private String taxCode;
    private String companyName;
    private String address;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
    @Enumerated(EnumType.STRING)
    private EntityStatus status = EntityStatus.ACTIVE;

    private Boolean isDeleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}


