package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvInvoiceDetailDto;
import vn.softz.app.einvoicehub.dto.request.SubmitInvoiceDetailRequest;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceDetailEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EinvInvoiceDetailMapper {

    EinvInvoiceDetailDto toDto(EinvInvoiceDetailEntity entity);

    List<EinvInvoiceDetailDto> toDtoList(List<EinvInvoiceDetailEntity> entities);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "tenantId",    ignore = true)
    @Mapping(target = "storeId",     ignore = true)
    @Mapping(target = "docId",       ignore = true)
    @Mapping(target = "invoice",     ignore = true)
    @Mapping(target = "lineNo",      ignore = true)
    @Mapping(target = "isFree",      ignore = true)
    @Mapping(target = "netAmount",   ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "netPrice",    ignore = true)
    @Mapping(target = "netPriceVat", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "unit",   target = "unit")
    EinvInvoiceDetailEntity requestToEntity(SubmitInvoiceDetailRequest request);

    List<EinvInvoiceDetailEntity> requestsToEntities(List<SubmitInvoiceDetailRequest> requests);

    //DTO → Entity
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "tenantId",    ignore = true)
    @Mapping(target = "storeId",     ignore = true)
    @Mapping(target = "docId",       ignore = true)
    @Mapping(target = "invoice",     ignore = true)
    @Mapping(target = "lineNo",      ignore = true)
    @Mapping(target = "isFree",      ignore = true)
    @Mapping(target = "netAmount",   ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "netPrice",    ignore = true)
    @Mapping(target = "netPriceVat", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "unit",   target = "unit")
    EinvInvoiceDetailEntity dtoToEntity(EinvInvoiceDetailDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "tenantId",    ignore = true)
    @Mapping(target = "storeId",     ignore = true)
    @Mapping(target = "docId",       ignore = true)
    @Mapping(target = "invoice",     ignore = true)
    @Mapping(target = "lineNo",      ignore = true)
    @Mapping(target = "isFree",      ignore = true)
    @Mapping(target = "netAmount",   ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "netPrice",    ignore = true)
    @Mapping(target = "netPriceVat", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "unit",   target = "unit")
    void updateEntity(EinvInvoiceDetailDto dto,
                      @MappingTarget EinvInvoiceDetailEntity entity);
}