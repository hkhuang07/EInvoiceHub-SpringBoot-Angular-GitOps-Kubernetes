package vn.softz.app.einvoicehub.provider.mobifone.mapper;

import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.provider.mobifone.constant.MobifoneInvoiceStatus;
import vn.softz.app.einvoicehub.provider.mobifone.model.*;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceDetailData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;
import vn.softz.core.common.Common;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MobifoneDataMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    private final EinvStoreProviderRepository storeProviderRepository;
    private final EinvoiceMappingService mappingService;

    private String mapPaymentMethodName(String lid, Integer payMethodId) {
        if (payMethodId == null) {
            return "";
        }

        String providerName = mappingService.getProviderCode(
                lid,
                "MOBI",
                MappingType.PAYMENT_METHOD,
                String.valueOf(payMethodId));

        log.info("[MOBI_MAPPING] PAYMENT_METHOD - MAPPED: {} → '{}'", payMethodId, providerName);
        return providerName;
    }

    private Integer mapItemTypeCode(String lid, Integer itemTypeId) {
        if (itemTypeId == null) {
            itemTypeId = 0; // item type mặc định nếu không có
        }

        String mappedStr = mappingService.getProviderCode(
                lid,
                "MOBI",
                MappingType.ITEM_TYPE,
                String.valueOf(itemTypeId));

        try {
            Integer result = Integer.parseInt(mappedStr);
            log.info("[MOBI_MAPPING] ITEM_TYPE - MAPPED: {} → {} (provider code)", itemTypeId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[MOBI_MAPPING] ITEM_TYPE - Parse failed for '{}', fallback to: {}", mappedStr, itemTypeId);
            return itemTypeId;
        }
    }
    
    private Integer mapInvoiceType(String lid, Integer invoiceTypeId) {
        if (invoiceTypeId == null) {
            invoiceTypeId = 1;
        }

        String mappedStr = mappingService.getProviderCode(
                lid,
                "MOBI",
                MappingType.INVOICE_TYPE,
                String.valueOf(invoiceTypeId));

        try {
            Integer result = Integer.parseInt(mappedStr);
            log.info("[MOBI_MAPPING] INVOICE_TYPE - MAPPED: {} → {} (provider code)", invoiceTypeId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[MOBI_MAPPING] INVOICE_TYPE - Parse failed for '{}', fallback to: {}", mappedStr, invoiceTypeId);
            return invoiceTypeId;
        }
    }

    private String mapTaxRate(String lid, String taxTypeId, BigDecimal defaultRate) {
        String defaultRateStr = defaultRate != null ? String.valueOf(defaultRate) : "10";
        if (taxTypeId == null || taxTypeId.isEmpty()) {
            return defaultRateStr;
        }

        String mappedStr = mappingService.getProviderCode(lid, "MOBI", MappingType.TAX_TYPE, taxTypeId);
        
        // Cache stores: "providerTaxTypeId,providerTaxRate"
        if (mappedStr != null && mappedStr.contains(",")) {
            String[] parts = mappedStr.split(",");
            String providerTaxRate = parts.length > 1 ? parts[1] : defaultRateStr;
            log.info("[MOBI_MAPPING] TAX_TYPE - MAPPED: {} → {} (rate)", taxTypeId, providerTaxRate);
            return providerTaxRate;
        }
        
        log.warn("[MOBI_MAPPING] TAX_TYPE - Mapping invalid or not found for '{}', using default", taxTypeId);
        return defaultRateStr;
    }

    private EinvStoreProviderEntity getConfig() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        return storeProviderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình HĐĐT cho cửa hàng này"));
    }

    public MobifoneInvoiceRequest toMobifoneInvoiceRequest(InvoiceData invoiceData) {
        String lid = invoiceData.getLid();
        return MobifoneInvoiceRequest.builder()
                .editmode(MobifoneInvoiceStatus.EDIT_MODE_CREATE)
                .taxCode(getConfig().getTaxCode())
                .data(List.of(toMobifoneInvoiceData(lid, invoiceData)))
                .build();
    }

    public MobifoneInvoiceData toMobifoneInvoiceData(String lid, InvoiceData invoiceData) {
        return MobifoneInvoiceData.builder()
                .cctbaoId(invoiceData.getProviderSerialId())
                .nlap(DATE_FORMATTER.format(invoiceData.getInvoiceDate()))
                .dvtte(invoiceData.getCurrencyCode() != null ? invoiceData.getCurrencyCode() : "VND")
                .tgia(invoiceData.getExchangeRate() != null ? invoiceData.getExchangeRate() : BigDecimal.ONE)
                .tthdon(mapInvoiceType(lid, invoiceData.getInvoiceTypeId()))
                .htttoan(truncate(mapPaymentMethodName(lid, invoiceData.getPayMethodId()), 50))
                .mnmua(truncate(invoiceData.getPartnerInvoiceStringId(), 30))
                .mst(StringUtils.hasText(invoiceData.getBuyerTaxCode()) ? truncate(invoiceData.getBuyerTaxCode(), 20) : null)
                .tnmua(truncate(invoiceData.getBuyerName(), 200))
                .ten(truncate(invoiceData.getBuyerUnitName(), 200))
                .dchi(truncate(invoiceData.getBuyerAddress(), 255))
                .email(truncate(invoiceData.getReceiverEmail(), 100))
                .sdtnmua(truncate(invoiceData.getReceiverMobile(), 20))
                .stknmua(truncate(invoiceData.getBuyerBankAccount(), 50))
                .tgtcthue(n(invoiceData.getGrossAmount()))
                .tgtthue(n(invoiceData.getTaxAmount()))
                .tgtttbso(n(invoiceData.getTotalAmount()))
                .tgtttbsoLast(n(invoiceData.getTotalAmount()))
                .sdhang(truncate(
                        invoiceData.getBillCode() != null ? invoiceData.getBillCode()
                                : (invoiceData.getPartnerInvoiceId() != null ? invoiceData.getPartnerInvoiceId() : ""),
                        30))
                .tthdon(MobifoneInvoiceStatus.ORIGINAL)
                .details(List.of(toMobifoneDetailWrapper(lid, invoiceData.getDetails())))
                .build();
    }

    public InvoiceResult toInvoiceResult(MobifoneInvoiceResponse response) {
        if (response == null) {
            return InvoiceResult.error("Không nhận được phản hồi từ MobiFone");
        }

        if (!response.isSuccess()) {
        String errorDetail = response.getErrorMessage();
        return InvoiceResult.builder()
                .success(false)
                .message(errorDetail != null 
                    ? "Lỗi từ MobiFone: " + errorDetail 
                    : "Lỗi từ MobiFone")
                .object(response)
                .build();
    }

        InvoiceResult.InvoiceResultBuilder builder = InvoiceResult.builder()
                .success(true)
                .message("Tạo hóa đơn thành công")
                .object(response);

        if (response.getData() != null) {
            var data = response.getData();

            builder.invoiceId(data.getHdonId() != null ? data.getHdonId() : data.getId())
                    .invoiceForm(extractInvoiceForm(data.getKhieu()))
                    .invoiceSerial(extractInvoiceSerial(data.getKhieu()))
                    .invoiceNo(data.getShdon())
                    .invoiceReferenceCode(data.getSbmat())
                    .taxAuthorityCode(data.getMccqthue())
                    .statusMessage(data.getTthai());
        }

        if (response.getLstHdonIdSuccess() != null && !response.getLstHdonIdSuccess().isEmpty()) {
            builder.invoiceId(response.getLstHdonIdSuccess().get(0));
        }
        if (response.getTthai() != null) {
            builder.statusMessage(response.getTthai());
        }

        return builder.build();
    }

    public InvoiceResult toInvoiceResultFromList(List<MobifoneInvoiceResponse> responses) {
        if (responses == null || responses.isEmpty()) {
            return InvoiceResult.error("Không nhận được phản hồi từ MobiFone");
        }
        return toInvoiceResult(responses.get(0));
    }

    private MobifoneInvoiceDetailWrapper toMobifoneDetailWrapper(String lid, List<InvoiceDetailData> details) {
        List<MobifoneInvoiceDetail> mobifoneDetails = new ArrayList<>();

        if (details != null) {
            for (int i = 0; i < details.size(); i++) {
                mobifoneDetails.add(toMobifoneInvoiceDetail(lid, details.get(i), i + 1));
            }
        }

        return MobifoneInvoiceDetailWrapper.builder()
                .data(mobifoneDetails)
                .build();
    }

    private MobifoneInvoiceDetail toMobifoneInvoiceDetail(String lid, InvoiceDetailData detail, int lineNo) {
        return MobifoneInvoiceDetail.builder()
                .stt(lineNo)
                .ma(truncate(detail.getItemCode(), 30))
                .ten(truncate(detail.getItemName(), 255))
                .dvtinh(truncate(detail.getUnitName(), 20))
                .sluong(n(detail.getQuantity()))
                .dgia(n(detail.getPrice()))
                .thtien(n(detail.getAmount()))
                .tlckhau(n(detail.getDiscountRate()))
                .stckhau(n(detail.getDiscountAmount()))
                .tsuat(mapTaxRate(lid, detail.getTaxRateId(), detail.getTaxRate()))
                .tthue(n(detail.getTaxAmount()))
                .tgtien(n(detail.getAmount()).add(n(detail.getTaxAmount())))
                .kmai(mapItemTypeCode(lid, detail.getItemTypeId()))
                .build();
    }

    private BigDecimal n(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String truncate(String value, int maxLength) {
        if (value == null)
            return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
    private String extractInvoiceForm(String khieu) {
        return (khieu != null && khieu.length() > 1) ? khieu.substring(0, 1) : null;
    }

    private String extractInvoiceSerial(String khieu) {
        return (khieu != null && khieu.length() > 1) ? khieu.substring(1) : khieu;
    }
}
