package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingReferenceTypeDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingReferenceTypeEntity;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingReferenceTypeMapper extends EntityMapper<EinvMappingReferenceTypeDto, EinvMappingReferenceTypeEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "referenceTypeId", ignore = true)
    @Mapping(target = "providerReferenceTypeId", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingReferenceTypeEntity toEntity(EinvMappingReferenceTypeDto dto);

    List<EinvMappingReferenceTypeDto> toDtoList(List<EinvMappingReferenceTypeEntity> list);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingReferenceTypeEntity entity, EinvMappingReferenceTypeDto dto);
}
