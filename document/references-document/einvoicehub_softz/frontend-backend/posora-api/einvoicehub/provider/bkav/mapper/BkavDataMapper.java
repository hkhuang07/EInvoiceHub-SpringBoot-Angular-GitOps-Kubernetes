package vn.softz.app.einvoicehub.provider.bkav.mapper;

import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavInvoice;
import vn.softz.app.einvoicehub.provider.model.InvoiceAttachmentData;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceDetailData;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavDataMapper {
    
    private static final DateTimeFormatter BKAV_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .withZone(ZoneId.systemDefault());

    private final EinvoiceMappingService mappingService;

    private Integer mapToProviderInt(String lid, MappingType type, Integer internalId, Integer defaultValue) {
        Integer originalValue = internalId;
        if (internalId == null) {
            internalId = defaultValue;
            log.debug("[MAPPING] {} - Input is null, using default: {}", type, defaultValue);
        }

        log.debug("[MAPPING] {} - Input: {} (original: {}), Default: {}", 
            type, internalId, originalValue, defaultValue);

        String mappedStr = mappingService.getProviderCode(
            lid, 
            "BKAV", 
            type, 
            String.valueOf(internalId)
        );

        try {
            Integer result = Integer.parseInt(mappedStr);
            log.info("[MAPPING] {} - MAPPED: {} → {} (provider code)", type, internalId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[MAPPING] {} - Parse failed for '{}', fallback to: {}", type, mappedStr, internalId);
            return internalId;
        }
    }

    private String[] mapTaxType(String lid, String taxTypeId) {
        String defaultTaxTypeId = "3";
        String defaultTaxRate = "10";
        
        if (taxTypeId == null || taxTypeId.isEmpty()) {
            log.debug("[TAX_MAPPING] taxTypeId is null, using default");
            return new String[]{defaultTaxTypeId, defaultTaxRate};
        }

        String mappedStr = mappingService.getProviderCode(lid, "BKAV", MappingType.TAX_TYPE, taxTypeId);
        
        if (mappedStr == null || !mappedStr.contains(",")) {
            log.warn("[TAX_MAPPING] Invalid mapping result: '{}', using default", mappedStr);
            return new String[]{defaultTaxTypeId, defaultTaxRate};
        }

        String[] parts = mappedStr.split(",");
        log.info("[TAX_MAPPING] {} → TaxTypeId={}, TaxRate={}", taxTypeId, parts[0], parts[1]);
        return parts;
    }
    
    public BkavInvoice.RequestData toBkavRequestData(String lid, InvoiceData invoiceData) {
        if (invoiceData == null) {
            return null;
        }
        
        Long partnerId = parsePartnerInvoiceId(invoiceData.getPartnerInvoiceId());
        if (partnerId == null || partnerId <= 0) {
            partnerId = System.currentTimeMillis();
        }
        
        String partnerStringId = invoiceData.getPartnerInvoiceStringId();
        if (partnerStringId == null) {
            partnerStringId = "";
        }
        
        return BkavInvoice.RequestData.builder()
                .invoice(toBkavHeader(lid, invoiceData))
                .listInvoiceDetailsWS(toBkavDetailList(lid, invoiceData.getDetails()))
                .listInvoiceAttachFileWS(toBkavAttachmentList(invoiceData.getAttachments()))
                .partnerInvoiceID(partnerId)
                .partnerInvoiceStringID(partnerStringId)
                .build();
    }

    private BkavInvoice.Header toBkavHeader(String lid,InvoiceData data) {
        return BkavInvoice.Header.builder()
                .invoiceTypeID(mapToProviderInt(lid, MappingType.INVOICE_TYPE, data.getInvoiceTypeId(), 1))
                .invoiceDate(formatBkavDate(data.getInvoiceDate()))
                .buyerName(data.getBuyerName() != null ? data.getBuyerName() : 
                        (data.getBuyerUnitName() != null ? data.getBuyerUnitName() : ""))
                .buyerTaxCode(data.getBuyerTaxCode() != null ? data.getBuyerTaxCode() : "")
                .buyerUnitName(data.getBuyerUnitName() != null ? data.getBuyerUnitName() : "")
                .buyerAddress(data.getBuyerAddress() != null ? data.getBuyerAddress() : "")
                .buyerBankAccount(data.getBuyerBankAccount() != null ? data.getBuyerBankAccount() : "")
                .payMethodID(mapToProviderInt(lid, MappingType.PAYMENT_METHOD, data.getPayMethodId(), 3))
                .receiveTypeID(data.getReceiveTypeId() != null ? data.getReceiveTypeId() : 3)
                .receiverEmail(data.getReceiverEmail() != null ? data.getReceiverEmail() : "")
                .receiverMobile(data.getReceiverMobile() != null ? data.getReceiverMobile() : "")
                .receiverAddress(data.getReceiverAddress() != null ? data.getReceiverAddress() : "")
                .receiverName(data.getReceiverName() != null ? data.getReceiverName() : "")
                .note(data.getNotes() != null ? data.getNotes() : "")
                .userDefine(data.getUserDefine() != null ? data.getUserDefine() : "")
                .billCode(data.getBillCode() != null ? data.getBillCode() : "")
                .currencyID(data.getCurrencyCode() != null ? data.getCurrencyCode() : "VND")
                .exchangeRate(data.getExchangeRate() != null ? data.getExchangeRate().doubleValue() : 1.0)
                .invoiceForm(data.getInvoiceForm() != null ? data.getInvoiceForm() : "1")
                .invoiceSerial(data.getInvoiceSerial() != null ? data.getInvoiceSerial() : "")
                .invoiceNo(data.getInvoiceNo() != null ? data.getInvoiceNo() : 0)
                .build();
    }

    private List<BkavInvoice.Detail> toBkavDetailList(String lid, List<InvoiceDetailData> details) {
        if (details == null || details.isEmpty()) {
            return new ArrayList<>();
        }
        return details.stream()
                .map(detail -> toBkavDetail(lid, detail))
                .collect(Collectors.toList());
    }

    private BkavInvoice.Detail toBkavDetail(String lid,InvoiceDetailData detail) {
        log.debug("[BKAV_DETAIL] Item='{}', ItemTypeId={}", 
            detail.getItemName(), detail.getItemTypeId());
        
        // Map tax type: input taxTypeId (String) → output [providerTaxTypeId, providerTaxRate]
        String[] taxMapping = mapTaxType(lid, detail.getTaxRateId());
        Integer providerTaxTypeId = Integer.parseInt(taxMapping[0]);
        java.math.BigDecimal providerTaxRate = new java.math.BigDecimal(taxMapping[1]);
        
        return BkavInvoice.Detail.builder()
                .itemTypeID(mapToProviderInt(lid, MappingType.ITEM_TYPE, detail.getItemTypeId(), 0))
                .itemCode(detail.getItemCode() != null ? detail.getItemCode() : "")
                .itemName(detail.getItemName())
                .unitName(detail.getUnitName())
                .qty(detail.getQuantity() != null ? detail.getQuantity() : java.math.BigDecimal.ZERO)
                .price(detail.getPrice() != null ? detail.getPrice() : java.math.BigDecimal.ZERO)
                .amount(detail.getAmount() != null ? detail.getAmount() : java.math.BigDecimal.ZERO)
                .taxRateID(providerTaxTypeId)
                .taxRate(providerTaxRate)
                .taxAmount(detail.getTaxAmount() != null ? detail.getTaxAmount() : java.math.BigDecimal.ZERO)
                .discountRate(detail.getDiscountRate() != null ? detail.getDiscountRate() : java.math.BigDecimal.ZERO)
                .discountAmount(detail.getDiscountAmount() != null ? detail.getDiscountAmount() : java.math.BigDecimal.ZERO)
                .isDiscount(detail.getIsDiscount() != null ? detail.getIsDiscount() : false)
                .userDefineDetails(detail.getUserDefineDetails() != null ? detail.getUserDefineDetails() : "")
                .isIncrease(detail.getIsIncrease() != null ? detail.getIsIncrease() : false)
                .build();
    }

    private List<BkavInvoice.Attachment> toBkavAttachmentList(List<InvoiceAttachmentData> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }
        return attachments.stream()
                .map(this::toBkavAttachment)
                .collect(Collectors.toList());
    }

    private BkavInvoice.Attachment toBkavAttachment(InvoiceAttachmentData attachment) {
        return BkavInvoice.Attachment.builder()
                .attachName(attachment.getFileName())
                .attachData(attachment.getFileContent())
                .attachDescription("")
                .build();
    }

    private Long parsePartnerInvoiceId(String partnerInvoiceId) {
        if (partnerInvoiceId == null || partnerInvoiceId.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(partnerInvoiceId);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    private String formatBkavDate(Instant instant) {
        if (instant == null) {
            instant = Instant.now();
        }
        return BKAV_DATE_FORMATTER.format(instant);
    }
}
