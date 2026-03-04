package vn.softz.app.einvoicehub.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAttachmentData {
    private String fileName;
    private String fileExtension;
    private String fileContent; 
}
