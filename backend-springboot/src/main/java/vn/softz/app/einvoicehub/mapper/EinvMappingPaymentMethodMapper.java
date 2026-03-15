package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingPaymentMethodDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingPaymentMethodEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvMappingPaymentMethodMapper extends EntityMapper<EinvMappingPaymentMethodDto, EinvMappingPaymentMethodEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "paymentMethodId", ignore = true)
    @Mapping(target = "providerPaymentMethodId", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvMappingPaymentMethodEntity toEntity(EinvMappingPaymentMethodDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvMappingPaymentMethodEntity entity, EinvMappingPaymentMethodDto dto);
}
