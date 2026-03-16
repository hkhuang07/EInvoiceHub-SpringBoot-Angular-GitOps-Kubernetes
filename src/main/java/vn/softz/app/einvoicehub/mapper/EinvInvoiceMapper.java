package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvInvoiceDto;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.SubmitInvoiceResponse;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceEntity;
import org.mapstruct.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EinvInvoiceDetailMapper.class})
public interface EinvInvoiceMapper {

    
    //Entity → DTO
    @Mapping(source = "details",        target = "details")
    @Mapping(source = "extraMetadata",  target = "extraMetadata",
            qualifiedByName = "objectToJsonNode")
    @Mapping(source = "deliveryInfo",   target = "deliveryInfo",
            qualifiedByName = "objectToJsonNode")
    @Mapping(source = "taxSummaryJson", target = "taxSummaryJson",
            qualifiedByName = "objectToJsonNode")
    EinvInvoiceDto toDto(EinvInvoiceEntity entity);

    List<EinvInvoiceDto> toDtoList(List<EinvInvoiceEntity> entities);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "details",     ignore = true)
    EinvInvoiceEntity toEntity(EinvInvoiceDto dto);

    
    // B. SubmitInvoiceRequest → EinvInvoiceEntity
    

    /**
     * FIX-3 & FIX-4: Thêm ignore cho isLocked, isDeleted, createdBy, updatedBy.
     * invoiceDate: SubmitInvoiceRequest.invoiceDate là LocalDateTime → map trực tiếp.
     */
    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "tenantId",           ignore = true)
    @Mapping(target = "storeId",            ignore = true)
    @Mapping(target = "providerId",         ignore = true)
    @Mapping(target = "providerInvoiceId",  ignore = true)
    @Mapping(target = "statusId",           ignore = true)
    @Mapping(target = "invoiceNo",          ignore = true)
    @Mapping(target = "signedDate",         ignore = true)
    @Mapping(target = "taxAuthorityCode",   ignore = true)
    @Mapping(target = "invoiceLookupCode",  ignore = true)
    @Mapping(target = "taxStatusId",        ignore = true)
    @Mapping(target = "cqtResponseCode",    ignore = true)
    @Mapping(target = "providerResponseId", ignore = true)
    @Mapping(target = "secretCode",         ignore = true)
    @Mapping(target = "extraMetadata",      ignore = true)
    @Mapping(target = "deliveryInfo",       ignore = true)
    @Mapping(target = "totalAmountText",    ignore = true)
    @Mapping(target = "taxSummaryJson",     ignore = true)
    @Mapping(target = "responseMessage",    ignore = true)
    @Mapping(target = "errorCode",          ignore = true)
    @Mapping(target = "isLocked",           ignore = true)
    @Mapping(target = "isDeleted",          ignore = true)
    @Mapping(target = "createdBy",          ignore = true)
    @Mapping(target = "updatedBy",          ignore = true)
    @Mapping(target = "createdDate",        ignore = true)
    @Mapping(target = "updatedDate",        ignore = true)
    @Mapping(target = "details",            ignore = true)
    EinvInvoiceEntity requestToEntity(SubmitInvoiceRequest request);

    
    // C. Entity → Response DTOs
    default GetInvoicesResponse toGetInvoicesResponse(EinvInvoiceEntity entity) {
        if (entity == null) return null;
        return GetInvoicesResponse.builder()
                .invoice(toDto(entity))
                .build();
    }

    default SubmitInvoiceResponse toSubmitInvoiceResponse(EinvInvoiceEntity entity) {
        if (entity == null) return null;
        Byte sid = entity.getStatusId();
        return SubmitInvoiceResponse.builder()
                .invoiceId(entity.getId())
                .partnerInvoiceId(entity.getPartnerInvoiceId())
                .invoiceNo(entity.getInvoiceNo())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedDate())
                .build();
    }

    
    // D. Update mappings
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",               ignore = true)
    @Mapping(target = "tenantId",         ignore = true)
    @Mapping(target = "storeId",          ignore = true)
    @Mapping(target = "providerId",       ignore = true)
    @Mapping(target = "partnerInvoiceId", ignore = true)
    @Mapping(target = "referenceTypeId",  ignore = true)
    @Mapping(target = "orgInvoiceId",     ignore = true)
    @Mapping(target = "createdBy",        ignore = true)
    @Mapping(target = "createdDate",      ignore = true)
    @Mapping(target = "updatedDate",      ignore = true)
    @Mapping(target = "details",          ignore = true)
    void updateEntity(EinvInvoiceDto dto, @MappingTarget EinvInvoiceEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",               ignore = true)
    @Mapping(target = "tenantId",         ignore = true)
    @Mapping(target = "storeId",          ignore = true)
    @Mapping(target = "partnerInvoiceId", ignore = true)
    @Mapping(target = "invoiceTypeId",    ignore = true)
    @Mapping(target = "referenceTypeId",  ignore = true)
    @Mapping(target = "invoiceDate",      ignore = true)
    @Mapping(target = "paymentMethodId",  ignore = true)
    @Mapping(target = "buyerTaxCode",     ignore = true)
    @Mapping(target = "buyerCompany",     ignore = true)
    @Mapping(target = "buyerFullName",    ignore = true)
    @Mapping(target = "buyerAddress",     ignore = true)
    @Mapping(target = "grossAmount",      ignore = true)
    @Mapping(target = "discountAmount",   ignore = true)
    @Mapping(target = "netAmount",        ignore = true)
    @Mapping(target = "taxAmount",        ignore = true)
    @Mapping(target = "totalAmount",      ignore = true)
    @Mapping(target = "isDraft",          ignore = true)
    @Mapping(target = "isMtt",            ignore = true)
    @Mapping(target = "isPetrol",         ignore = true)
    @Mapping(target = "isLocked",         ignore = true)
    @Mapping(target = "isDeleted",        ignore = true)
    @Mapping(target = "details",          ignore = true)
    @Mapping(target = "createdDate",      ignore = true)
    @Mapping(target = "updatedDate",      ignore = true)
    void applyProviderResult(EinvInvoiceEntity providerResult,
                             @MappingTarget EinvInvoiceEntity target);

    
    // E. @Named helpers
    

    /**
     * FIX-2: Object (Entity JSON column) → JsonNode (DTO).
     * EinvInvoiceEntity.extraMetadata/deliveryInfo/taxSummaryJson = Object
     * EinvInvoiceDto.extraMetadata/deliveryInfo/taxSummaryJson = JsonNode
     * MapStruct không tự convert → cần explicit @Named method.
     */
    @Named("objectToJsonNode")
    default JsonNode objectToJsonNode(Object value) {
        if (value == null) return null;
        if (value instanceof JsonNode) return (JsonNode) value;
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.valueToTree(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Named("bkavStatusToHubStatusId")
    default Integer bkavStatusToHubStatusId(Integer bkavStatus) {
        if (bkavStatus == null) return 0;
        return switch (bkavStatus) {
            case 1       -> 1;
            case 2, 3, 4 -> 2;
            case 7       -> 11;
            case 8       -> 8;
            case 9       -> 6;
            case 10      -> 9;
            case 11      -> 10;
            default      -> 0;
        };
    }

    @Named("mobiStatusToHubStatusId")
    default Integer mobiStatusToHubStatusId(Integer mobiStatus) {
        if (mobiStatus == null) return 0;
        return switch (mobiStatus) {
            case 1  -> 0;  case 5  -> 1;  case 10 -> 2;
            case 15 -> 11; case 20 -> 10; case 21 -> 8;
            case 25 -> 9;  case 30 -> 6;
            default -> 0;
        };
    }

    @Named("hubStatusIdToName")
    default String hubStatusIdToName(Integer statusId) {
        if (statusId == null) return "Không xác định";
        return switch (statusId) {
            case 0  -> "Mới tạo (chưa cấp số)";
            case 1  -> "Đã cấp số, chờ ký";
            case 2  -> "Đã phát hành";
            case 5  -> "HĐ thay thế - chờ ký";
            case 6  -> "HĐ thay thế - đã phát hành";
            case 7  -> "HĐ điều chỉnh - chờ ký";
            case 8  -> "HĐ điều chỉnh - đã phát hành";
            case 9  -> "HĐ bị thay thế";
            case 10 -> "HĐ bị điều chỉnh";
            case 11 -> "Đã hủy";
            default -> "Không xác định (" + statusId + ")";
        };
    }

    @Named("resolveInitialStatusId")
    default Integer resolveInitialStatusId(String submitInvoiceType) {
        if (submitInvoiceType == null) return 0;
        return switch (submitInvoiceType) {
            case "102" -> 2;
            case "111" -> 7;
            case "112" -> 8;
            default    -> 0;
        };
    }

    @Named("isStableStatus")
    default boolean isStableStatus(Integer statusId) {
        if (statusId == null) return false;
        return statusId == 2 || statusId == 6 || statusId == 8
                || statusId == 9 || statusId == 10;
    }

    @Named("buildOrgInvoiceIdentify")
    default String buildOrgInvoiceIdentify(String form, String series, String no) {
        if (form == null || series == null || no == null) return null;
        return "[" + form + "]_[" + series + "]_[" + no + "]";
    }

    @Named("buildUrlLookup")
    default String buildUrlLookup(String lookupBaseUrl, String invoiceLookupCode) {
        if (lookupBaseUrl == null || invoiceLookupCode == null) return null;
        return lookupBaseUrl.endsWith("/")
                ? lookupBaseUrl + invoiceLookupCode
                : lookupBaseUrl + "/" + invoiceLookupCode;
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(java.time.LocalDateTime dt) {
        if (dt == null) return null;
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Named("calcNetAmount")
    default BigDecimal calcNetAmount(BigDecimal grossAmount, BigDecimal discountAmount) {
        BigDecimal gross    = grossAmount    != null ? grossAmount    : BigDecimal.ZERO;
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        return gross.subtract(discount);
    }

    @Named("calcTotalAmount")
    default BigDecimal calcTotalAmount(BigDecimal netAmount, BigDecimal taxAmount) {
        BigDecimal net = netAmount != null ? netAmount : BigDecimal.ZERO;
        BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        return net.add(tax);
    }

    @Named("calcNetPrice")
    default BigDecimal calcNetPrice(BigDecimal netAmount, BigDecimal quantity) {
        if (netAmount == null || quantity == null
                || quantity.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return netAmount.divide(quantity, 2, RoundingMode.HALF_UP);
    }

    @Named("calcNetPriceVat")
    default BigDecimal calcNetPriceVat(BigDecimal totalAmount, BigDecimal quantity) {
        if (totalAmount == null || quantity == null
                || quantity.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return totalAmount.divide(quantity, 2, RoundingMode.HALF_UP);
    }
}