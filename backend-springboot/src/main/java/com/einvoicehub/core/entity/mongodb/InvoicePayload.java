package com.einvoicehub.core.entity.mongodb;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "invoice_payloads")
@Data
public class InvoicePayload {
    @Id
    private String id;
    private Long merchantId;
    private String clientRequestId; // Để chống trùng lặp dữ liệu
    private Map<String, Object> data; // Lưu JSON thô của hóa đơn
    private LocalDateTime createdAt = LocalDateTime.now();
}