package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.EinvStoreProviderEntity;
import com.einvoicehub.core.dto.EinvStoreProviderDto;
import com.einvoicehub.core.dto.request.EinvStoreProviderRequest;
import org.mapstruct.*;

/** Mapper chuyển đổi giữa Entity, DTO và Request cho cấu hình tích hợp Store-Provider.*/
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EinvStoreProviderMapper {
    // 1. MAPPING ENTITY -> DTO
    EinvStoreProviderDto toDto(EinvStoreProviderEntity entity);

    // 2. MAPPING REQUEST -> ENTITY
    @Mapping(target = "id", ignore = true) // ID sẽ được sinh UUID ở Service hoặc DB
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "integratedDate", ignore = true)
    EinvStoreProviderEntity toEntity(EinvStoreProviderRequest request);


    // 3. CẬP NHẬT ENTITY TỪ REQUEST
    @InheritConfiguration(name = "toEntity")
    void updateEntityFromRequest(EinvStoreProviderRequest request, @MappingTarget EinvStoreProviderEntity entity);

    /** Hỗ trợ chuyển đổi nhanh cho danh sách */
    java.util.List<EinvStoreProviderDto> toDtoList(java.util.List<EinvStoreProviderEntity> entities);
}