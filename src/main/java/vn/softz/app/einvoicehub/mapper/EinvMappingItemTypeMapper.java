package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingItemTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingItemTypeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingItemTypeMapper extends EntityMapper<EinvMappingItemTypeDto, EinvMappingItemTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "itemTypeId", ignore = true)
    @Mapping(target = "providerItemTypeId", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingItemTypeEntity toEntity(EinvMappingItemTypeDto dto);

    List<EinvMappingItemTypeDto> toDtoList(List<EinvMappingItemTypeEntity> list);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingItemTypeEntity entity, EinvMappingItemTypeDto dto);
}
