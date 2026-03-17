package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingTaxTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingTaxTypeEntity;
import org.mapstruct.*;
import vn.softz.app.einvoicehub.dto.EinvMappingUnitDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingTaxTypeMapper extends EntityMapper<EinvMappingTaxTypeDto, EinvMappingTaxTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "taxTypeId", ignore = true)
    @Mapping(target = "providerTaxTypeId", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingTaxTypeEntity toEntity(EinvMappingTaxTypeDto dto);

    List<EinvMappingTaxTypeDto> toDtoList(List<EinvMappingTaxTypeEntity> list);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingTaxTypeEntity entity, EinvMappingTaxTypeDto dto);
}
