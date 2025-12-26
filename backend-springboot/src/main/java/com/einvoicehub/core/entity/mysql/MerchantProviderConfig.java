package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.converter.EncryptionConverter;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.mongodb.core.convert.encryption.MongoEncryptionConverter;
import org.springframework.data.mongodb.core.encryption.EncryptionContext;

@Entity
@Table(name="merchant_provider_configs")
@Data
public class MerchantProviderConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private String providerCode; //VNPT, VIETTEL
    private String usernameService;

    @Convert(converter = EncryptionConverter.class)
    private String passwordEncrypted;

    @Column(columnDefinition = "JSON")
    private String extraConfig; // Lưu Pattern, Serial
}
