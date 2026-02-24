package com.einvoicehub.core.mapper;

import com.einvoicehub.core.domain.entity.EinvInvoiceTemplateEntity;
import com.einvoicehub.core.dto.EinvStoreSerialDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvStoreSerialMapper {

    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "invoiceType.id", target = "invoiceTypeId")
    @Mapping(source = "invoiceType.typeName", target = "invoiceTypeName")
    @Mapping(source = "templateCode", target = "invoiceForm")
    @Mapping(source = "symbolCode", target = "invoiceSerial")
    @Mapping(source = "currentNumber", target = "currentNumber")
    EinvStoreSerialDto toDto(EinvInvoiceTemplateEntity entity);

    List<EinvStoreSerialDto> toDtoList(List<EinvInvoiceTemplateEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "merchantId", target = "merchant.id")
    @Mapping(source = "invoiceTypeId", target = "invoiceType.id")
    @Mapping(source = "invoiceForm", target = "templateCode")
    @Mapping(source = "invoiceSerial", target = "symbolCode")
    EinvInvoiceTemplateEntity toEntity(EinvStoreSerialDto dto);
}