package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.EinvPaymentMethodDto;
import vn.softz.app.einvoicehub.domain.entity.EinvPaymentMethodEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvPaymentMethodMapper extends EntityMapper<EinvPaymentMethodDto, EinvPaymentMethodEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name",ignore = true)
    @Mapping(target = "note",ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvPaymentMethodEntity toEntity(EinvPaymentMethodDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvPaymentMethodEntity entity, EinvPaymentMethodDto dto);
}
