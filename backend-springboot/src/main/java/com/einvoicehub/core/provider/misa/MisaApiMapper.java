package com.einvoicehub.core.provider.misa;

import com.einvoicehub.core.domain.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MisaApiMapper {

    public MisaAdapter.MisaInvoicePayload toMisaPayload(InvoiceRequest request) {
        List<MisaAdapter.MisaInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToMisaDetail)
                .toList();

        MisaAdapter.MisaInvoicePayload payload = MisaAdapter.MisaInvoicePayload.builder()
                .refId(UUID.randomUUID().toString())
                .invSeries(getInvSeries(request))
                .invDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .currencyCode(StringUtils.hasText(getCurrency(request)) ? getCurrency(request) : "VND")
                .exchangeRate(BigDecimal.ONE)
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
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        BigDecimal totalVatAmount = BigDecimal.ZERO;

        for (MisaAdapter.MisaInvoiceDetail detail : payload.getOriginalInvoiceDetail()) {
            totalSaleAmount = totalSaleAmount.add(detail.getAmountOC() != null ? detail.getAmountOC() : BigDecimal.ZERO);
            totalVatAmount = totalVatAmount.add(detail.getVatAmountOC() != null ? detail.getVatAmountOC() : BigDecimal.ZERO);
        }

        BigDecimal totalAmount = totalSaleAmount.add(totalVatAmount);

        payload.setTotalSaleAmountOC(totalSaleAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalSaleAmount(totalSaleAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountOC(totalAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalVATAmountOC(totalVatAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountInWords(convertNumberToWords(totalAmount));
    }

    private MisaAdapter.MisaInvoiceDetail convertToMisaDetail(InvoiceRequest.InvoiceItem item) {
        BigDecimal qty = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ONE;
        BigDecimal price = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal amount = qty.multiply(price).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxRate = item.getTaxRate() != null ? item.getTaxRate() : BigDecimal.ZERO;
        BigDecimal vatAmount = amount.multiply(taxRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return MisaAdapter.MisaInvoiceDetail.builder()
                .itemType(1).sortOrder(1).lineNumber(1)
                .itemName(item.getItemName())
                .unitName(item.getUnitName() != null ? item.getUnitName() : "")
                .quantity(qty.setScale(2, RoundingMode.HALF_UP))
                .unitPrice(price.setScale(2, RoundingMode.HALF_UP))
                .amountOC(amount).amount(amount)
                .vatAmountOC(vatAmount).vatAmount(vatAmount)
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

    /* Vietnamese Number-to-Words for invoice legality */
    public String convertNumberToWords(BigDecimal amount) {
        try {
            long wholePart = amount.longValue();
            if (wholePart == 0) return "Không đồng.";
            return convertWholeNumberToWords(wholePart) + " đồng.";
        } catch (Exception e) { return "Invalid amount"; }
    }

    private String convertWholeNumberToWords(long number) {
        String[] units = {"", "nghìn", "triệu", "tỷ"};
        String result = "";
        int idx = 0;
        while (number > 0) {
            long group = number % 1000;
            if (group > 0) result = convertGroupToWords((int) group) + " " + units[idx] + " " + result;
            number /= 1000;
            idx++;
        }
        return result.trim();
    }

    private String convertGroupToWords(int n) {
        String[] digits = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
        int h = n / 100, r = n % 100;
        String res = (h > 0) ? digits[h] + " trăm " : "";
        if (r > 0) {
            if (r < 10) res += "lẻ " + digits[r];
            else if (r < 20) res += "mười " + (r % 10 == 5 ? "lăm" : digits[r % 10]);
            else res += tens[r / 10] + (r % 10 == 5 ? " lăm" : (r % 10 > 0 ? " " + digits[r % 10] : ""));
        }
        return res.trim();
    }

    public InvoiceResponse toHubResponse(MisaAdapter.MisaApiResponse misaResponse, String requestId) {
        if (misaResponse == null) return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage("Provider timeout").build();

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

    public InvoiceStatus mapMisaStatus(int status) {
        return switch (status) {
            case 1 -> InvoiceStatus.SUCCESS;
            case 2 -> InvoiceStatus.CANCELLED;
            case 3, 7 -> InvoiceStatus.REPLACED;
            default -> InvoiceStatus.FAILED;
        };
    }

    private String getInvSeries(InvoiceRequest r) { return (r.getExtraConfig() != null) ? (String) r.getExtraConfig().getOrDefault("InvSeries", "") : ""; }
    private String getCurrency(InvoiceRequest r) { return (r.getSummary() != null) ? r.getSummary().getCurrencyCode() : "VND"; }
}