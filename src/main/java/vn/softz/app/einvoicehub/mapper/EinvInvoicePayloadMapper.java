package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvInvoicePayloadDto;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoicePayloadEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvInvoicePayloadMapper extends EntityMapper<EinvInvoicePayloadDto, EinvInvoicePayloadEntity> {

    @Override
    @Mapping(target = "invoiceId", ignore = true)
    @Mapping(target = "requestJson", ignore = true)
    @Mapping(target = "requestXml", ignore = true)
    @Mapping(target = "signedXml", ignore = true)
    @Mapping(target = "pdfData", ignore = true)
    @Mapping(target = "responseRaw", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvInvoicePayloadEntity toEntity(EinvInvoicePayloadDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvInvoicePayloadEntity entity, EinvInvoicePayloadDto dto);
}
