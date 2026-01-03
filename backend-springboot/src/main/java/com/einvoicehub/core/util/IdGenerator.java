package com.einvoicehub.core.util;

import org.springframework.stereotype.Component;

/**
 * ID Generator Utility
 * 
 */
@Component
public class IdGenerator {

    public String generateClientRequestId() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String timestamp = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String random = generateRandom(6);
        return String.format("REQ-%s-%s", timestamp, random);
    }


    public String generateTransactionId() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String timestamp = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String random = generateRandom(8);
        return String.format("TXN-%s-%s", timestamp, random);
    }


    public String generateInvoiceNumber() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String date = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = generateRandom(6);
        return String.format("INV-%s-%s", date, random);
    }


    private String generateRandom(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}