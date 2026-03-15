package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvInvoiceTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceTypeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvInvoiceTypeMapper extends EntityMapper<EinvInvoiceTypeDto, EinvInvoiceTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvInvoiceTypeEntity toEntity(EinvInvoiceTypeDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvInvoiceTypeEntity entity, EinvInvoiceTypeDto dto);
}
