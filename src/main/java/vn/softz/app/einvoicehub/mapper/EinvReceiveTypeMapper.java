package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvReceiveTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvReceiveTypeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvReceiveTypeMapper extends EntityMapper<EinvReceiveTypeDto, EinvReceiveTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvReceiveTypeEntity toEntity(EinvReceiveTypeDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvReceiveTypeEntity entity, EinvReceiveTypeDto dto);
}
