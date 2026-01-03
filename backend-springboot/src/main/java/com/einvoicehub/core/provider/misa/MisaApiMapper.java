package com.einvoicehub.core.provider.misa;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode; // Đã fix lỗi import
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MisaApiMapper {

    private final ObjectMapper objectMapper;

    /**
     * Khôi phục 100% logic buildMisaPayload từ file gốc của Huy
     */
    public MisaAdapter.MisaInvoicePayload toMisaPayload(InvoiceRequest request) {
        List<MisaAdapter.MisaInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToMisaDetail)
                .collect(Collectors.toList());

        MisaAdapter.MisaInvoicePayload payload = MisaAdapter.MisaInvoicePayload.builder()
                .refId(UUID.randomUUID().toString())
                .invSeries(getInvSeries(request))
                .invDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .currencyCode(request.getSummary() != null &&
                        StringUtils.hasText(request.getSummary().getCurrencyCode()) ?
                        request.getSummary().getCurrencyCode() : "VND")
                .exchangeRate(BigDecimal.valueOf(1.0)) // Fix lỗi double to BigDecimal
                .paymentMethodName("TM/CK")
                .isInvoiceSummary(false)
                .buyerLegalName(request.getBuyer() != null ? request.getBuyer().getName() : "")
                .buyerTaxCode(request.getBuyer() != null ? request.getBuyer().getTaxCode() : "")
                .buyerAddress(request.getBuyer() != null ? request.getBuyer().getAddress() : "")
                .buyerFullName(request.getBuyer() != null ? request.getBuyer().getName() : "")
                .buyerPhoneNumber(request.getBuyer() != null ? request.getBuyer().getPhone() : "")
                .buyerEmail(request.getBuyer() != null ? request.getBuyer().getEmail() : "")
                .originalInvoiceDetail(details)
                .taxRateInfo(buildTaxRateInfo(details))
                .build();

        calculateTotals(payload);
        return payload;
    }

    private void calculateTotals(MisaAdapter.MisaInvoicePayload payload) {
        BigDecimal totalSaleAmountOC = BigDecimal.ZERO;
        BigDecimal totalDiscountAmountOC = BigDecimal.ZERO;
        BigDecimal totalVATAmountOC = BigDecimal.ZERO;

        for (MisaAdapter.MisaInvoiceDetail detail : payload.getOriginalInvoiceDetail()) {
            totalSaleAmountOC = totalSaleAmountOC.add(detail.getAmountOC() != null ? detail.getAmountOC() : BigDecimal.ZERO);
            totalDiscountAmountOC = totalDiscountAmountOC.add(detail.getDiscountAmountOC() != null ? detail.getDiscountAmountOC() : BigDecimal.ZERO);
            totalVATAmountOC = totalVATAmountOC.add(detail.getVatAmountOC() != null ? detail.getVatAmountOC() : BigDecimal.ZERO);
        }

        BigDecimal totalAmountWithoutVATOC = totalSaleAmountOC.subtract(totalDiscountAmountOC);
        BigDecimal totalAmountOC = totalAmountWithoutVATOC.add(totalVATAmountOC);

        // Fix RoundingMode: Sử dụng Half_UP theo chuẩn kế toán
        payload.setTotalSaleAmountOC(totalSaleAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalSaleAmount(totalSaleAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountOC(totalAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmount(totalAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalVATAmountOC(totalVATAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountInWords(convertNumberToWords(totalAmountOC));
    }

    private MisaAdapter.MisaInvoiceDetail convertToMisaDetail(InvoiceRequest.InvoiceItem item) {
        BigDecimal quantity = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ONE;
        BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal amountOC = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxRate = item.getTaxRate() != null ? item.getTaxRate() : BigDecimal.ZERO;
        BigDecimal vatAmountOC = amountOC.multiply(taxRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return MisaAdapter.MisaInvoiceDetail.builder()
                .itemType(1).sortOrder(1).lineNumber(1)
                .itemName(item.getItemName())
                .unitName(item.getUnitName() != null ? item.getUnitName() : "")
                .quantity(quantity.setScale(2, RoundingMode.HALF_UP))
                .unitPrice(unitPrice.setScale(2, RoundingMode.HALF_UP))
                .amountOC(amountOC).amount(amountOC)
                .vatAmountOC(vatAmountOC).vatAmount(vatAmountOC)
                .vatRateName(taxRate.stripTrailingZeros().toPlainString() + "%")
                .discountAmountOC(BigDecimal.ZERO).discountAmount(BigDecimal.ZERO)
                .build();
    }

    private List<MisaAdapter.MisaTaxRateInfo> buildTaxRateInfo(List<MisaAdapter.MisaInvoiceDetail> details) {
        Map<String, MisaAdapter.MisaTaxRateInfo> taxMap = new LinkedHashMap<>();
        for (MisaAdapter.MisaInvoiceDetail detail : details) {
            taxMap.computeIfAbsent(detail.getVatRateName(), k -> MisaAdapter.MisaTaxRateInfo.builder()
                    .vatRateName(k).amountWithoutVATOC(BigDecimal.ZERO).vatAmountOC(BigDecimal.ZERO).build());
            MisaAdapter.MisaTaxRateInfo info = taxMap.get(detail.getVatRateName());
            info.setAmountWithoutVATOC(info.getAmountWithoutVATOC().add(detail.getAmountOC()));
            info.setVatAmountOC(info.getVatAmountOC().add(detail.getVatAmountOC()));
        }
        return new ArrayList<>(taxMap.values());
    }

    /**
     * Khôi phục logic chuyển số thành chữ của Huy
     */
    public String convertNumberToWords(BigDecimal amount) {
        try {
            long wholePart = amount.longValue();
            if (wholePart == 0) return "Không đồng.";
            return convertWholeNumberToWords(wholePart) + " đồng.";
        } catch (Exception e) { return ""; }
    }

    private String convertWholeNumberToWords(long number) {
        String[] units = {"", "nghìn", "triệu", "tỷ"};
        String result = "";
        int unitIndex = 0;
        while (number > 0) {
            long group = number % 1000;
            if (group > 0) result = convertGroupToWords((int) group) + " " + units[unitIndex] + " " + result;
            number /= 1000;
            unitIndex++;
        }
        return result.trim();
    }

    private String convertGroupToWords(int number) {
        String[] digits = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
        int hundreds = number / 100;
        int remainder = number % 100;
        String res = (hundreds > 0) ? digits[hundreds] + " trăm " : "";
        if (remainder > 0) {
            if (remainder < 10) res += digits[remainder];
            else if (remainder < 20) res += "mười " + digits[remainder - 10];
            else res += tens[remainder / 10] + ((remainder % 10 > 0) ? " " + digits[remainder % 10] : "");
        }
        return res.trim();
    }

    public InvoiceResponse toHubResponse(MisaAdapter.MisaApiResponse misaResponse, String requestId) {
        if (misaResponse == null) return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage("No response").build();
        boolean isSuccess = Boolean.TRUE.equals(misaResponse.getSuccess());
        InvoiceResponse.InvoiceResponseBuilder builder = InvoiceResponse.builder()
                .clientRequestId(requestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(misaResponse.getErrorCode())
                .errorMessage(misaResponse.getDescriptionErrorCode())
                .responseTime(LocalDateTime.now())
                .rawResponse(misaResponse);

        if (isSuccess && misaResponse.getPublishInvoiceResult() != null && !misaResponse.getPublishInvoiceResult().isEmpty()) {
            Map<String, Object> res = misaResponse.getPublishInvoiceResult().get(0);
            builder.transactionCode((String) res.get("TransactionID"))
                    .invoiceNumber((String) res.get("InvNo"))
                    .symbolCode((String) res.get("InvSeries"))
                    .lookupCode((String) res.get("TransactionID"));
        }
        return builder.build();
    }

    public InvoiceStatus mapMisaStatus(int eInvoiceStatus) {
        switch (eInvoiceStatus) {
            case 1: return InvoiceStatus.SUCCESS;
            case 2: return InvoiceStatus.CANCELLED;
            case 3: case 7: return InvoiceStatus.REPLACED;
            default: return InvoiceStatus.FAILED;
        }
    }

    private String getInvSeries(InvoiceRequest request) {
        return (request.getExtraConfig() != null) ? (String) request.getExtraConfig().getOrDefault("InvSeries", "") : "";
    }
}