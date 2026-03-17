package vn.softz.app.einvoicehub.provider.bkav.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavForwardMapper {

    private final EinvoiceMappingService einvoiceMappingService;
    private static final String PROVIDER_ID = "BKAV";

    private Integer mapToProviderInteger(String storeId, MappingType type, Integer hubId, Integer defaultValue) {
        if (hubId == null) {
            hubId = defaultValue;
        }

        String mappedStr = einvoiceMappingService.getProviderCode(storeId, PROVIDER_ID, type, String.valueOf(hubId));

        try {
            return Integer.parseInt(mappedStr);
        } catch (NumberFormatException e) {
            return hubId;
        }
    }

    public String[] mapTaxType(String storeId, String hubTaxTypeId) {
        String defaultTaxTypeId = "3";
        String defaultTaxRate = "10";

        if (hubTaxTypeId == null || hubTaxTypeId.isEmpty()) {
            return new String[]{defaultTaxTypeId, defaultTaxRate};
        }

        String mappedStr = einvoiceMappingService.getProviderCode(
                storeId, PROVIDER_ID, MappingType.TAX_TYPE, hubTaxTypeId
        );

        if (mappedStr == null || !mappedStr.contains(",")) {
            return new String[]{defaultTaxTypeId, defaultTaxRate};
        }

        return mappedStr.split(",", 2);
    }

    public Integer mapInvoiceType(String storeId, Integer hubInvoiceTypeId, Integer defaultValue) {
        return mapToProviderInteger(storeId, MappingType.INVOICE_TYPE, hubInvoiceTypeId, defaultValue);
    }

    public Integer mapPaymentMethod(String storeId, Integer hubPaymentMethodId, Integer defaultValue) {
        return mapToProviderInteger(storeId, MappingType.PAYMENT_METHOD, hubPaymentMethodId, defaultValue);
    }

    public Integer mapItemType(String storeId, Integer hubItemTypeId, Integer defaultValue) {
        return mapToProviderInteger(storeId, MappingType.ITEM_TYPE, hubItemTypeId, defaultValue);
    }
}
