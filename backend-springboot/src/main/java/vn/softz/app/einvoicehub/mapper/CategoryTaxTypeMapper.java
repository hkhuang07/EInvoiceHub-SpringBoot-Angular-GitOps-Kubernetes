package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.catalog.CategoryTaxTypeDto;
import vn.softz.app.einvoicehub.domain.entity.CategoryTaxType;
import org.mapstruct.*;

/**
 * Mapper cho bảng CategoryTaxType - Loại thuế
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryTaxTypeMapper extends EntityMapper<CategoryTaxTypeDto, CategoryTaxType> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    CategoryTaxType toEntity(CategoryTaxTypeDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget CategoryTaxType entity, CategoryTaxTypeDto dto);
}
