package vn.softz.app.einvoicehub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderRequest;

@Mapper(componentModel = "spring")
public interface EinvStoreProviderMapper {
    
    EinvStoreProviderDto toDto(EinvStoreProviderEntity entity);

    @Mapping(source = "bkavPartnerGuid", target = "partnerId")
    @Mapping(source = "bkavPartnerToken", target = "partnerToken")
    @Mapping(source = "mobifoneUsername", target = "partnerUsr")
    @Mapping(source = "mobifonePassword", target = "partnerPwd")
    @Mapping(source = "mobifoneTaxCode", target = "taxCode")
    EinvStoreProviderEntity toEntity(EinvStoreProviderRequest request);

    @Mapping(source = "bkavPartnerGuid", target = "partnerId")
    @Mapping(source = "bkavPartnerToken", target = "partnerToken")
    @Mapping(source = "mobifoneUsername", target = "partnerUsr")
    @Mapping(source = "mobifonePassword", target = "partnerPwd")
    @Mapping(source = "mobifoneTaxCode", target = "taxCode")
    void updateEntity(EinvStoreProviderRequest request, @MappingTarget EinvStoreProviderEntity entity);
}
