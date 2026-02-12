package vn.softz.app.einvoicehub.provider.mobifone;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.provider.EInvoiceProvider;
import vn.softz.app.einvoicehub.provider.mobifone.constant.MobifoneApiEndpoints;
import vn.softz.app.einvoicehub.provider.mobifone.constant.MobifoneInvoiceStatus;
import vn.softz.app.einvoicehub.provider.mobifone.mapper.MobifoneDataMapper;
import vn.softz.app.einvoicehub.provider.mobifone.model.*;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.core.common.Common;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MobifoneEInvoiceProvider implements EInvoiceProvider {
    
    private final MobifoneHttpClient httpClient;
    private final MobifoneDataMapper dataMapper;
    private final EinvStoreProviderRepository storeProviderRepository;

    private EinvStoreProviderEntity getConfig() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        return storeProviderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình HĐĐT cho cửa hàng này"));
    }
    
    @Override
    public String getProviderType() {
        return "MOBI";
    }
    
    @Override
    public InvoiceResult createInvoice(int commandType, InvoiceData invoiceData) {
        try {
            log.info("MobiFone createInvoice - partnerInvoiceId: {}", invoiceData.getPartnerInvoiceId());
            
            MobifoneInvoiceRequest request = dataMapper.toMobifoneInvoiceRequest(invoiceData);
            
            List<MobifoneInvoiceResponse> responses = httpClient.postForList(
                    MobifoneApiEndpoints.SAVE_INVOICE,
                    request,
                    new TypeReference<>() {}
            );
            
            InvoiceResult result = dataMapper.toInvoiceResultFromList(responses);
            
            if (result.isSuccess()) {
                log.info("MobiFone createInvoice success - hdon_id: {}, khieu: {}, shdon: {}", 
                        result.getInvoiceId(), result.getInvoiceSerial(), result.getInvoiceNo());
            } else {
                log.error("MobiFone createInvoice failed: {}", result.getMessage());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("MobiFone createInvoice error", e);
            return InvoiceResult.error("Lỗi tạo hóa đơn MobiFone: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult signInvoiceByHsm(String invoiceGuid) {
        try {
            log.info("MobiFone signInvoiceByHsm - invoiceId: {}", invoiceGuid);
            
            var config = getConfig();
            
            Map<String, Object> signRequest = Map.of(
                    "data", List.of(Map.of(
                            "branch_code", httpClient.getMaDvcs(),
                            "username", config.getPartnerUsr(),
                            "lsthdon_id", List.of(invoiceGuid),
                            "cer_serial", "",
                            "type_cmd", MobifoneInvoiceStatus.TYPE_CMD_WITH_CODE,
                            "is_api", "1"
                    ))
            );
            
            MobifoneInvoiceResponse response = httpClient.post(
                    MobifoneApiEndpoints.SIGN_INVOICE,
                    signRequest,
                    MobifoneInvoiceResponse.class
            );
            
            InvoiceResult result = dataMapper.toInvoiceResult(response);
            
            if (result.isSuccess()) {
                log.info("MobiFone signInvoiceByHsm success - status: {}", response.getTthai());
            } else {
                log.error("MobiFone signInvoiceByHsm failed: {}", result.getMessage());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("MobiFone signInvoiceByHsm error", e);
            return InvoiceResult.error("Lỗi ký hóa đơn MobiFone: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult getInvoiceData(String invoiceGuid) {
        try {
            log.info("MobiFone getInvoiceData - invoiceId: {}", invoiceGuid);
            
            Object response = httpClient.get(
                    MobifoneApiEndpoints.GET_INVOICE_BY_ID + "?id=" + invoiceGuid, 
                    Object.class
            );
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Lấy thông tin hóa đơn thành công")
                    .object(response)
                    .build();
            
        } catch (Exception e) {
            log.error("MobiFone getInvoiceData error", e);
            return InvoiceResult.error("Lỗi lấy thông tin hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public Object getInvoiceStatus(String invoiceGuid) {
        try {
            log.info("MobiFone getInvoiceStatus - invoiceId: {}", invoiceGuid);
            return httpClient.get(MobifoneApiEndpoints.GET_INVOICE_BY_ID + "?id=" + invoiceGuid, Object.class);
        } catch (Exception e) {
            log.error("MobiFone getInvoiceStatus error", e);
            throw new RuntimeException("Lỗi lấy trạng thái hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public String getInvoicePdf(String docId) {
        try {
            log.info("MobiFone getInvoicePdf - docId: {}", docId);
            
            String endpoint = MobifoneApiEndpoints.GET_INVOICE_PDF + "?id=" + docId + "&type=PDF&inchuyendoi=false";
            byte[] pdfBytes = httpClient.getBytes(endpoint);
            
            return Base64.getEncoder().encodeToString(pdfBytes);
            
        } catch (Exception e) {
            log.error("MobiFone getInvoicePdf error", e);
            throw new RuntimeException("Lỗi lấy PDF hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public String getInvoiceXml(String docId) {
        try {
            log.info("MobiFone getInvoiceXml - docId: {}", docId);
            return httpClient.getString(MobifoneApiEndpoints.GET_INVOICE_XML + "?id=" + docId);
        } catch (Exception e) {
            log.error("MobiFone getInvoiceXml error", e);
            throw new RuntimeException("Lỗi lấy XML hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult cancelInvoiceByGuid(String invoiceGuid, String reason) {
        try {
            log.info("MobiFone cancelInvoice - invoiceId: {}, reason: {}", invoiceGuid, reason);
            
            MobifoneCancelInvoiceRequest cancelRequest = MobifoneCancelInvoiceRequest.builder()
                    .editmode(MobifoneInvoiceStatus.EDIT_MODE_CREATE)
                    .mst(getConfig().getTaxCode())
                    .hdonId(invoiceGuid)
                    .tctbao(MobifoneInvoiceStatus.TBSS_CANCEL)
                    .ldo(reason)
                    .build();
            
            Object response = httpClient.post(MobifoneApiEndpoints.SAVE_ERROR_NOTIFICATION, cancelRequest, Object.class);
            
            log.info("MobiFone cancelInvoice response: {}", response);
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Hủy hóa đơn thành công")
                    .object(response)
                    .build();
            
        } catch (Exception e) {
            log.error("MobiFone cancelInvoice error", e);
            return InvoiceResult.error("Lỗi hủy hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult deleteInvoiceByGuid(String invoiceGuid) {
        try {
            log.info("MobiFone deleteInvoice - invoiceId: {}", invoiceGuid);
            
            Map<String, Object> deleteRequest = Map.of(
                    "editmode", MobifoneInvoiceStatus.EDIT_MODE_DELETE,
                    "data", List.of(Map.of("hdon_id", invoiceGuid))
            );
            
            Object response = httpClient.post(MobifoneApiEndpoints.DELETE_UNSIGNED_INVOICE, deleteRequest, Object.class);
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Xóa hóa đơn thành công")
                    .object(response)
                    .build();
            
        } catch (Exception e) {
            log.error("MobiFone deleteInvoice error", e);
            return InvoiceResult.error("Lỗi xóa hóa đơn: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult createReplacementInvoice(InvoiceData invoiceData) {
        try {
            log.info("MobiFone createReplacementInvoice - originalInvoiceId: {}", invoiceData.getOriginalInvoiceIdentify());
            
            MobifoneInvoiceRequest request = dataMapper.toMobifoneInvoiceRequest(invoiceData);
            
            if (!request.getData().isEmpty()) {
                MobifoneInvoiceData data = request.getData().get(0);
                data.setHdonIdOld(invoiceData.getOriginalInvoiceIdentify());
                data.setTthdon(MobifoneInvoiceStatus.REPLACEMENT);
                data.setLhdclquan(1);
            }
            
            List<MobifoneInvoiceResponse> responses = httpClient.postForList(
                    MobifoneApiEndpoints.SAVE_INVOICE,
                    request,
                    new TypeReference<>() {}
            );
            
            return dataMapper.toInvoiceResultFromList(responses);
            
        } catch (Exception e) {
            log.error("MobiFone createReplacementInvoice error", e);
            return InvoiceResult.error("Lỗi tạo hóa đơn thay thế: " + e.getMessage());
        }
    }
    
    @Override
    public InvoiceResult createAdjustmentInvoice(InvoiceData invoiceData) {
        try {
            log.info("MobiFone createAdjustmentInvoice - originalInvoiceId: {}", invoiceData.getOriginalInvoiceIdentify());
            
            MobifoneInvoiceRequest request = dataMapper.toMobifoneInvoiceRequest(invoiceData);
            
            if (!request.getData().isEmpty()) {
                MobifoneInvoiceData data = request.getData().get(0);
                data.setHdonIdOld(invoiceData.getOriginalInvoiceIdentify());
                data.setLhdclquan(1);
                
                Integer adjustType = invoiceData.getTypeCreateInvoice();
                data.setTthdon(adjustType != null ? switch (adjustType) {
                    case 3 -> MobifoneInvoiceStatus.ADJUSTMENT_INCREASE;
                    case 4 -> MobifoneInvoiceStatus.ADJUSTMENT_DECREASE;
                    case 2 -> MobifoneInvoiceStatus.ADJUSTMENT_INFO;
                    default -> MobifoneInvoiceStatus.ADJUSTMENT;
                } : MobifoneInvoiceStatus.ADJUSTMENT);
            }
            
            List<MobifoneInvoiceResponse> responses = httpClient.postForList(
                    MobifoneApiEndpoints.SAVE_INVOICE,
                    request,
                    new TypeReference<>() {}
            );
            
            return dataMapper.toInvoiceResultFromList(responses);
            
        } catch (Exception e) {
            log.error("MobiFone createAdjustmentInvoice error", e);
            return InvoiceResult.error("Lỗi tạo hóa đơn điều chỉnh: " + e.getMessage());
        }
    }
    
    @Override
    public Object lookupCompany(String taxCode) {
        log.warn("MobiFone lookupCompany not implemented - taxCode: {}", taxCode);
        return Map.of("success", false, "message", "Chức năng tra cứu doanh nghiệp không khả dụng với MobiFone");
    }
    
    @Override
    public String getInvoiceLink(String partnerInvoiceId) {
        throw new UnsupportedOperationException("MobiFone không hỗ trợ lấy link, sử dụng getInvoicePdf thay thế");
    }
    
    @Override
    public InvoiceResult updateInvoiceByGuid(String invoiceGuid, InvoiceData invoiceData) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    
    @Override
    public InvoiceResult updateInvoiceByPartnerId(String partnerInvoiceId, InvoiceData invoiceData) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    
    @Override
    public InvoiceResult cancelInvoiceByPartnerId(String partnerInvoiceId, String reason) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    
    @Override
    public InvoiceResult deleteInvoiceByPartnerId(String partnerInvoiceId) {
        throw new UnsupportedOperationException("Chưa implement");
    }
}
