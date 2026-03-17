package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingUnitDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingUnitEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingUnitMapper extends EntityMapper<EinvMappingUnitDto, EinvMappingUnitEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "unitCode", ignore = true)
    @Mapping(target = "providerUnitCode", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingUnitEntity toEntity(EinvMappingUnitDto dto);

    List<EinvMappingUnitDto> toDtoList(List<EinvMappingUnitEntity> list);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingUnitEntity entity, EinvMappingUnitDto dto);
}
