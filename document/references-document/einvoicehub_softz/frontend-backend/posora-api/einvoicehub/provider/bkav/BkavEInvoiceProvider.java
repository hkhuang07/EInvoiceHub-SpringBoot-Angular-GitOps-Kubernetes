package vn.softz.app.einvoicehub.provider.bkav;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.provider.EInvoiceProvider;
import vn.softz.app.einvoicehub.provider.bkav.constant.BkavCommandType;
import vn.softz.app.einvoicehub.provider.bkav.mapper.BkavDataMapper;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavInvoice;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavResponse;
import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BkavEInvoiceProvider implements EInvoiceProvider {
    
    private final BkavSoapClient soapClient;
    private final BkavDataMapper dataMapper;
    private final EinvoiceMappingService mappingService;
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    
    @Override
    public String getProviderType() {
        return "BKAV";
    }
    
    @Override
    public InvoiceResult createInvoice(int commandType, InvoiceData invoiceData) {
        try {
            BkavInvoice.RequestData bkavData = dataMapper.toBkavRequestData(invoiceData.getLid(), invoiceData);
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(bkavData));
            log.debug("BKAV Request JSON: {}", jsonData);
            
            BkavResponse bkavResponse = soapClient.executeCommand(commandType, jsonData);
            
            if (!bkavResponse.isSuccess()) {
                String errorMsg = bkavResponse.getErrorMessage();
                return buildErrorResult(
                    errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi từ BKAV", 
                    bkavResponse.getStatus()
                );
            }
            
            BkavResponse.InvoiceResult invoiceResult = bkavResponse.asInvoiceResult();
            
            if (invoiceResult == null) {
                return InvoiceResult.error("BKAV không trả về kết quả");
            }
            
            if (!invoiceResult.isSuccess()) {
                String errorMsg = invoiceResult.getMessLog();
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi từ BKAV: Lỗi tạo hóa đơn",
                        invoiceResult.getStatus()
                );
            }
            
            return buildSuccessResultFromBkav(invoiceResult);
            
        } catch (Exception e) {
            log.error("Lỗi tạo hóa đơn BKAV: ", e);
            return buildErrorResult("Lỗi kết nối BKAV: " + e.getMessage(), 1);
        }
    }
    
    @Override
    public InvoiceResult createReplacementInvoice(InvoiceData invoiceData) {
        return createInvoice(BkavCommandType.CREATE_INVOICE_REPLACE, invoiceData);
    }
    
    @Override
    public InvoiceResult createAdjustmentInvoice(InvoiceData invoiceData) {
        return createInvoice(BkavCommandType.CREATE_INVOICE_ADJUST, invoiceData);
    }
    
    @Override
    public InvoiceResult signInvoiceByHsm(String invoiceGuid) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                BkavCommandType.SIGN_INVOICE_BY_HSM, invoiceGuid);
            
            if (!bkavResponse.isSuccess()) {
                String errorMsg = bkavResponse.getErrorMessage();
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi từ BKAV: Lỗi ký hóa đơn",
                        bkavResponse.getStatus()
                );
            }
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Ký hóa đơn bằng HSM thành công")
                    .object(bkavResponse.getObject())
                    .build();
            
        } catch (Exception e) {
            log.error("Lỗi ký hóa đơn BKAV: ", e);
            return buildErrorResult("Lỗi ký hóa đơn: " + e.getMessage(), 1);
        }
    }

    @Override
    public InvoiceResult getInvoiceData(String lid, String invoiceGuid) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                BkavCommandType.GET_INVOICE_DATA_WS, invoiceGuid);
            
            if (bkavResponse.getCode() != null && bkavResponse.getCode() != 0) {
                String errorMsg = bkavResponse.getErrorMessage();
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi từ BKAV: Lỗi lấy thông tin hóa đơn",
                        bkavResponse.getStatus()
                );
            }
            
            BkavResponse.InvoiceFullData fullData = bkavResponse.asInvoiceFullData();
            
            if (fullData == null) {
                return InvoiceResult.builder()
                        .success(true)
                        .message("Lấy thông tin hóa đơn thành công")
                        .object(bkavResponse.getObject())
                        .build();
            }
            Integer bkavInvoiceStatusId = fullData.getInvoiceStatusId();
            Integer bkavInvoiceTypeId = fullData.getInvoice() != null ? fullData.getInvoice().getInvoiceTypeID() : null;
            Integer bkavPayMethodId = fullData.getInvoice() != null ? fullData.getInvoice().getPaymentMethodId() : null;
            
            Integer hubStatusId = reverseMapInvoiceStatus(lid, bkavInvoiceStatusId);
            Integer hubTypeId = reverseMapInvoiceType(lid, bkavInvoiceTypeId);
            Integer hubPayMethodId = reverseMapPaymentMethod(lid, bkavPayMethodId);
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Lấy thông tin hóa đơn thành công")
                    .invoiceDate(fullData.getInvoiceDate())
                    .signedDate(fullData.getSignedDate())
                    .invoiceForm(fullData.getInvoice() != null ? fullData.getInvoice().getInvoiceForm() : null)
                    .invoiceSerial(fullData.getInvoice() != null ? fullData.getInvoice().getInvoiceSerial() : null)
                    .invoiceNo(fullData.getInvoice() != null && fullData.getInvoice().getInvoiceNo() != null 
                            ? String.valueOf(fullData.getInvoice().getInvoiceNo()) : null)
                    .invoiceReferenceCode(fullData.getInvoiceCode())
                    .taxAuthorityCode(fullData.getInvoice() != null ? fullData.getInvoice().getMaCuaCQT() : null)
                    .providerInvoiceId(fullData.getInvoice() != null && fullData.getInvoice().getInvoiceGUID() != null 
                            ? fullData.getInvoice().getInvoiceGUID().toString() : null)
                    .status(fullData.getInvoiceStatusId())
                    .invoiceStatusId(hubStatusId)
                    .invoiceTypeId(hubTypeId)
                    .paymentMethodId(hubPayMethodId)
                    .object(fullData)
                    .build();
            
        } catch (Exception e) {
            log.error("Lỗi lấy thông tin hóa đơn BKAV: ", e);
            return buildErrorResult("Lỗi lấy thông tin hóa đơn: " + e.getMessage(), 1);
        }
    }
    
    @Override
    public Object getInvoiceStatus(String invoiceGuid) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                BkavCommandType.GET_INVOICE_STATUS_WITH_TAX_CODE, 
                invoiceGuid
            );
            
            if (!bkavResponse.isSuccess()) {
                log.warn("Failed to get invoice status: {}", bkavResponse.getErrorMessage());
                return bkavResponse;
            }
            
            List<BkavResponse.InvoiceStatus> statusList = bkavResponse.asInvoiceStatusList();
            if (statusList != null && !statusList.isEmpty()) {
                return statusList.get(0);
            }
            
            return bkavResponse;
            
        } catch (Exception e) {
            log.error("Lỗi lấy trạng thái hóa đơn BKAV: ", e);
            throw new RuntimeException("Lỗi lấy trạng thái hóa đơn", e);
        }
    }
    
    @Override
    public String getInvoicePdf(String docId) {
        return getInvoiceFile(docId, BkavCommandType.GET_INVOICE_DATA_FILE_PDF);
    }
    
    @Override
    public String getInvoiceXml(String docId) {
        return getInvoiceFile(docId, BkavCommandType.GET_INVOICE_DATA_FILE_XML);
    }
    
    private String getInvoiceFile(String partnerInvoiceStringId, int commandType) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(commandType, partnerInvoiceStringId);
            
            if (bkavResponse.getCode() != null && bkavResponse.getCode() != 0) {
                throw new RuntimeException("BKAV error: " + bkavResponse.getErrorMessage());
            }
            
            BkavResponse.InvoiceFile fileResult = bkavResponse.asInvoiceFile();
            
            if (fileResult == null) {
                throw new RuntimeException("Không nhận được file từ BKAV");
            }
            
            return commandType == BkavCommandType.GET_INVOICE_DATA_FILE_PDF 
                    ? fileResult.getPdf() 
                    : fileResult.getXml();
            
        } catch (Exception e) {
            log.error("Error getting invoice file", e);
            throw new RuntimeException("Lỗi lấy file hóa đơn", e);
        }
    }
    
    @Override
    public String getInvoiceLink(String partnerInvoiceId) {
        try {
            BkavInvoice.RequestData data = BkavInvoice.RequestData.builder()
                    .partnerInvoiceID(Long.parseLong(partnerInvoiceId))
                    .build();
            
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(data));
            BkavResponse bkavResponse = soapClient.executeCommand(BkavCommandType.GET_INVOICE_LINK, jsonData);
            
            if (bkavResponse.getCode() != null && bkavResponse.getCode() != 0) {
                throw new RuntimeException("BKAV error: " + bkavResponse.getErrorMessage());
            }
            
            return bkavResponse.getObject() != null ? bkavResponse.getObject().toString() : null;
            
        } catch (Exception e) {
            log.error("Error getting invoice link", e);
            throw new RuntimeException("Lỗi lấy link hóa đơn", e);
        }
    }
    
    @Override
    public InvoiceResult cancelInvoiceByGuid(String invoiceGuid, String reason) {
        try {
            BkavInvoice.RequestData cancelData = BkavInvoice.RequestData.builder()
                    .invoice(BkavInvoice.Header.builder()
                            .invoiceGUID(UUID.fromString(invoiceGuid))
                            .build())
                    .build();
            
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(cancelData));
            BkavResponse bkavResponse = soapClient.executeCommand(BkavCommandType.CANCEL_INVOICE_BY_GUID, jsonData);
            
            if (bkavResponse.getCode() != null && bkavResponse.getCode() != 0) {
                return InvoiceResult.error("BKAV error: " + bkavResponse.getErrorMessage());
            }
            
            return InvoiceResult.builder()
                    .success(true)
                    .message("Hủy hóa đơn thành công")
                    .object(bkavResponse.getObject())
                    .build();
            
        } catch (Exception e) {
            log.error("Lỗi hủy hóa đơn BKAV: ", e);
            return InvoiceResult.error("Lỗi kết nối BKAV: " + e.getMessage());
        }
    }
    
    @Override
    public Object lookupCompany(String taxCode) {
        try {
            BkavResponse response = soapClient.executeCommand(BkavCommandType.GET_UNIT_INFO_BY_TAXCODE, taxCode);
            
            if (!response.isSuccess()) {
                log.warn("Failed to lookup company: {}", response.getErrorMessage());
            }
            
            return response;
            
        } catch (Exception e) {
            log.error("Error looking up company", e);
            BkavResponse errorResponse = new BkavResponse();
            errorResponse.setStatus(1);
            errorResponse.setMessage("Lỗi tra cứu doanh nghiệp: " + e.getMessage());
            return errorResponse;
        }
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
    public InvoiceResult deleteInvoiceByGuid(String invoiceGuid) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    
    @Override
    public InvoiceResult deleteInvoiceByPartnerId(String partnerInvoiceId) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    
    private Integer reverseMapInvoiceStatus(String lid, Integer providerStatusId) {
        if (providerStatusId == null) {
            log.debug("[BKAV_REVERSE_MAP] INVOICE_STATUS - Provider status is null");
            return null;
        }

        String internalId = mappingService.getInternalCode(
                lid,
                "BKAV",
                MappingType.INVOICE_STATUS,
                String.valueOf(providerStatusId));

        try {
            Integer result = Integer.parseInt(internalId);
            log.info("[BKAV_REVERSE_MAP] INVOICE_STATUS - Provider {} → Hub ID {}", providerStatusId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[BKAV_REVERSE_MAP] INVOICE_STATUS - Parse failed for '{}', fallback to provider value: {}", internalId, providerStatusId);
            return providerStatusId;  // Fallback to provider value if mapping fails
        }
    }

    private Integer reverseMapInvoiceType(String lid, Integer providerTypeId) {
        if (providerTypeId == null) {
            log.debug("[BKAV_REVERSE_MAP] INVOICE_TYPE - Provider type is null");
            return null;
        }

        String internalId = mappingService.getInternalCode(
                lid,
                "BKAV",
                MappingType.INVOICE_TYPE,
                String.valueOf(providerTypeId));

        try {
            Integer result = Integer.parseInt(internalId);
            log.info("[BKAV_REVERSE_MAP] INVOICE_TYPE - Provider {} → Hub ID {}", providerTypeId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[BKAV_REVERSE_MAP] INVOICE_TYPE - Parse failed for '{}', fallback to provider value: {}", internalId, providerTypeId);
            return providerTypeId;
        }
    }
    
    private Integer reverseMapPaymentMethod(String lid, Integer providerPayMethodId) {
        if (providerPayMethodId == null) {
            log.debug("[BKAV_REVERSE_MAP] PAYMENT_METHOD - Provider payment method is null");
            return null;
        }

        String internalId = mappingService.getInternalCode(
                lid,
                "BKAV",
                MappingType.PAYMENT_METHOD,
                String.valueOf(providerPayMethodId));

        try {
            Integer result = Integer.parseInt(internalId);
            log.info("[BKAV_REVERSE_MAP] PAYMENT_METHOD - Provider {} → Hub ID {}", providerPayMethodId, result);
            return result;
        } catch (NumberFormatException e) {
            log.warn("[BKAV_REVERSE_MAP] PAYMENT_METHOD - Parse failed for '{}', fallback to provider value: {}", internalId, providerPayMethodId);
            return providerPayMethodId;
        }
    }
    
    private InvoiceResult buildSuccessResultFromBkav(BkavResponse.InvoiceResult bkavInvoice) {
        return InvoiceResult.builder()
                .success(true)
                .message("Tạo hóa đơn thành công")
                .invoiceGuid(bkavInvoice.getInvoiceGUID())
                .invoiceId(bkavInvoice.getInvoiceGUID() != null 
                    ? bkavInvoice.getInvoiceGUID().toString() 
                    : null)
                .providerInvoiceId(bkavInvoice.getInvoiceGUID() != null 
                    ? bkavInvoice.getInvoiceGUID().toString() 
                    : null)
                .invoiceForm(bkavInvoice.getInvoiceForm())
                .invoiceSerial(bkavInvoice.getInvoiceSerial())
                .invoiceNo(bkavInvoice.getInvoiceNo() != null 
                    ? String.valueOf(bkavInvoice.getInvoiceNo()) 
                    : null)
                .invoiceReferenceCode(bkavInvoice.getInvoiceLookupCode())
                .taxAuthorityCode(bkavInvoice.getTaxAuthorityCode())
                .status(bkavInvoice.getStatus())
                .messLog(bkavInvoice.getMessLog())
                .object(bkavInvoice)
                .build();
    }
    
    private InvoiceResult buildErrorResult(String message, Integer status) {
        return InvoiceResult.builder()
                .success(false)
                .message(message)
                .status(status)
                .build();
    }
}
