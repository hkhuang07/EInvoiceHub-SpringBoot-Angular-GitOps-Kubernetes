package com.einvoicehub.core.entity.mysql;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;



@Entity
@Table(name = "api_credebtials")
@Data
public class ApiCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private String clientId;
    private String apiKeyHash;
    private String ipWhitelist;
    private String scopes;
    private LocalDateTime expiredAt;
}
