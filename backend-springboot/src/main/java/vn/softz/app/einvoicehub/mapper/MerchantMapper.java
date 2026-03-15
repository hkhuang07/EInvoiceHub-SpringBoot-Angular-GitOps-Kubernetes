package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.MerchantDto;
import vn.softz.app.einvoicehub.domain.entity.MerchantEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper extends EntityMapper<MerchantDto, MerchantEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "companyName", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    MerchantEntity toEntity(MerchantDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget MerchantEntity entity, MerchantDto dto);
}
