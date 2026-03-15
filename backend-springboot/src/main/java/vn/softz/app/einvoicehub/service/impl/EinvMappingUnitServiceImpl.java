package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingUnitEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvMappingUnitRepository;
import vn.softz.app.einvoicehub.dto.EinvMappingUnitDto;
import vn.softz.app.einvoicehub.mapper.EinvMappingUnitMapper;
import vn.softz.app.einvoicehub.service.mapping.EinvMappingUnitService;

import java.util.List;
import java.util.Optional;

/**
 * Mapping Đơn vị tính Hub ↔ NCC ({@code einv_mapping_unit}).
 *
 * <p>Hub unitCode là String (VD: "DVT01", "DVT05"),
 * NCC có chuẩn riêng (VD: BKAV dùng "Cái", MobiFone dùng "PIECE").
 *
 * <pre>
 *   Hub unitCode ↔ NCC providerUnitCode
 *   "DVT01" (Cái)  ↔ BKAV: "Cái"    | MobiFone: "PIECE"
 *   "DVT05" (Kg)   ↔ BKAV: "Kg"     | MobiFone: "KG"
 *   "DVT07" (Lít)  ↔ BKAV: "Lít"    | MobiFone: "LITER"
 * </pre>
 *
 * <p>Khi NCC không có mapping cho unitCode → fallback về giá trị Hub gốc
 * (tên đơn vị tính bằng tiếng Việt) để tránh lỗi XML schema.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMappingUnitServiceImpl
        extends AbstractMappingServiceImpl<EinvMappingUnitDto, String>
        implements EinvMappingUnitService {

    private final EinvMappingUnitRepository repository;
    private final EinvMappingUnitMapper      mapper;

    @Override protected String mappingName() { return "Unit"; }

    @Override
    protected List<EinvMappingUnitDto> doFindByProvider(String p) {
        return mapper.toDtoList(repository.findByProviderId(p));
    }

    @Override
    protected List<EinvMappingUnitDto> doFindActive(String p) {
        return mapper.toDtoList(
            repository.findByProviderId(p).stream()
                      .filter(e -> !Boolean.TRUE.equals(e.getInactive())).toList());
    }

    @Override
    protected Optional<EinvMappingUnitDto> doFindById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    protected Optional<String> doLookupProviderCode(String providerId, String hubUnitCode) {
        return repository.findByProviderIdAndUnitCode(providerId, hubUnitCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingUnitEntity::getProviderUnitCode);
    }

    @Override
    protected Optional<String> doLookupHubId(String providerId, String providerUnitCode) {
        return repository.findByProviderIdAndProviderUnitCode(providerId, providerUnitCode)
                         .filter(e -> !Boolean.TRUE.equals(e.getInactive()))
                         .map(EinvMappingUnitEntity::getUnitCode);
    }

    @Override
    @Transactional
    protected EinvMappingUnitDto doCreate(EinvMappingUnitDto dto) {
        if (existsByProviderAndHubUnit(dto.getProviderId(), dto.getUnitCode())) {
            throw duplicateMapping(dto.getProviderId(), dto.getUnitCode());
        }
        EinvMappingUnitEntity entity = mapper.toEntity(dto);
        repository.saveAndFlush(entity);
        log.info("[mapping-Unit] Created: providerId={}, hubUnitCode={}, providerCode={}",
                 dto.getProviderId(), dto.getUnitCode(), dto.getProviderUnitCode());
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected EinvMappingUnitDto doUpdate(String id, EinvMappingUnitDto dto) {
        EinvMappingUnitEntity entity = repository.findById(id)
                .orElseThrow(() -> notFound(id));
        mapper.partialUpdate(entity, dto);
        repository.saveAndFlush(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    protected void doSoftDelete(String id) {
        repository.findById(id).ifPresent(e -> { e.setInactive(true); repository.save(e); });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProviderAndHubUnit(String providerId, String unitCode) {
        return repository.findByProviderIdAndUnitCode(providerId, unitCode).isPresent();
    }
}
