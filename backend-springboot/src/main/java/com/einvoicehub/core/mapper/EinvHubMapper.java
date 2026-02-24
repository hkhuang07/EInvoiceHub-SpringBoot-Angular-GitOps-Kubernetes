package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.dto.*;
import com.einvoicehub.core.dto.request.EinvInvoiceAdjustmentRequest;
import com.einvoicehub.core.dto.request.SubmitInvoice.*;
import com.einvoicehub.core.dto.response.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EinvHubMapper {
    //  1. Catalog
    EinvInvoiceTypeDto toDto(EinvInvoiceTypeEntity entity);
    EinvInvoiceTypeEntity toEntity(EinvInvoiceTypeDto dto);
    EinvInvoiceStatusDto toDto(EinvInvoiceStatusEntity entity);
    EinvInvoiceStatusEntity toEntity(EinvInvoiceStatusDto dto);
    EinvPaymentMethodDto toDto(EinvPaymentMethodEntity entity);
    EinvPaymentMethodEntity toEntity(EinvPaymentMethodDto dto);
    EinvVatRateDto toDto(EinvVatRateEntity entity);
    EinvVatRateEntity toEntity(EinvVatRateDto dto);
    EinvRegistrationStatusDto toDto(EinvRegistrationStatusEntity entity);
    EinvRegistrationStatusEntity toEntity(EinvRegistrationStatusDto dto);
    EinvServiceProviderDto toDto(EinvProviderEntity entity);
    EinvProviderEntity toEntity(EinvServiceProviderDto dto);
    EinvInvoicePayloadDto toDto(EinvInvoicePayloadEntity entity);
    EinvInvoicePayloadEntity toEntity(EinvInvoicePayloadDto dto);
    EinvSystemConfigDto toDto(EinvSystemConfigEntity entity);
    EinvSystemConfigEntity toEntity(EinvSystemConfigDto dto);

    EinvTaxAuthorityResponseDto toDto(EinvTaxAuthorityResponseEntity entity);
    EinvTaxAuthorityResponseEntity toEntity(EinvTaxAuthorityResponseDto dto);
    EinvInvoiceSyncQueueResponse toDto(EinvInvoiceSyncQueueEntity entity);
    EinvInvoiceSyncQueueEntity toEntity(EinvInvoiceSyncQueueResponse dto);

    //  2. Merchant & User
    EinvMerchantEntity toEntity(EinvMerchantRequest request);
    EinvMerchantResponse toResponse(EinvMerchantEntity entity);
    EinvMerchantUserEntity toEntity(EinvMerchantUserRequest request);
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "merchant.taxCode", target = "merchantTaxCode")
    EinvMerchantUserResponse toResponse(EinvMerchantUserEntity entity);


    //  3. Config & Registration
    EinvStoreProviderEntity toEntity(EinvMerchantProviderConfigRequest request);
    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "provider.providerName", target = "providerName")
    @Mapping(source = "provider.providerCode", target = "providerCode")
    EinvMerchantProviderConfigResponse toResponse(EinvStoreProviderEntity entity);

    EinvInvoiceRegistrationEntity toEntity(EinvInvoiceRegistrationRequest request);
    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "status.name", target = "statusName")
    EinvInvoiceRegistrationResponse toResponse(EinvInvoiceRegistrationEntity entity);

    EinvInvoiceTemplateEntity toEntity(EinvInvoiceTemplateRequest request);
    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "invoiceType.typeName", target = "invoiceTypeName")
    @Mapping(source = "registration.registrationNumber", target = "registrationNumber")
    EinvInvoiceTemplateResponse toResponse(EinvInvoiceTemplateEntity entity);

    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "invoiceStatus.name", target = "statusName")
    @Mapping(source = "invoiceStatus.note", target = "statusMessage")
    @Mapping(source = "paymentMethodEntity.methodName", target = "paymentMethod")
    EinvInvoiceMetadataResponse toResponse(EinvInvoiceEntity entity);

    EinvInvoiceItemDto toDto(EinvInvoiceDetailEntity entity);
    List<EinvInvoiceItemDto> toItemDtoList(List<EinvInvoiceDetailEntity> entities);

    EinvInvoiceTaxBreakdownDto toDto(EinvInvoiceTaxBreakDownEntity entity);
    List<EinvInvoiceTaxBreakdownDto> toTaxDtoList(List<EinvInvoiceTaxBreakDownEntity> entities);

    //Tọa hóa đơn
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "invoiceTypeId", target = "invoiceTemplate.id")
    @Mapping(source = "buyerCompany", target = "buyerName")
    EinvInvoiceEntity toEntity(SubmitInvoiceRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "itemName", target = "productName")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(source = "taxTypeId", target = "vatRate.id")
    EinvInvoiceDetailEntity toEntity(SubmitInvoiceDetailRequest request);
    EinvInvoiceDetailEntity toEntity(EinvInvoiceItemDto dto);

    @Mapping(source = "invoiceNumber", target = "invoiceNumber")
    @Mapping(source = "lookupCode", target = "lookupCode")
    @Mapping(source = "taxAuthorityCode", target = "taxAuthorityCode")
    SubmitInvoiceResponse toDto(EinvInvoiceEntity entity);

    //Danh sách và tra cứu
    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "invoiceStatus.name", target = "statusName")
    @Mapping(source = "paymentMethodEntity.methodName", target = "paymentMethodName")
    @Mapping(source = "createdAt", target = "createdAt")
    ListInvoicesResponse toListResponse(EinvInvoiceEntity entity);
    List<ListInvoicesResponse> toListResponseList(List<EinvInvoiceEntity> entities);
    List<ListInvoicesResponse> toDto(List<EinvInvoiceEntity> entities);

    @Mapping(source = "id", target = "invoiceId")
    @Mapping(source = "invoiceStatus.id", target = "invoiceStatusId")
    GetInvoicesResponse toGetInvoicesResponse(EinvInvoiceEntity entity);
    GetInvoicesResponse toEntity(EinvInvoiceEntity entity);

    //  5. Adjustment & Operationss
    EinvInvoiceAdjustmentsEntity toEntity(EinvInvoiceAdjustmentRequest request);
    @Mapping(source = "originalInvoice.invoiceNumber", target = "originalInvoiceNumber")
    @Mapping(source = "originalInvoice.symbolCode", target = "originalSymbolCode")
    EinvInvoiceAdjustmentResponse toResponse(EinvInvoiceAdjustmentsEntity entity);

    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "user.username", target = "userName")
    EinvAuditLogResponse toResponse(EinvAuditLogsEntity entity);

    @Mapping(source = "invoice.invoiceNumber", target = "invoiceNumber")
    EinvInvoiceSyncQueueResponse toResponse(EinvInvoiceSyncQueueEntity entity);

    @Mapping(source = "merchant.companyName", target = "merchantName")
    EinvApiCredentialDto toDto(EinvApiCredentialsEntity entity);


    //Ký số
    @Mapping(source = "signedAt", target = "signedAt")
    @Mapping(target = "status", constant = "SIGNED")
    SignInvoiceResponse toSignResponse(EinvInvoiceEntity entity);

    @Mapping(source = "merchant.companyName", target = "merchantName")
    @Mapping(source = "user.username", target = "userName")
    EinvAuditLogResponse toAuditResponse(EinvAuditLogsEntity entity);
    EinvAuditLogResponse toDto(EinvAuditLogsEntity entity);

    @Mapping(source = "invoice.invoiceNumber", target = "invoiceNumber")
    EinvInvoiceSyncQueueResponse toSyncResponse(EinvInvoiceSyncQueueEntity entity);

}