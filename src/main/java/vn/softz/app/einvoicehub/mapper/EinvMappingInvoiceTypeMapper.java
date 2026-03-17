package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceTypeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingInvoiceTypeMapper extends EntityMapper<EinvMappingInvoiceTypeDto, EinvMappingInvoiceTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "invoiceTypeId", ignore = true)
    @Mapping(target = "providerInvoiceTypeId", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note",ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingInvoiceTypeEntity toEntity(EinvMappingInvoiceTypeDto dto);

    List<EinvMappingInvoiceTypeDto> toDtoList(List<EinvMappingInvoiceTypeEntity> list);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingInvoiceTypeEntity entity, EinvMappingInvoiceTypeDto dto);
}
