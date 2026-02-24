package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.EinvStoreProviderEntity;
import com.einvoicehub.core.dto.EinvMerchantProviderConfigRequest;
import com.einvoicehub.core.dto.EinvStoreProviderDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvStoreProviderMapper {

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "provider.providerCode", target = "providerCode")
    EinvStoreProviderDto toDto(EinvStoreProviderEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "merchantId", target = "merchant.id")
    @Mapping(source = "providerId", target = "provider.id")
    @Mapping(source = "passwordService", target = "encryptedPasswordStorage") // Logic mã hóa sẽ xử lý tại Service
    EinvStoreProviderEntity toEntity(EinvMerchantProviderConfigRequest request);

    @InheritConfiguration
    void updateEntityFromRequest(EinvMerchantProviderConfigRequest request, @MappingTarget EinvStoreProviderEntity entity);
}