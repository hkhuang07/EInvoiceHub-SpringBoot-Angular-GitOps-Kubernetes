package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_provider")
public class EinvProviderEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    // Mã ngắn của nhà cung cấp (BKAV, VNPT, MOBI, MISA, VIETTEL)
    @Column(name = "provider_code", length = 36, nullable = false, unique = true)
    private String providerCode;

    @Column(name = "provider_name", length = 200)
    private String providerName;

    // Endpoint tích hợp chính với NCC
    @Column(name = "integration_url", length = 200)
    private String integrationUrl;

    // Base URL tra cứu hóa đơn
    @Column(name = "lookup_url", length = 200)
    private String lookupUrl;

    // TINYINT(1) in schema: 0: Hoạt động, 1: Tạm ngưng
    @Column(name = "inactive", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean inactive = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getIntegrationUrl() {
        return integrationUrl;
    }

    public void setIntegrationUrl(String integrationUrl) {
        this.integrationUrl = integrationUrl;
    }

    public String getLookupUrl() {
        return lookupUrl;
    }

    public void setLookupUrl(String lookupUrl) {
        this.lookupUrl = lookupUrl;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
}
