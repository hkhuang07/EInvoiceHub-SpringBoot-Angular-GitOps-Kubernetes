package vn.softz.app.einvoicehub.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.softz.cache.base.BaseCacheData;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EinvoiceMappingCacheData extends BaseCacheData {
    
    private String cacheKey;
    private Map<String, String> mapping;

    @Override
    public String getKey() {
        return cacheKey;
    }
}
