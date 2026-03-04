export interface EinvoiceInvoiceDto {
    id?: string;
    partnerInvoiceId?: string;
    providerId?: string;
    providerInvoiceId?: string;
    invoiceForm?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: string;
    buyerTaxCode?: string;
    buyerCompany?: string;
    buyerFullName?: string;
    buyerAddress?: string;
    buyerMobile?: string;
    paymentMethodId?: number;
    paymentMethodName?: string;
    grossAmount?: number;
    discountAmount?: number;
    taxAmount?: number;
    totalAmount?: number;
    taxAuthorityCode?: string;
    notes?: string;
    signedDate?: string;
    statusId?: number;
    statusName?: string;
    createdByUsername?: string;
    createdByFullName?: string;
    createdDate?: string;
    createdBy?: string;
    updatedDate?: string;
    updatedBy?: string;
}

export interface InvoiceSearchRequest {
    keyword?: string;
    statusId?: number;
    providerId?: string;
    offset?: number;
    limit?: number;
}

export interface InvoiceStatus {
    id: number;
    name: string;
    description?: string;
    note?: string;
}

export interface InvoiceSearchResponse {
    offset: number;
    limit: number;
    total: number;
    count: number;
    items: EinvoiceInvoiceDto[];
}