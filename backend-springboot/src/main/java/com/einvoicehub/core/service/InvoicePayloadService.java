package com.einvoicehub.core.service;

import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import com.einvoicehub.core.repository.mongodb.InvoicePayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePayloadService {

    private final InvoicePayloadRepository invoicePayloadRepo;

    public InvoicePayload savePayload(InvoicePayload invoicePayload) {
        log.info("Saving invoice payload for clientRequestId: {}", invoicePayload.getClientRequestId());
        return invoicePayloadRepo.save(invoicePayload);
    }

    public void updatePayloadWithResponse(String requestId, String responseData, String status) {
        log.info("Updating payload response for requestId: {} with status: {}", requestId, status);

        InvoicePayload payload = invoicePayloadRepo.findByClientRequestId(requestId)
                .orElseThrow(() -> {
                    log.error("Payload not found for requestId: {}", requestId);
                    return new AppException(ErrorCode.VALIDATION_ERROR, "Payload reference not found");
                });

        payload.setResponseRaw(responseData);
        payload.setStatus(status);
        invoicePayloadRepo.save(payload);
    }

    public Optional<InvoicePayload> findByClientRequestId(String requestId) {
        return invoicePayloadRepo.findByClientRequestId(requestId);
    }
}