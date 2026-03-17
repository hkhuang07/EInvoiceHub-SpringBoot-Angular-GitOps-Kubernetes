package vn.softz.app.einvoicehub.service.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MappingStrategyRegistry {

    private final Map<String, MappingStrategy> strategies;

    public MappingStrategyRegistry(List<MappingStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                MappingStrategy::getMappingType,
                Function.identity()
            ));

        log.info("Registered {} mapping strategies: {}", strategies.size(), strategies.keySet());
    }

    public MappingStrategy getStrategy(MappingType type) {
        MappingStrategy strategy = strategies.get(type.name());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }
        return strategy;
    }

    public boolean hasStrategy(MappingType type) {
        return strategies.containsKey(type.name());
    }
}
