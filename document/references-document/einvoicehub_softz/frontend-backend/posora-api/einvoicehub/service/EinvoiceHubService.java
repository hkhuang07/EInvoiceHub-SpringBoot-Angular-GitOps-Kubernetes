package vn.softz.app.einvoicehub.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.core.audit.TenantAware;
import vn.softz.core.common.Common;
import vn.softz.core.exception.InvalidDataException;
import vn.softz.core.exception.UnAuthorizedException;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceDetailEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvUserAuthorEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvHubInvoiceRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvInvoiceStatusRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvUserAuthorRepository;
import vn.softz.app.einvoicehub.domain.entity.EinvPaymentMethodEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvPaymentMethodRepository;
import vn.softz.app.einvoicehub.dto.EinvoiceHubResponse;
import vn.softz.app.einvoicehub.dto.PaginationResponseDto;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.dto.PaginationRequestDto;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoice.SubmitInvoiceRequest;
import vn.softz.app.einvoicehub.dto.response.GetInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.ListInvoicesResponse;
import vn.softz.app.einvoicehub.dto.response.SignInvoiceResponse;
import vn.softz.app.einvoicehub.dto.response.SubmitInvoiceResponse;
import vn.softz.app.einvoicehub.mapper.EinvHubMapper;
import vn.softz.app.einvoicehub.provider.EInvoiceProvider;
import vn.softz.app.einvoicehub.provider.EInvoiceProviderFactory;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.app.einvoicehub.util.PaginationUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvoiceHubService {

    private final EinvHubInvoiceRepository invoiceRepository;
    private final EinvHubMapper mapper;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final EinvStoreProviderRepository storeProviderRepository;
    private final EInvoiceProviderFactory providerFactory;
    private final EinvStoreSerialRepository serialRepository;
    private final EinvProviderRepository einvProviderRepository;
    private final EinvUserAuthorRepository userAuthorRepository;
    private final EinvInvoiceStatusRepository einvoiceStatusRepository;
    private final EinvPaymentMethodRepository paymentMethodRepository;
    private static final int MAX_SIGN_INVOICES_PER_REQUEST = 30;
    private static final int MAX_GET_INVOICES_PER_REQUEST = 30;
    private static final Set<Integer> SKIP_PROVIDER_SYNC_STATUSES = Set.of(2, 5, 7, 8, 9);

    @Transactional
    @TenantAware
    public EinvoiceHubResponse<SubmitInvoiceResponse> submitInvoice(Object data, int invoiceType) {
        String storeId = Common.getCurrentUser()
            .orElseThrow(UnAuthorizedException::new)
            .getLocId();
        SubmitInvoiceRequest request = objectMapper.convertValue(data, SubmitInvoiceRequest.class);

        String partnerInvoiceId = request.getPartnerInvoiceId();

        boolean exists = invoiceRepository.existsByPartnerInvoiceId(partnerInvoiceId);

        if (exists) {
            return EinvoiceHubResponse.alreadyExists("einv.error.invoice_already_exists");
        }

        EinvStoreProviderEntity storeProvider = storeProviderRepository.findByStoreId(storeId)
            .orElseThrow(() -> new InvalidDataException("einv.error.store_provider_not_found"));

        if (storeProvider.getStatus() == null || storeProvider.getStatus() != 1) {
            return EinvoiceHubResponse.badRequest("", "einv.error.integration_not_registered");
        }
        
        EinvProviderEntity einvProvider = einvProviderRepository.findById(storeProvider.getProviderId())
            .orElseThrow(() -> new InvalidDataException("einv.error.provider_not_found"));
        
        Optional<EinvStoreSerialEntity> serialOpt = serialRepository.findLatestActive(storeId, storeProvider.getProviderId());
        
        EinvHubInvoiceEntity invoice = mapper.toEntity(request);
        invoice.setStatusId(0);
        invoice.setReferenceTypeId(0);
        invoice.setProviderId(storeProvider.getProviderId());

        // Set serial info if available
        if (serialOpt.isPresent()) {
            EinvStoreSerialEntity serial = serialOpt.get();
            invoice.setInvoiceForm(serial.getInvoiceForm());
            invoice.setInvoiceSeries(serial.getInvoiceSerial());
            invoice.setInvoiceTypeId(serial.getInvoiceTypeId());
            log.debug("Found active serial: {} - Form: {}", serial.getInvoiceSerial(), serial.getInvoiceForm());
        } else {
            log.warn("No active serial found for store: {} and provider: {}", storeId, storeProvider.getProviderId());
        }

        // tính detail
        if (request.getDetails() != null) {
            List<EinvHubInvoiceDetailEntity> details = mapper.toDetailEntities(request.getDetails());
            
            int lineNo = 1;
            for (EinvHubInvoiceDetailEntity detail : details) {
                detail.setLineNo(lineNo++);
                detail.setInvoice(invoice);
                
                // Calculate detail values
                // net_amount = gross_amount - discount_amount
                BigDecimal gross = detail.getGrossAmount() != null ? detail.getGrossAmount() : BigDecimal.ZERO;
                BigDecimal discount = detail.getDiscountAmount() != null ? detail.getDiscountAmount() : BigDecimal.ZERO;
                BigDecimal tax = detail.getTaxAmount() != null ? detail.getTaxAmount() : BigDecimal.ZERO;
                BigDecimal qty = detail.getQuantity() != null && detail.getQuantity().compareTo(BigDecimal.ZERO) != 0 
                               ? detail.getQuantity() : BigDecimal.ONE;
                
                BigDecimal netAmount = gross.subtract(discount);
                detail.setNetAmount(netAmount);
                
                // total_amount = net_amount + tax_amount
                BigDecimal totalAmount = netAmount.add(tax);
                detail.setTotalAmount(totalAmount);
                
                // net_price = net_amount / quantity
                detail.setNetPrice(netAmount.divide(qty, 2, java.math.RoundingMode.HALF_UP));
                
                // net_price_vat = total_amount / quantity
                detail.setNetPriceVat(totalAmount.divide(qty, 2, java.math.RoundingMode.HALF_UP));
            }
            invoice.setDetails(details);

            // Calculate totals for Header
            invoice.setGrossAmount(details.stream()
                .map(d -> d.getGrossAmount() != null ? d.getGrossAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
                
            invoice.setDiscountAmount(details.stream()
                .map(d -> d.getDiscountAmount() != null ? d.getDiscountAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
                
            invoice.setNetAmount(details.stream()
                .map(EinvHubInvoiceDetailEntity::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
                
            invoice.setTaxAmount(details.stream()
                .map(d -> d.getTaxAmount() != null ? d.getTaxAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
                
            invoice.setTotalAmount(details.stream()
                .map(EinvHubInvoiceDetailEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        invoice = invoiceRepository.save(invoice);

        // gọi nhà cc hóa đơn
        EInvoiceProvider provider = providerFactory.getProvider(storeProvider.getProviderId());
        var invoiceData = mapper.toInvoiceData(invoice);
        invoiceData.setLid(storeId);
        
        if (serialOpt.isPresent()) {
            invoiceData.setProviderSerialId(serialOpt.get().getProviderSerialId());
        }
        
        InvoiceResult result;

        if (invoiceType == 102) {
            result = provider.createInvoice(101, invoiceData);

            if (result.isSuccess()) {
                String invoiceGuid = result.getInvoiceId();
                InvoiceResult signResult = provider.signInvoiceByHsm(invoiceGuid);

                if (signResult.isSuccess()) {
                    invoice.setSignedDate(Instant.now());
                    invoice.setStatusId(2);
                    result.setSignedDate(signResult.getSignedDate());
                    result.setStatus(signResult.getStatus());
                } else {
                    log.debug("Sign failed: {}", signResult.getMessage());
                }
            } else {
                log.debug("Create invoice failed: {}", result.getMessage());
            }
        } else {
            result = provider.createInvoice(invoiceType, invoiceData);
        }
        
        if (result.isSuccess()) {
            invoice.setProviderInvoiceId(result.getInvoiceId());
            invoice.setInvoiceForm(result.getInvoiceForm());
            invoice.setInvoiceNo(result.getInvoiceNo());
            invoice.setInvoiceSeries(result.getInvoiceSerial());
            invoice.setInvoiceLookupCode(result.getInvoiceReferenceCode());
            invoice.setTaxAuthorityCode(result.getTaxAuthorityCode());
            invoice = invoiceRepository.save(invoice);
        } else {
            invoice.setStatusId(3);
            invoiceRepository.save(invoice);
            return EinvoiceHubResponse.providerError("", result.getMessage());
        }

        return EinvoiceHubResponse.success("requestId", SubmitInvoiceResponse.builder()
            .partnerInvoiceId(request.getPartnerInvoiceId())
            .invoiceId(invoice.getId())
            .invoiceForm(result.getInvoiceForm())
            .invoiceNo(result.getInvoiceNo())
            .invoiceLookupCode(result.getInvoiceReferenceCode())
            .taxAuthorityCode(result.getTaxAuthorityCode())
            .provider(invoice.getProviderId())
            .providerInvoiceId(invoice.getProviderInvoiceId())
            .urlLookup(einvProvider.getLookupUrl())
            .build());
    }

    @Transactional
    public EinvoiceHubResponse<List<SignInvoiceResponse>> signInvoices(List<SignInvoicesRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidDataException("einv.error.no_invoices_to_sign");
        }
        if (items.size() > MAX_SIGN_INVOICES_PER_REQUEST) {
            throw new InvalidDataException("einv.error.max_invoices_per_request");
        }

        List<SignInvoiceResponse> results = new ArrayList<>();  

        String storeId = Common.getCurrentUser()
            .orElseThrow(UnAuthorizedException::new)
            .getLocId();
            
        EinvStoreProviderEntity storeProvider = storeProviderRepository.findByStoreId(storeId)
            .orElseThrow(() -> new InvalidDataException("einv.error.store_provider_not_found"));
        
        EInvoiceProvider provider = providerFactory.getProvider(storeProvider.getProviderId());
        
        for (SignInvoicesRequest item : items) {
            EinvHubInvoiceEntity invoice = findInvoiceByIdType(item);

            if (invoice == null) continue;

            InvoiceResult result = provider.signInvoiceByHsm(invoice.getProviderInvoiceId());

            if (result.isSuccess()) {
                invoice.setSignedDate(Instant.now());
                invoice.setStatusId(2);
                invoiceRepository.save(invoice);
                results.add(SignInvoiceResponse.builder()
                    .partnerInvoiceId(invoice.getPartnerInvoiceId())
                    .invoiceId(invoice.getId())
                    .signedDate(Instant.now().toString())
                    .build()
                );
            }
        }
        
        return EinvoiceHubResponse.success("requestId", results);
    }

    @TenantAware
    public EinvoiceHubResponse<List<GetInvoicesResponse>> getInvoices(List<GetInvoicesRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidDataException("einv.error.no_invoices_to_get");
        }
        if (items.size() > MAX_GET_INVOICES_PER_REQUEST) {
            throw new InvalidDataException("einv.error.max_invoices_per_request");
        }

        List<GetInvoicesResponse> results = new ArrayList<>();

        String storeId = Common.getCurrentUser()
            .orElseThrow(UnAuthorizedException::new)
            .getLocId();

        EinvStoreProviderEntity storeProvider = storeProviderRepository.findByStoreId(storeId)
            .orElseThrow(() -> new InvalidDataException("einv.error.store_provider_not_found"));

        EInvoiceProvider provider = providerFactory.getProvider(storeProvider.getProviderId());

        for (GetInvoicesRequest item : items) {
            // 3.1
            EinvHubInvoiceEntity invoice = findInvoiceByIdType(item);

            if (invoice == null) continue;

            // 3.2
            if (SKIP_PROVIDER_SYNC_STATUSES.contains(invoice.getStatusId())) {
                results.add(mapper.toGetInvoicesResponse(invoice));
                continue;
            }

            try {
                InvoiceResult callProviderResult = provider.getInvoiceData(
                    storeId, 
                    invoice.getProviderInvoiceId()
                );

                if (callProviderResult.isSuccess()) {
                    if (callProviderResult.getInvoiceStatusId() != null) {
                        invoice.setStatusId(callProviderResult.getInvoiceStatusId());
                    }
                    if (callProviderResult.getInvoiceTypeId() != null) {
                        invoice.setInvoiceTypeId(callProviderResult.getInvoiceTypeId());
                    }
                    if (callProviderResult.getPaymentMethodId() != null) {
                        invoice.setPaymentMethodId(callProviderResult.getPaymentMethodId());
                    }
                    
                    invoice.setInvoiceDate(callProviderResult.getInvoiceDate());
                    invoice.setInvoiceForm(callProviderResult.getInvoiceForm());
                    invoice.setInvoiceSeries(callProviderResult.getInvoiceSerial());
                    invoice.setInvoiceNo(callProviderResult.getInvoiceNo());
                    invoice.setSignedDate(callProviderResult.getSignedDate());
                    invoice.setTaxAuthorityCode(callProviderResult.getTaxAuthorityCode());
                    invoice.setInvoiceLookupCode(callProviderResult.getInvoiceReferenceCode());
                    invoice.setProviderInvoiceId(callProviderResult.getProviderInvoiceId());
                    invoice = invoiceRepository.save(invoice);
                }
                
                results.add(mapper.toGetInvoicesResponse(invoice));
                
            } catch (Exception e) {
                log.warn("failed to get invoice data: {} - {}", invoice.getId(), e.getMessage());
                results.add(mapper.toGetInvoicesResponse(invoice));
            }
            
        }
        
        return EinvoiceHubResponse.success("requestId", results);
    }

    @Transactional(readOnly = true)
    @TenantAware
    public PaginationResponseDto<ListInvoicesResponse> listInvoices(PaginationRequestDto request) {
        // 1. Create Pageable
        Pageable pageable = PaginationUtil.createPageable(request, "createdDate");
        String searchString = null;
        Integer statusId = null;
        
        if (request.getFilters() != null) {
            Object searchObj = request.getFilters().get("searchString");
            if (searchObj != null) {
                searchString = searchObj.toString();
            }
            
            Object statusObj = request.getFilters().get("statusId");
            if (statusObj != null) {
                try {
                    statusId = Integer.parseInt(statusObj.toString());
                } catch (NumberFormatException e) {}
            }
        }
        
        // 3. Query with filters
        Page<EinvHubInvoiceEntity> page = invoiceRepository.findByFilters(
            searchString,
            statusId,
            pageable
        );

        // 3. Map to DTOs
        List<ListInvoicesResponse> content = page.getContent().stream()
            .map(mapper::toListResponse)
            .collect(Collectors.toList());

        // 4. Enrich with User Info and Status Name
        Set<String> userIds = content.stream()
            .map(ListInvoicesResponse::getCreatedBy)
            .filter(id -> id != null && !id.isEmpty())
            .collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            Map<String, EinvUserAuthorEntity> userMap = userAuthorRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(EinvUserAuthorEntity::getId, Function.identity()));
                
            content.forEach(dto -> {
                EinvUserAuthorEntity user = userMap.get(dto.getCreatedBy());
                if (user != null) {
                    dto.setCreatedByUsername(user.getName());
                    dto.setCreatedByFullName(user.getFullName());
                }
            });
        }
        
        Set<Integer> statusIds = content.stream()
            .map(ListInvoicesResponse::getStatusId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());

        if (!statusIds.isEmpty()) {
            Map<Integer, EinvInvoiceStatusEntity> statusMap = einvoiceStatusRepository.findAllById(statusIds).stream()
                .collect(Collectors.toMap(EinvInvoiceStatusEntity::getId, Function.identity()));

            content.forEach(dto -> {
                EinvInvoiceStatusEntity status = statusMap.get(dto.getStatusId());
                if (status != null) {
                    dto.setStatusName(status.getName());
                }
            });
        }

        Map<Integer, String> paymentMethodMap = paymentMethodRepository.findAll().stream()
            .collect(Collectors.toMap(EinvPaymentMethodEntity::getId, EinvPaymentMethodEntity::getName));

        content.forEach(dto -> {
            if (dto.getPaymentMethodId() != null) {
                dto.setPaymentMethodName(paymentMethodMap.get(dto.getPaymentMethodId()));
            }
        });
        
        // 5. Build response
        return PaginationUtil.buildResponse(request, page, content);
    }

    ///////////////////
    private EinvHubInvoiceEntity findInvoiceByIdType(GetInvoicesRequest item) {
        if (item.getIdType() == null || item.getIdType() == 0) {
            return invoiceRepository.findWithDetailsById(item.getInvoiceId()).orElse(null);
        } else if (item.getIdType() == 1) {
            return invoiceRepository.findTopByPartnerInvoiceIdOrderByCreatedDateDesc(item.getInvoiceId()).orElse(null);
        }
        return null;
    }
    
    private EinvHubInvoiceEntity findInvoiceByIdType(SignInvoicesRequest item) {
        if (item.getIdType() == null || item.getIdType() == 0) {
            return invoiceRepository.findById(item.getInvoiceId()).orElse(null);
        } else if (item.getIdType() == 1) {
            return invoiceRepository.findTopByPartnerInvoiceIdOrderByCreatedDateDesc(item.getInvoiceId()).orElse(null);
        }
        return null;
    }
}
