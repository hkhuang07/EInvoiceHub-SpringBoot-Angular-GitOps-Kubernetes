package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.request.EinvStoreProviderRequest;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderHistoryEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EinvStoreProviderMapper {

    // Entity ↔ DTO (Byte status ↔ Byte status
    EinvStoreProviderDto toDto(EinvStoreProviderEntity entity);

    List<EinvStoreProviderDto> toDtoList(List<EinvStoreProviderEntity> entities);

    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "createdDate",    ignore = true)
    @Mapping(target = "updatedDate",    ignore = true)
    EinvStoreProviderEntity toEntity(EinvStoreProviderDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "tenantId",       ignore = true)
    @Mapping(target = "storeId",        ignore = true)
    @Mapping(target = "providerId",     ignore = true)
    @Mapping(target = "integratedDate", ignore = true)
    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "createdDate",    ignore = true)
    @Mapping(target = "updatedDate",    ignore = true)
    void updateEntityFromDto(EinvStoreProviderDto dto,
                             @MappingTarget EinvStoreProviderEntity entity);

    // Request → Entity
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "tenantId",       ignore = true)
    @Mapping(target = "storeId",        ignore = true)
    @Mapping(target = "status",         ignore = true)
    @Mapping(target = "integratedDate", ignore = true)
    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "createdDate",    ignore = true)
    @Mapping(target = "updatedDate",    ignore = true)
    @Mapping(source = "bkavPartnerGuid",  target = "partnerId")
    @Mapping(source = "bkavPartnerToken", target = "partnerToken")
    @Mapping(source = "mobifoneUsername", target = "partnerUsr")
    @Mapping(source = "mobifonePassword", target = "partnerPwd")
    @Mapping(source = "mobifoneTaxCode",  target = "taxCode")
    EinvStoreProviderEntity requestToEntity(EinvStoreProviderRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "tenantId",       ignore = true)
    @Mapping(target = "storeId",        ignore = true)
    @Mapping(target = "providerId",     ignore = true)
    @Mapping(target = "status",         ignore = true)
    @Mapping(target = "integratedDate", ignore = true)
    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "createdDate",    ignore = true)
    @Mapping(target = "updatedDate",    ignore = true)
    @Mapping(source = "bkavPartnerGuid",  target = "partnerId")
    @Mapping(source = "bkavPartnerToken", target = "partnerToken")
    @Mapping(source = "mobifoneUsername", target = "partnerUsr")
    @Mapping(source = "mobifonePassword", target = "partnerPwd")
    @Mapping(source = "mobifoneTaxCode",  target = "taxCode")
    void updateEntity(EinvStoreProviderRequest request,
                      @MappingTarget EinvStoreProviderEntity entity);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "createdBy",    ignore = true)
    @Mapping(target = "updatedBy",    ignore = true)
    @Mapping(target = "createdDate",    ignore = true)
    @Mapping(target = "updatedDate",    ignore = true)
    @Mapping(source = "entity.tenantId",   target = "tenantId")
    @Mapping(source = "entity.storeId",    target = "storeId")
    @Mapping(source = "entity.providerId", target = "providerId")
    @Mapping(source = "entity.status",     target = "status")
    @Mapping(source = "actionType",        target = "actionType")
    @Mapping(source = "notes",             target = "notes")
    EinvStoreProviderHistoryEntity toHistory(EinvStoreProviderEntity entity,
                                             String actionType,
                                             String notes);

    @Named("byteToBoolean")
    default Boolean byteToBoolean(Byte value) {
        if (value == null) return null;
        return value != 0;
    }
}