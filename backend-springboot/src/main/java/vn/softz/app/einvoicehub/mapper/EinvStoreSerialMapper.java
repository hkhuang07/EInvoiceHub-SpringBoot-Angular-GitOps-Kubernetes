package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvStoreSerialDto;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvStoreSerialMapper extends EntityMapper<EinvStoreSerialDto, EinvStoreSerialEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvStoreSerialEntity toEntity(EinvStoreSerialDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvStoreSerialEntity entity, EinvStoreSerialDto dto);
}
