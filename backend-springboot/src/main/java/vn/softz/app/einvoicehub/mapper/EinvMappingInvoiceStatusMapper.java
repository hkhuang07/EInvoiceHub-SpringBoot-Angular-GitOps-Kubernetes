package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvMappingInvoiceStatusDto;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceStatusEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EinvMappingInvoiceStatusMapper {

    // Entity → DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "providerId",      ignore = true)
    @Mapping(target = "invoiceStatusId", ignore = true)
    @Mapping(target = "providerInvoiceStatusId", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "inactive",  ignore = true)
    @Mapping(target = "createdBy",     ignore = true)
    @Mapping(target = "updatedBy",     ignore = true)
    @Mapping(target = "createdDate",     ignore = true)
    @Mapping(target = "updatedDate",     ignore = true)
    EinvMappingInvoiceStatusDto toDto(EinvMappingInvoiceStatusEntity entity);

    List<EinvMappingInvoiceStatusDto> toDtoList(List<EinvMappingInvoiceStatusEntity> entities);

    // DTO → Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "providerId",      ignore = true)
    @Mapping(target = "invoiceStatusId", ignore = true)
    @Mapping(target = "providerInvoiceStatusId", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "createdBy",     ignore = true)
    @Mapping(target = "updatedBy",     ignore = true)
    @Mapping(target = "createdDate",     ignore = true)
    @Mapping(target = "updatedDate",     ignore = true)
    EinvMappingInvoiceStatusEntity toEntity(EinvMappingInvoiceStatusDto dto);

    // DTO → Entity (cập nhật, PATCH)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "providerId",      ignore = true)
    @Mapping(target = "invoiceStatusId", ignore = true)
    @Mapping(target = "providerInvoiceStatusId", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "inactive", ignore = true)
    @Mapping(target = "createdBy",     ignore = true)
    @Mapping(target = "updatedBy",     ignore = true)
    @Mapping(target = "createdDate",     ignore = true)
    @Mapping(target = "updatedDate",     ignore = true)
    void updateEntity(EinvMappingInvoiceStatusDto dto,
                      @MappingTarget EinvMappingInvoiceStatusEntity entity);

    // Converters
    @Named("byteToBoolean")
    default Boolean byteToBoolean(Byte value) {
        if (value == null) return null;
        return value != 0;
    }

    @Named("booleanToByte")
    default Byte booleanToByte(Boolean value) {
        if (value == null) return null;
        return value ? (byte) 1 : (byte) 0;
    }
}