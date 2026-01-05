package com.einvoicehub.core.entity.mongodb;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "provider_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "merchant_timestamp_idx", def = "{'merchantId': 1, 'timestamp': -1}")
@CompoundIndex(name = "invoice_timestamp_idx", def = "{'invoiceMetadataId': 1, 'timestamp': -1}")
public class ProviderTransaction {

    @Id
    private String id;

    @Indexed
    @Field("invoiceMetadataId")
    private Long invoiceMetadataId;

    @Indexed
    @Field("merchantId")
    private Long merchantId;

    @Indexed
    @Field("providerCode")
    private String providerCode;

    @Field("transactionType")
    private TransactionType transactionType;

    @Field("request")
    private TransactionRequest request;

    @Field("response")
    private TransactionResponse response;

    @Field("latencyMs")
    private Long latencyMs;

    @Field("status")
    private TransactionStatus status;

    @Field("retryCount")
    @Builder.Default
    private Integer retryCount = 0;

    @Field("errorDetails")
    private Map<String, Object> errorDetails;

    @CreatedDate
    @Field("timestamp")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public enum TransactionType {
        CREATE_INVOICE,
        GET_INVOICE,
        CANCEL_INVOICE,
        REPLACE_INVOICE,
        SYNC_STATUS,
        AUTHENTICATE
    }

    public enum TransactionStatus {
        PENDING,
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        TIMEOUT
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TransactionRequest {
        private String url;
        private String httpMethod;
        private Map<String, String> headers;
        private Map<String, Object> body;
        private String rawPayload;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TransactionResponse {
        private Integer httpStatusCode;
        private String httpStatusMessage;
        private Map<String, Object> body;
        private String rawResponse;
        private String providerTransactionCode;
        private String providerErrorCode;
        private String providerErrorMessage;
    }
}