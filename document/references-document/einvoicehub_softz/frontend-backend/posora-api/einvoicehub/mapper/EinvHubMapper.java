package vn.softz.app.einvoicehub.mapper;

import org.mapstruct.*;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceDetailEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceEntity;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoice.SubmitInvoiceDetailRequest;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoice.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceDetailData;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse.GetInvoiceDetailResponse;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EinvHubMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "storeId", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "providerInvoiceId", ignore = true)
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "referenceTypeId", ignore = true)
    @Mapping(target = "invoiceNo", ignore = true)
    @Mapping(target = "signedDate", ignore = true)
    @Mapping(target = "taxAuthorityCode", ignore = true)
    @Mapping(target = "invoiceLookupCode", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(source = "buyerName", target = "buyerFullName")
    @Mapping(source = "receiverTypeId", target = "receiveTypeId")
    @Mapping(source = "invoiceDate", target = "invoiceDate", qualifiedByName = "stringToInstant")
    EinvHubInvoiceEntity toEntity(SubmitInvoiceRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "lineNo", ignore = true)
    @Mapping(target = "isFree", ignore = true)
    @Mapping(target = "netPriceVat", ignore = true)
    @Mapping(target = "netPrice", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(source = "itemCode", target = "itemId")
    @Mapping(source = "unitName", target = "unit")
    EinvHubInvoiceDetailEntity toDetailEntity(SubmitInvoiceDetailRequest detail);

    List<EinvHubInvoiceDetailEntity> toDetailEntities(List<SubmitInvoiceDetailRequest> details);

    @Named("stringToInstant")
    default Instant stringToInstant(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
            .atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    // === Entity to Provider DTO mappings ===

    @Mapping(source = "invoiceSeries", target = "invoiceSerial")
    @Mapping(source = "invoiceDate", target = "invoiceDate")
    @Mapping(source = "buyerFullName", target = "buyerName")
    @Mapping(source = "buyerCompany", target = "buyerUnitName")
    @Mapping(source = "paymentMethodId", target = "payMethodId")
    @Mapping(source = "receiverEmail", target = "receiverEmail")
    @Mapping(source = "details", target = "details")
    InvoiceData toInvoiceData(EinvHubInvoiceEntity entity);

    @Mapping(source = "itemId", target = "itemCode")
    @Mapping(source = "unit", target = "unitName")
    @Mapping(source = "lineNo", target = "lineNo")
    @Mapping(source = "itemName", target = "itemName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "grossAmount", target = "amount")
    @Mapping(source = "taxRate", target = "taxRate")
    @Mapping(source = "taxAmount", target = "taxAmount")
    @Mapping(source = "discountRate", target = "discountRate")
    @Mapping(source = "discountAmount", target = "discountAmount")
    @Mapping(source = "taxTypeId", target = "taxRateId")
    InvoiceDetailData toInvoiceDetailData(EinvHubInvoiceDetailEntity detail);

    List<InvoiceDetailData> toInvoiceDetailDataList(List<EinvHubInvoiceDetailEntity> details);

    // === GetInvoices to Response ===
    @Mapping(source = "id", target = "invoiceId")
    @Mapping(source = "buyerFullName", target = "buyerName")
    @Mapping(source = "statusId", target = "invoiceStatusId")
    @Mapping(source = "providerId", target = "provider")
    @Mapping(source = "invoiceDate", target = "invoiceDate", qualifiedByName = "instantToString")
    @Mapping(source = "signedDate", target = "signedDate", qualifiedByName = "instantToString")
    GetInvoicesResponse toGetInvoicesResponse(EinvHubInvoiceEntity entity);

    @Mapping(source = "itemId", target = "itemCode")
    @Mapping(source = "unit", target = "unitName")
    GetInvoiceDetailResponse toGetInvoiceDetailResponse(EinvHubInvoiceDetailEntity detail);

    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    ListInvoicesResponse toListResponse(EinvHubInvoiceEntity entity);
}
