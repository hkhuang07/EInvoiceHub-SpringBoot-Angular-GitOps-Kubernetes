package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvStoreDto;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvStoreMapper extends EntityMapper<EinvStoreDto, EinvStoreEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "storeName", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvStoreEntity toEntity(EinvStoreDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvStoreEntity entity, EinvStoreDto dto);
}
