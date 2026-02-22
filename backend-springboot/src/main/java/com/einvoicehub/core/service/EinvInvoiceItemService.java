package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.domain.repository.*;
import com.einvoicehub.core.dto.EinvInvoiceItemDto;
import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvInvoiceItemService {

    private final EinvInvoiceItemRepository repository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvVatRateRepository vatRateRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceItemDto> getByInvoiceId(Long invoiceId) {
        log.info("[Item] Lấy danh sách hàng hóa cho hóa đơn ID: {}", invoiceId);
        return repository.findByInvoiceIdOrderByLineNumberAsc(invoiceId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceItemDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dòng hàng không tồn tại"));
    }


    @Transactional
    public EinvInvoiceItemDto create(Long invoiceId, EinvInvoiceItemDto dto) {
        log.info("[Item] Thêm hàng hóa mới vào hóa đơn ID: {}", invoiceId);

        EinvInvoiceMetadataEntity invoice = metadataRepository.findById(invoiceId)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn cha không tồn tại"));
        if (invoice.getInvoiceStatus().getId() != 1) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn đã chốt, không được thêm dòng hàng");
        }

        EinvInvoiceItemEntity entity = mapper.toEntity(dto);
        entity.setInvoice(invoice);
        calculateItemAmounts(entity);
        entity = repository.save(entity);
        updateInvoiceTotals(invoice);

        return mapper.toDto(entity);
    }

    @Transactional
    public EinvInvoiceItemDto update(Long id, EinvInvoiceItemDto dto) {
        log.info("[Item] Cập nhật dòng hàng ID: {}", id);
        EinvInvoiceItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        if (entity.getInvoice().getInvoiceStatus().getId() != 1) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn đã chốt, không được sửa dòng hàng");
        }

        entity.setProductName(dto.getProductName());
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setDiscountAmount(dto.getDiscountAmount());

        calculateItemAmounts(entity);
        entity = repository.save(entity);

        updateInvoiceTotals(entity.getInvoice());
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Item] Xóa dòng hàng ID: {}", id);
        EinvInvoiceItemEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        EinvInvoiceMetadataEntity invoice = entity.getInvoice();
        if (invoice.getInvoiceStatus().getId() != 1) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn đã phát hành, không thể xóa chi tiết");
        }

        repository.delete(entity);
        updateInvoiceTotals(invoice);
    }

    private void calculateItemAmounts(EinvInvoiceItemEntity entity) {
        BigDecimal qty = entity.getQuantity() != null ? entity.getQuantity() : BigDecimal.ZERO;
        BigDecimal price = entity.getUnitPrice() != null ? entity.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal discount = entity.getDiscountAmount() != null ? entity.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal gross = qty.multiply(price).setScale(2, RoundingMode.HALF_UP);
        entity.setGrossAmount(gross);

        BigDecimal net = gross.subtract(discount);
        entity.setNetAmount(net);

        if (entity.getVatRate() != null) {
            BigDecimal taxRate = entity.getVatRate().getRatePercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal taxAmount = net.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
            entity.setTaxAmount(taxAmount);
            entity.setTaxRate(entity.getVatRate().getRatePercent());
        }
        entity.setTotalAmount(net.add(entity.getTaxAmount() != null ? entity.getTaxAmount() : BigDecimal.ZERO));
    }

    private void updateInvoiceTotals(EinvInvoiceMetadataEntity invoice) {
        List<EinvInvoiceItemEntity> items = repository.findByInvoiceIdOrderByLineNumberAsc(invoice.getId());

        BigDecimal subtotal = items.stream().map(EinvInvoiceItemEntity::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = items.stream().map(i -> i.getTaxAmount() != null ? i.getTaxAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setSubtotalAmount(subtotal);
        invoice.setTaxAmount(tax);
        invoice.setTotalAmount(subtotal.add(tax));

        metadataRepository.save(invoice);
        log.debug("[Transaction] Đã cập nhật lại tổng tiền cho hóa đơn ID: {}. Tổng cộng: {}", invoice.getId(), invoice.getTotalAmount());
    }
}