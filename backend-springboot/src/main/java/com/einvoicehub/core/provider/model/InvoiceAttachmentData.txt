package com.einvoicehub.core.provider.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAttachmentData {
    private String fileName;
    private String fileExtension;
    private String fileContent; //Base64
}