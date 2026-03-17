package vn.softz.app.einvoicehub.provider.bkav.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavReverseMapper {

    private final EinvoiceMappingService einvoiceMappingService;
    private static final String PROVIDER_ID = "BKAV";

    private Integer reverseMapToHubInteger(String storeId, MappingType type, Integer providerValue) {
        if (providerValue == null) {
            return null;
        }

        String hubId = einvoiceMappingService.getInternalCode(storeId, PROVIDER_ID, type, String.valueOf(providerValue));

        try {
            return Integer.parseInt(hubId);
        } catch (NumberFormatException e) {
            return providerValue;
        }
    }

    public String reverseMapTaxType(String storeId, Integer bkavTaxTypeId, Double bkavTaxRate) {
        if (bkavTaxTypeId == null) {
            return null;
        }

        String providerValue = bkavTaxTypeId.toString();
        if (bkavTaxRate != null) {
            providerValue = bkavTaxTypeId + "," + bkavTaxRate.intValue();
        }

        return einvoiceMappingService.getInternalCode(storeId, PROVIDER_ID, MappingType.TAX_TYPE, providerValue);
    }

    public Integer reverseMapInvoiceStatus(String storeId, Integer bkavStatusId) {
        return reverseMapToHubInteger(storeId, MappingType.INVOICE_STATUS, bkavStatusId);
    }

    public Integer reverseMapInvoiceType(String storeId, Integer bkavInvoiceTypeId) {
        return reverseMapToHubInteger(storeId, MappingType.INVOICE_TYPE, bkavInvoiceTypeId);
    }

    public Integer reverseMapPaymentMethod(String storeId, Integer bkavPaymentMethodId) {
        return reverseMapToHubInteger(storeId, MappingType.PAYMENT_METHOD, bkavPaymentMethodId);
    }

    public Integer reverseMapItemType(String lid, Integer bkavItemTypeId) {
        return reverseMapToHubInteger(lid, MappingType.ITEM_TYPE, bkavItemTypeId);
    }
}
