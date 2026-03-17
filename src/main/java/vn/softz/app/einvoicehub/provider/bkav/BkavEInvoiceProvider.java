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
import vn.softz.app.einvoicehub.provider.model.InvoiceStatusResult;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("BKAV")
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
    public InvoiceResult getInvoiceData(String lid, String invoiceGuid) {
        try {
            //BkavResponse bkavResponse = soapClient.executeCommand(BkavCommandType.GET_INVOICE_DATA_WS, invoiceGuid);
            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.GET_INVOICE_DATA_WS, invoiceGuid);

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
    public InvoiceStatusResult getInvoiceStatusFull(String lid, String invoiceGuid) {
        log.info("[BKAV] getInvoiceStatusFull - CmdType 850 - lid: {}, invoiceGuid: {}", lid, invoiceGuid);

        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.GET_INVOICE_STATUS_WITH_TAX_CODE, invoiceGuid);

            if (!bkavResponse.isSuccess()) {
                log.warn("[BKAV] getInvoiceStatusFull failed - invoiceGuid: {}, error: {}",
                        invoiceGuid, bkavResponse.getErrorMessage());
                return InvoiceStatusResult.error(bkavResponse.getErrorMessage());
            }

            List<BkavResponse.InvoiceStatus> statusList = bkavResponse.asInvoiceStatusList();

            if (statusList == null || statusList.isEmpty()) {
                log.warn("[BKAV] getInvoiceStatusFull - Danh sách trạng thái rỗng cho invoiceGuid: {}", invoiceGuid);
                return InvoiceStatusResult.error("Không có dữ liệu trạng thái từ BKAV");
            }

            BkavResponse.InvoiceStatus statusData = statusList.get(0);

            Integer bkavStatusId = statusData.getInvoiceStatusID();
            String taxAuthorityCode = statusData.getMaCuaCQT();

            log.info("[BKAV] getInvoiceStatusFull - Raw: statusId={}, MaCuaCQT={} cho invoiceGuid: {}",
                    bkavStatusId, taxAuthorityCode, invoiceGuid);

            Integer hubStatusId = reverseMapInvoiceStatus(lid, bkavStatusId);

            log.info("[BKAV] getInvoiceStatusFull - Map status: BKAV {} → Hub {}", bkavStatusId, hubStatusId);

            return InvoiceStatusResult.builder()
                    .success(true)
                    .bkavStatusId(bkavStatusId)
                    .hubStatusId(hubStatusId)
                    .taxAuthorityCode(taxAuthorityCode)
                    .invoiceLookupCode(statusData.getInvoiceCode())
                    .message("Lấy trạng thái đầy đủ thành công")
                    .build();

        } catch (Exception e) {
            log.error("[BKAV] Lỗi getInvoiceStatusFull - lid: {}, invoiceGuid: {}", lid, invoiceGuid, e);
            return InvoiceStatusResult.error("Lỗi lấy trạng thái đầy đủ: " + e.getMessage());
        }
    }

    /*@Override
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
    */
    @Override
    public Object getInvoiceStatus(String lid, String invoiceGuid) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.GET_INVOICE_STATUS_ID, invoiceGuid);

            if (!bkavResponse.isSuccess()) {
                log.warn("[BKAV] getInvoiceStatus failed - invoiceGuid: {}, error: {}",
                        invoiceGuid, bkavResponse.getErrorMessage());
                return InvoiceStatusResult.error(bkavResponse.getErrorMessage());
            }

            List<BkavResponse.InvoiceStatus> statusList = bkavResponse.asInvoiceStatusList();
            if (statusList != null && !statusList.isEmpty()) {
                return statusList.get(0);
            }

            Integer bkavStatusId = bkavResponse.asStatusId();

            if (bkavStatusId == null) {
                log.warn("[BKAV] getInvoiceStatus - Không parse được InvoiceStatusID từ response. invoiceGuid: {}, object: {}",
                        invoiceGuid, bkavResponse.getObject());
                return InvoiceStatusResult.error("Không lấy được trạng thái từ BKAV");
            }

            log.info("[BKAV] getInvoiceStatus - Raw BKAV InvoiceStatusID: {} cho invoiceGuid: {}",
                    bkavStatusId, invoiceGuid);

            Integer hubStatusId = reverseMapInvoiceStatus(invoiceGuid, bkavStatusId);

            log.info("[BKAV] getInvoiceStatus - Map status: BKAV {} → Hub {} cho invoiceGuid: {}",
                    bkavStatusId, hubStatusId, invoiceGuid);

            return InvoiceStatusResult.builder()
                    .success(true)
                    .bkavStatusId(bkavStatusId)
                    .hubStatusId(hubStatusId)
                    .taxAuthorityCode(null)
                    .message("Lấy trạng thái thành công")
                    .build();

        } catch (Exception e) {
            log.error("[BKAV] Lỗi getInvoiceStatus - invoiceGuid: {}", invoiceGuid, e);
            return InvoiceStatusResult.error("Lỗi lấy trạng thái hóa đơn: " + e.getMessage());
        }
    }

    public Object lookupCompany(String lid, String taxCode) {
        try {
            BkavResponse response = soapClient.executeCommand(
                    lid, BkavCommandType.GET_UNIT_INFO_BY_TAXCODE, taxCode);

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
    public String getInvoiceLink(String lid, String partnerInvoiceId) {
        try {
            BkavInvoice.RequestData data = BkavInvoice.RequestData.builder()
                    .partnerInvoiceID(Long.parseLong(partnerInvoiceId))
                    .build();
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(data));

            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.GET_INVOICE_LINK, jsonData);

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
    public String getInvoicePdf(String storeid, String docId) {
        return getInvoiceFile(null, docId, BkavCommandType.GET_INVOICE_DATA_FILE_PDF);
    }

    @Override
    public String getInvoiceXml(String storeid, String docId) {
        return getInvoiceFile(null, docId, BkavCommandType.GET_INVOICE_DATA_FILE_XML);
    }

    private String getInvoiceFile(String lid, String partnerInvoiceStringId, int commandType) {
        try {
            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, commandType, partnerInvoiceStringId);

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
    public InvoiceResult createInvoice(String lid, int commandType, InvoiceData invoiceData) {
        try {
            BkavInvoice.RequestData bkavData = dataMapper.toBkavRequestData(invoiceData.getLid(), invoiceData);
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(bkavData));
            log.debug("BKAV Request JSON: {}", jsonData);

            // BkavResponse bkavResponse = soapClient.executeCommand(commandType, jsonData);
            BkavResponse bkavResponse = soapClient.executeCommand(lid, commandType, jsonData);

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
    public InvoiceResult updateInvoiceByGuid(String lid, String invoiceGuid, InvoiceData invoiceData) {
        log.info("[BKAV][updateInvoiceByGuid] CmdType={} invoiceGuid={} partnerInvoiceId={}",
                BkavCommandType.UPDATE_INVOICE_BY_GUID,
                invoiceGuid,
                invoiceData.getPartnerInvoiceId());

        try {
            // Build RequestData — tương tự createAdjustmentInvoice nhưng dùng CmdType 210
            BkavInvoice.RequestData requestData =
                    dataMapper.toBkavAdjustRequestData(invoiceData.getLid(), invoiceData);

            // Set InvoiceGUID để BKAV biết cần cập nhật hóa đơn nào
            if (requestData.getInvoice() != null) {
                requestData.getInvoice().setInvoiceGUID(UUID.fromString(invoiceGuid));
            }

            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(requestData));
            log.debug("[BKAV][updateInvoiceByGuid] Request JSON: {}", jsonData);

            // Gọi BKAV CmdType=210
            BkavResponse bkavResponse = soapClient.executeCommand(lid, BkavCommandType.UPDATE_INVOICE_BY_GUID, jsonData);

            if (!bkavResponse.isSuccess()) {
                String errorMsg = bkavResponse.getErrorMessage();
                log.warn("[BKAV][updateInvoiceByGuid] Provider error: {}", errorMsg);
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi cập nhật HĐ điều chỉnh từ BKAV",
                        bkavResponse.getStatus());
            }

            BkavResponse.InvoiceResult invoiceResult = bkavResponse.asInvoiceResult();

            if (invoiceResult == null) {
                log.warn("[BKAV][updateInvoiceByGuid] Response OK nhưng InvoiceResult null.");
                return InvoiceResult.error("BKAV không trả về kết quả cho hóa đơn cập nhật");
            }

            if (!invoiceResult.isSuccess()) {
                String errorMsg = invoiceResult.getMessLog();
                log.warn("[BKAV][updateInvoiceByGuid] Invoice-level error: {}", errorMsg);
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi cập nhật hóa đơn điều chỉnh",
                        invoiceResult.getStatus());
            }

            log.info("[BKAV][updateInvoiceByGuid] Thành công. InvoiceGUID={} InvoiceNo={}",
                    invoiceResult.getInvoiceGUID(), invoiceResult.getInvoiceNo());

            return buildSuccessResultFromBkav(invoiceResult);

        } catch (Exception e) {
            log.error("[BKAV][updateInvoiceByGuid] Exception invoiceGuid={} partnerInvoiceId={}",
                    invoiceGuid, invoiceData.getPartnerInvoiceId(), e);
            return buildErrorResult("Lỗi kết nối BKAV: " + e.getMessage(), 1);
        }
    }

    @Override
    public InvoiceResult updateInvoiceByPartnerId(String lid, String partnerInvoiceId, InvoiceData invoiceData) {
        throw new UnsupportedOperationException("Chưa implement");
    }

    @Override
    public InvoiceResult signInvoiceByHsm(String lid, String invoiceGuid) {
        try {
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(invoiceGuid));
            //BkavResponse bkavResponse = soapClient.executeCommand(BkavCommandType.SIGN_INVOICE_BY_HSM, invoiceGuid);
            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.SIGN_INVOICE_BY_HSM, invoiceGuid);

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
                    .signedDate(Instant.now())
                    .object(bkavResponse.getObject())
                    .build();

        } catch (Exception e) {
            log.error("Lỗi ký hóa đơn BKAV: ", e);
            return buildErrorResult("Lỗi ký hóa đơn: " + e.getMessage(), 1);
        }
    }

    @Override
    public InvoiceResult signInvoiceBatchByHsm(String lid, List<String> invoiceGuids) {
        log.info("[BKAV] Bắt đầu ký lô {} hóa đơn qua CmdType 205 ", invoiceGuids.size());
        try {
            for (String guid : invoiceGuids) {
                InvoiceResult singleResult = this.signInvoiceByHsm(lid, guid);

                if (!singleResult.isSuccess()) {
                    log.error("[BKAV] Lỗi khi ký hóa đơn {} trong lô: {}", guid, singleResult.getMessage());
                    return buildErrorResult("Hóa đơn " + guid + " ký lỗi: " + singleResult.getMessage(), 1);
                }
            }
            return InvoiceResult.builder()
                    .success(true)
                    .message("Ký lô hóa đơn bằng HSM thành công")
                    .signedDate(Instant.now())
                    .build();

        } catch (Exception e) {
            log.error("[BKAV] Lỗi xử lý ký lô hóa đơn: ", e);
            return buildErrorResult("Lỗi xử lý ký lô hóa: " + e.getMessage(), 1);
        }
    }

    @Override
    public InvoiceResult createAdjustmentInvoice(String lid, int submitType, InvoiceData invoiceData) {

        log.info("[BKAV][createAdjustmentInvoice] CmdType={} partnerInvoiceId={}",
                BkavCommandType.CREATE_INVOICE_ADJUST_SET_NO,
                invoiceData.getPartnerInvoiceId());

        try {
            BkavInvoice.RequestData bkavData =
                    dataMapper.toBkavAdjustRequestData(invoiceData.getLid(), invoiceData);

            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(bkavData));
            log.debug("[BKAV][createAdjustmentInvoice] Request JSON: {}", jsonData);

            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.CREATE_INVOICE_ADJUST_SET_NO, jsonData);
            //BkavResponse bkavResponse = soapClient.executeCommand(BkavCommandType.CREATE_INVOICE_ADJUST, jsonData);

            if (!bkavResponse.isSuccess()) {
                String errorMsg = bkavResponse.getErrorMessage();
                log.warn("[BKAV][createAdjustmentInvoice] Provider error: {}", errorMsg);
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi tạo HĐ điều chỉnh từ BKAV",
                        bkavResponse.getStatus());
            }

            BkavResponse.InvoiceResult invoiceResult = bkavResponse.asInvoiceResult();
            if (invoiceResult == null) {
                log.warn("[BKAV][createAdjustmentInvoice] Response OK nhưng InvoiceResult null.");
                return InvoiceResult.error("BKAV không trả về kết quả cho hóa đơn điều chỉnh");
            }

            if (!invoiceResult.isSuccess()) {
                String errorMsg = invoiceResult.getMessLog();
                log.warn("[BKAV][createAdjustmentInvoice] Invoice-level error: {}", errorMsg);
                return buildErrorResult(
                        errorMsg != null ? "Lỗi từ BKAV: " + errorMsg : "Lỗi tạo hóa đơn điều chỉnh",
                        invoiceResult.getStatus());
            }

            log.info("[BKAV][createAdjustmentInvoice] Thành công. InvoiceGUID={} InvoiceNo={}",
                    invoiceResult.getInvoiceGUID(), invoiceResult.getInvoiceNo());

            return buildSuccessResultFromBkav(invoiceResult);

        } catch (Exception e) {
            log.error("[BKAV][createAdjustmentInvoice] Exception partnerInvoiceId={}",
                    invoiceData.getPartnerInvoiceId(), e);
            return buildErrorResult("Lỗi kết nối BKAV: " + e.getMessage(), 1);
        }
    }
    /*@Override
    public InvoiceResult createAdjustmentInvoice(InvoiceData invoiceData) {
        return createInvoice(BkavCommandType.CREATE_INVOICE_ADJUST, invoiceData);
    }
    */
    @Override
    public InvoiceResult createReplacementInvoice(String lid, InvoiceData invoiceData) {
        return createInvoice(lid, BkavCommandType.CREATE_INVOICE_REPLACE, invoiceData);
    }

    @Override
    public InvoiceResult deleteInvoiceByGuid(String lid, String invoiceGuid) {
        throw new UnsupportedOperationException("Chưa implement");
    }
    @Override
    public InvoiceResult deleteInvoiceByPartnerId(String lid, String partnerInvoiceId) {
        throw new UnsupportedOperationException("Chưa implement");
    }

   /*@Override
    public InvoiceResult cancelInvoiceByGuid(String lid, String invoiceGuid, String reason) {
        try {
            BkavInvoice.RequestData cancelData = BkavInvoice.RequestData.builder()
                    .invoice(BkavInvoice.Header.builder()
                            .invoiceGUID(UUID.fromString(invoiceGuid))
                            .build())
                    .build();
            String jsonData = OBJECT_MAPPER.writeValueAsString(List.of(cancelData));

            BkavResponse bkavResponse = soapClient.executeCommand(
                    lid, BkavCommandType.CANCEL_INVOICE_BY_GUID, jsonData);

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
    }*/
    /*@Override
    public InvoiceResult cancelInvoiceByPartnerId(String lid, String partnerInvoiceId, String reason) {
        throw new UnsupportedOperationException("Chưa implement");
    }*/


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
