package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingActionDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingActionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingActionMapper extends EntityMapper<EinvMappingActionDto, EinvMappingActionEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "hubAction", ignore = true)
    @Mapping(target = "providerCmd", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingActionEntity toEntity(EinvMappingActionDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingActionEntity entity, EinvMappingActionDto dto);
}
