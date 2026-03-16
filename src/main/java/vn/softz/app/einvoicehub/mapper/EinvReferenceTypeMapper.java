package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvReferenceTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvReferenceTypeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvReferenceTypeMapper extends EntityMapper<EinvReferenceTypeDto, EinvReferenceTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvReferenceTypeEntity toEntity(EinvReferenceTypeDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvReferenceTypeEntity entity, EinvReferenceTypeDto dto);
}
