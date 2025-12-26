package com.einvoicehub.core.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Base64;

public class EncryptionConverter implements AttributeConverter<String,String> {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY = "EInvoiceHubSecretKey_2025";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        // Logic mã hóa AES-256 (Base64)
        return Base64.getEncoder().encodeToString(encrypt(attribute.getBytes()));
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        // Logic giải mã
        return new String(decrypt(Base64.getDecoder().decode(dbData)));
    }
}
