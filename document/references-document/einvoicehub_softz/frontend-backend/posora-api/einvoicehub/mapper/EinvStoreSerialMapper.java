package vn.softz.app.einvoicehub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialDto;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EinvStoreSerialMapper {
    
    EinvStoreSerialDto toDto(EinvStoreSerialEntity entity);
    
    List<EinvStoreSerialDto> toDtoList(List<EinvStoreSerialEntity> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "storeId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvStoreSerialEntity toEntity(EinvStoreSerialRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "storeId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    void updateEntity(EinvStoreSerialRequest request, @MappingTarget EinvStoreSerialEntity entity);
}
