package com.einvoicehub.server.dto.request;
/**
 * Nghiệp vụ SubmitAdjustInvoice đã được GỘP vào {@link SubmitInvoiceRequest}.
 * Cách phân biệt:
 *   - SubmitInvoice       → submitInvoiceType ∈ {"100", "101", "102"}, referenceTypeId = 0
 *   - SubmitAdjustInvoice → submitInvoiceType ∈ {"111", "112"},        referenceTypeId = 2
 * Lý do gộp:
 *   - Hai luồng chia sẻ >90% field (header người mua, dòng hàng, tiền tệ...).
 *   - Gộp tránh duplicate validation và dễ maintain khi schema thay đổi.
 *   - Helper methods isAdjustRequest() / buildOrgInvoiceIdentify() xử lý logic phân nhánh.
 *   - Các trường riêng điều chỉnh (orgInvoice*, orgInvoiceReason) được validate
 *     tại Service layer khi isAdjustRequest() == true.
 * @see SubmitInvoiceRequest
 *  * @deprecated Dùng {@link SubmitInvoiceRequest} với submitInvoiceType = "111" hoặc "112".
 */
@Deprecated(since = "1.0", forRemoval = true)
public class SubmitAdjustInvoiceRequest extends SubmitInvoiceRequest {

}