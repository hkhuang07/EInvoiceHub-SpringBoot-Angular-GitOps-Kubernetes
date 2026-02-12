package vn.softz.app.einvoicehub.provider.mobifone.mapper;

import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.provider.mobifone.constant.MobifoneInvoiceStatus;
import vn.softz.app.einvoicehub.provider.mobifone.model.*;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceDetailData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.core.common.Common;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MobifoneDataMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());
    
    private final EinvStoreProviderRepository storeProviderRepository;
    
    public MobifoneDataMapper(EinvStoreProviderRepository storeProviderRepository) {
        this.storeProviderRepository = storeProviderRepository;
    }

    private EinvStoreProviderEntity getConfig() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        return storeProviderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình HĐĐT cho cửa hàng này"));
    }
    
    public MobifoneInvoiceRequest toMobifoneInvoiceRequest(InvoiceData invoiceData) {
        return MobifoneInvoiceRequest.builder()
                .editmode(MobifoneInvoiceStatus.EDIT_MODE_CREATE)
                .taxCode(getConfig().getTaxCode())
                .data(List.of(toMobifoneInvoiceData(invoiceData)))
                .build();
    }
    
    public MobifoneInvoiceData toMobifoneInvoiceData(InvoiceData invoiceData) {
        var config = getConfig();
        
        return MobifoneInvoiceData.builder()
                .cctbaoId(invoiceData.getProviderSerialId()) 
                .nlap(formatDate(invoiceData.getInvoiceDate()))
                .dvtte(invoiceData.getCurrencyCode() != null ? invoiceData.getCurrencyCode() : "VND")
                .tgia(invoiceData.getExchangeRate() != null ? invoiceData.getExchangeRate() : BigDecimal.ONE)
                .htttoan(truncate(getPaymentMethodName(invoiceData.getPayMethodId()), 50))
                .mnmua(truncate(invoiceData.getPartnerInvoiceStringId(), 30))
                .mst(hasText(invoiceData.getBuyerTaxCode()) ? truncate(invoiceData.getBuyerTaxCode(), 20) : null)
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
                        invoiceData.getBillCode() != null ? invoiceData.getBillCode() : 
                        (invoiceData.getPartnerInvoiceId() != null ? invoiceData.getPartnerInvoiceId() : ""), 
                        30))
                .tthdon(MobifoneInvoiceStatus.ORIGINAL)
                .details(List.of(toMobifoneDetailWrapper(invoiceData.getDetails())))
                .build();
    }
    
    public InvoiceResult toInvoiceResult(MobifoneInvoiceResponse response) {
        if (response == null) {
            return InvoiceResult.error("Không nhận được phản hồi từ MobiFone");
        }
        
        if (!response.isSuccess()) {
            return InvoiceResult.builder()
                    .success(false)
                    .message(response.getMessage() != null ? response.getMessage() : "Lỗi từ MobiFone")
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
    
    private MobifoneInvoiceDetailWrapper toMobifoneDetailWrapper(List<InvoiceDetailData> details) {
        List<MobifoneInvoiceDetail> mobifoneDetails = new ArrayList<>();
        
        if (details != null) {
            for (int i = 0; i < details.size(); i++) {
                mobifoneDetails.add(toMobifoneInvoiceDetail(details.get(i), i + 1));
            }
        }
        
        return MobifoneInvoiceDetailWrapper.builder()
                .data(mobifoneDetails)
                .build();
    }
    
    private MobifoneInvoiceDetail toMobifoneInvoiceDetail(InvoiceDetailData detail, int lineNo) {
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
                .tsuat(getTaxRateString(detail.getTaxRate()))
                .tthue(n(detail.getTaxAmount()))
                .tgtien(n(detail.getAmount()).add(n(detail.getTaxAmount())))
                .kmai(Boolean.TRUE.equals(detail.getIsDiscount()) 
                        ? MobifoneInvoiceStatus.ITEM_PROMOTION 
                        : MobifoneInvoiceStatus.ITEM_GOODS_SERVICE)
                .build();
    }
    
    private String formatDate(Instant instant) {
        return DATE_FORMATTER.format(instant != null ? instant : Instant.now());
    }
    
    private String getPaymentMethodName(Integer payMethodId) {
        if (payMethodId == null) return "Tiền mặt/Chuyển khoản";
        return switch (payMethodId) {
            case 1 -> "Tiền mặt";
            case 2 -> "Chuyển khoản";
            default -> "Tiền mặt/Chuyển khoản";
        };
    }
    
    private String getTaxRateString(BigDecimal taxRate) {
        if (taxRate == null) return "10";
        return String.valueOf(taxRate.intValue());
    }
    
    private BigDecimal n(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
    
    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
    
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String extractInvoiceForm(String khieu) {
        return (khieu != null && khieu.length() > 1) ? khieu.substring(0, 1) : null;
    }

    private String extractInvoiceSerial(String khieu) {
        return (khieu != null && khieu.length() > 1) ? khieu.substring(1) : khieu;
    }
}
