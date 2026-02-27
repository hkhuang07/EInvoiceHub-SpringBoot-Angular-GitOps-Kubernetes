package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.EinvStoreSerialEntity;
import com.einvoicehub.core.dto.EinvStoreSerialDto;
import com.einvoicehub.core.dto.request.EinvStoreSerialRequest;
import org.mapstruct.*;

import java.util.List;

/** Mapper chuyển đổi cho cấu hình Dải ký hiệu / Mẫu số hóa đơn (einv_store_serial).*/
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EinvStoreSerialMapper {

    
    // 1. MAPPING ENTITY -> DTO
    @Mapping(source = "storeProvider.id", target = "storeProviderId")
    @Mapping(source = "invoiceType.id", target = "invoiceTypeId")
    @Mapping(source = "invoiceType.invoiceTypeName", target = "invoiceTypeName")
    EinvStoreSerialDto toDto(EinvStoreSerialEntity entity);

    List<EinvStoreSerialDto> toDtoList(List<EinvStoreSerialEntity> entities);

    
    // 2. MAPPING REQUEST -> ENTITY
    @Mapping(target = "id", ignore = true) // UUID sinh tự động hoặc từ logic Service
    @Mapping(source = "storeProviderId", target = "storeProvider.id")
    @Mapping(source = "invoiceTypeId", target = "invoiceType.id")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    EinvStoreSerialEntity toEntity(EinvStoreSerialRequest request);

    
    // 3. CẬP NHẬT ENTITY
    @InheritConfiguration(name = "toEntity")
    void updateEntityFromRequest(EinvStoreSerialRequest request, @MappingTarget EinvStoreSerialEntity entity);
}