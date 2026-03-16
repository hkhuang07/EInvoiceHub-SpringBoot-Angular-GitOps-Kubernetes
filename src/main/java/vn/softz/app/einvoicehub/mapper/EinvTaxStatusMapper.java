package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvTaxStatusDto;
import vn.softz.app.einvoicehub.domain.entity.EinvTaxStatusEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvTaxStatusMapper extends EntityMapper<EinvTaxStatusDto, EinvTaxStatusEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvTaxStatusEntity toEntity(EinvTaxStatusDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvTaxStatusEntity entity, EinvTaxStatusDto dto);
}
