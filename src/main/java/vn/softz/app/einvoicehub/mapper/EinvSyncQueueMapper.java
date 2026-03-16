package vn.softz.app.einvoicehub.mapper;

import vn.softz.app.einvoicehub.dto.EinvSyncQueueDto;
import vn.softz.app.einvoicehub.domain.entity.EinvSyncQueueEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvSyncQueueMapper extends EntityMapper<EinvSyncQueueDto, EinvSyncQueueEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "invoiceId", ignore = true)
    @Mapping(target = "cqtMessageId", ignore = true)
    @Mapping(target = "syncType", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attemptCount", ignore = true)
    @Mapping(target = "maxAttempts", ignore = true)
    @Mapping(target = "lastError", ignore = true)
    @Mapping(target = "errorCode", ignore = true)
    @Mapping(target = "nextRetryAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvSyncQueueEntity toEntity(EinvSyncQueueDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvSyncQueueEntity entity, EinvSyncQueueDto dto);
}
