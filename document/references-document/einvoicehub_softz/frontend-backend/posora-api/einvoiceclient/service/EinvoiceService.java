package vn.softz.app.einvoiceclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.biz.invoice.domain.entity.InvoiceEntity;
import vn.softz.app.biz.retail.domain.entity.RetailEntity;
import vn.softz.app.biz.retail.domain.repository.BizRetailRepository;
import vn.softz.app.biz.retail_detail.domain.entity.RetailDetailEntity;
import vn.softz.app.biz.retail_detail.domain.repository.BizRetailDetailRepository;
import vn.softz.app.einvoiceclient.domain.repository.InvoiceRepository;
import vn.softz.app.einvoiceclient.dto.EinvoiceSubmitRequest;
import vn.softz.app.einvoiceclient.dto.hub.SubmitInvoiceRequest;
import vn.softz.app.einvoiceclient.mapper.EinvoiceMapper;
import vn.softz.app.einvoicehub.dto.request.SignInvoicesRequest;
import vn.softz.app.einvoicehub.dto.request.GetInvoicesRequest;
import vn.softz.app.einvoicehub.service.EinvoiceHubService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EinvoiceService {
    
    private final EinvoiceHubService einvoiceHubService;
    private final EinvoiceMapper einvoiceMapper;
    private final BizRetailRepository retailRepository;
    private final BizRetailDetailRepository retailDetailRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public Object submitInvoice(EinvoiceSubmitRequest request) {
        InvoiceEntity entity = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + request.getInvoiceId()));

        RetailEntity retail = retailRepository.findById(entity.getDocId())
                .orElseThrow(() -> new RuntimeException("Retail not found: " + entity.getDocId()));
        
        List<RetailDetailEntity> details = retailDetailRepository.findAllByDocId(retail.getId());
        
        SubmitInvoiceRequest hubRequest = einvoiceMapper.toHubSubmitRequest(
                retail, details, entity, request.getSubmitType()
        );
        
        return einvoiceHubService.submitInvoice(hubRequest, request.getSubmitType());
    }

    public Object signInvoices(List<vn.softz.app.einvoiceclient.dto.SignInvoicesRequest> items) {
        List<SignInvoicesRequest> hubItems = items.stream()
                .map(item -> SignInvoicesRequest.builder()
                        .idType(item.getIdType())
                        .invoiceId(item.getInvoiceId())
                        .build())
                .collect(Collectors.toList());
        return einvoiceHubService.signInvoices(hubItems);
    }

    public Object getInvoiceDetail(List<vn.softz.app.einvoiceclient.dto.GetInvoicesRequest> request) {
        List<GetInvoicesRequest> hubItems = request.stream()
                .map(item -> GetInvoicesRequest.builder()
                        .idType(item.getIdType())
                        .invoiceId(item.getInvoiceId())
                        .build())
                .collect(Collectors.toList());
        return einvoiceHubService.getInvoices(hubItems);
    }
}