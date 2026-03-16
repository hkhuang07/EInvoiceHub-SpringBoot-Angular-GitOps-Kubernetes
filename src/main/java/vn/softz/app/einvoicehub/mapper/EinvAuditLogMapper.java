package vn.softz.app.einvoicehub.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softz.app.einvoicehub.dto.EinvAuditLogDto;
import vn.softz.app.einvoicehub.domain.entity.EinvAuditLogEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EinvAuditLogMapper extends EntityMapper<EinvAuditLogDto, EinvAuditLogEntity> {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "action", ignore = true)
    @Mapping(target = "entityName", ignore = true)
    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "payload", ignore = true) // Đã sửa: payLoad -> payload (chữ l thường)
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "errorMsg", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    EinvAuditLogEntity toEntity(EinvAuditLogDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget EinvAuditLogEntity entity, EinvAuditLogDto dto);


    default JsonNode mapStringToJsonNode(String value) {
        try {
            if (value == null || value.isEmpty()) return null;
            return OBJECT_MAPPER.readTree(value);
        } catch (Exception e) {
            return null;
        }
    }

    default String mapJsonNodeToString(JsonNode value) {
        if (value == null) return null;
        return value.toString();
    }
}