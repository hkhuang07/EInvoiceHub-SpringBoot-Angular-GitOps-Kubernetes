package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.dto.*;
import com.einvoicehub.core.dto.request.*;
import com.einvoicehub.core.dto.request.SubmitInvoice.*;
import com.einvoicehub.core.dto.response.*;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EinvHubMapper {

    
    // 1. NHÓM DANH MỤC
    EinvInvoiceTypeDto toDto(EinvInvoiceTypeEntity entity);
    EinvInvoiceStatusDto toDto(EinvInvoiceStatusEntity entity);
    EinvPaymentMethodDto toDto(EinvPaymentMethodEntity entity);
    EinvTaxTypeDto toDto(EinvTaxTypeEntity entity);
    EinvUnitDto toDto(EinvUnitEntity entity);
    EinvItemTypeDto toDto(EinvItemTypeEntity entity);
    EinvReceiveTypeDto toDto(EinvReceiveTypeEntity entity);
    EinvReferenceTypeDto toDto(EinvReferenceTypeEntity entity);
    EinvTaxStatusDto toDto(EinvTaxStatusEntity entity);

    EinvInvoiceTypeEntity toEntity(EinvInvoiceTypeDto dto);
    EinvInvoiceStatusEntity toEntity(EinvInvoiceStatusDto dto);
    EinvPaymentMethodEntity toEntity(EinvPaymentMethodDto dto);
    EinvTaxTypeEntity toEntity(EinvTaxTypeDto dto);
    EinvUnitEntity toEntity(EinvUnitDto dto);
    EinvItemTypeEntity toEntity(EinvItemTypeDto dto);
    EinvReceiveTypeEntity toEntity(EinvReceiveTypeDto dto);
    EinvReferenceTypeEntity toEntity(EinvReferenceTypeDto dto);
    EinvTaxStatusEntity toEntity(EinvTaxStatusDto dto);

    // 2. NHÓM MAPPING
    EinvMappingTaxTypeDto toDto(EinvMappingTaxTypeEntity entity);
    EinvMappingInvoiceTypeDto toDto(EinvMappingInvoiceTypeEntity entity);
    EinvMappingPaymentMethodDto toDto(EinvMappingPaymentMethodEntity entity);
    EinvMappingItemTypeDto toDto(EinvMappingItemTypeEntity entity);
    EinvMappingUnitDto toDto(EinvMappingUnitEntity entity);
    EinvMappingStatusDto toDto(EinvMappingStatusEntity entity);
    EinvMappingActionDto toDto(EinvMappingActionEntity entity);

    EinvMappingTaxTypeEntity toEntity(EinvMappingTaxTypeDto dto);
    EinvMappingInvoiceTypeEntity toEntity(EinvMappingInvoiceTypeDto dto);
    EinvMappingPaymentMethodEntity toEntity(EinvMappingPaymentMethodDto dto);
    EinvMappingItemTypeEntity toEntity(EinvMappingItemTypeDto dto);
    EinvMappingUnitEntity toEntity(EinvMappingUnitDto dto);
    EinvMappingStatusEntity toEntity(EinvMappingStatusDto dto);
    EinvMappingActionEntity toEntity(EinvMappingActionDto dto);



    // 3. NHÓM NGƯỜI DÙNG & MERCHANT
    SysUserDto toDto(SysUserEntity entity);
    MerchantDto toDto(MerchantEntity entity);
    EinvStoreDto toDto(EinvStoreEntity entity);

    SysUserEntity toEntity(SysUserDto dto);
    MerchantEntity toEntity(MerchantDto dto);
    EinvStoreEntity toEntity(EinvStoreDto dto);




    // 4. NGHIỆP VỤ HÓA ĐƠN CHÍNH
    //Entity to DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "statusId", target = "statusId")
    @Mapping(source = "invoiceDate", target = "invoiceDate", qualifiedByName = "dateTimeToString")
    EinvInvoiceDto toInvoiceDto(EinvInvoiceEntity entity);

    @Mapping(source = "itemCode", target = "itemCode")
    EinvInvoiceDetailDto toDetailDto(EinvInvoiceDetailEntity entity);

    //POS Request to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "invoiceDate", target = "invoiceDate", qualifiedByName = "stringToDateTime")
    @Mapping(source = "details", target = "details")
    EinvInvoiceEntity toEntity(SubmitInvoiceRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "unitName", target = "unitName")
    EinvInvoiceDetailEntity toEntity(SubmitInvoiceDetailRequest request);

    
    // 5. NHÓM PHẢN HỒI NGHIỆP VỤ
    //Phản hồi sau Submit
    @Mapping(source = "id", target = "id")
    @Mapping(source = "partnerInvoiceId", target = "partnerInvoiceId")
    @Mapping(source = "statusId", target = "statusId")
    @Mapping(source = "providerId", target = "provider")
    SubmitInvoiceResponse toSubmitResponse(EinvInvoiceEntity entity);
    //Phản hồi sau  Ký số
    @Mapping(source = "id", target = "invoiceId")
    @Mapping(source = "signedDate", target = "signedDate", qualifiedByName = "dateTimeToString")
    @Mapping(target = "isSuccess", expression = "java(entity.getStatusId() == 2)")
    SignInvoiceResponse toSignResponse(EinvInvoiceEntity entity);
    // Danh sách
    @Mapping(source = "id", target = "id")
    @Mapping(source = "statusId", target = "statusId")
    ListInvoicesResponse toListResponse(EinvInvoiceEntity entity);

    
    // 6. CẤU HÌNH VẬN HÀNH
    EinvStoreProviderEntity toEntity(EinvStoreProviderRequest request);
    EinvStoreSerialEntity toEntity(EinvStoreSerialRequest request);

    EinvStoreProviderRequest toDto(EinvStoreProviderEntity entity);
    @Mapping(source = "storeProvider.id", target = "storeProviderId")
    EinvStoreSerialDto toDto(EinvStoreSerialEntity entity);

    
    // 7. DATETIME
    @Named("dateTimeToString")
    default String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Named("stringToDateTime")
    default LocalDateTime stringToDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            if (dateStr.length() == 10) dateStr += " 00:00:00";
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }
}