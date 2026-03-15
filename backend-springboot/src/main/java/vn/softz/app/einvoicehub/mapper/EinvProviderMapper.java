package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvProviderDto;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvProviderMapper extends EntityMapper<EinvProviderDto, EinvProviderEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerCode", ignore = true)
    @Mapping(target = "providerName", ignore = true)
    @Mapping(target = "integrationUrl", ignore = true)
    @Mapping(target = "lookupUrl", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvProviderEntity toEntity(EinvProviderDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvProviderEntity entity, EinvProviderDto dto);
}
