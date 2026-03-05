package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvInvoice;
import com.einvoicehub.server.domain.entity.EinvInvoiceDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback; // Thêm cái này
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat; // Đổi import này

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class InvoiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    //@Rollback(false)
    @Commit
    @DisplayName("Kiểm tra lưu Hóa đơn và Chi tiết hóa đơn (Cascade)")
    void shouldSaveInvoiceWithDetails() {
        // 1. Tạo Header
        EinvInvoice invoice = new EinvInvoice();
        invoice.setPartnerInvoiceId("INV-PROD-" + System.currentTimeMillis()); // Tránh trùng ID
        invoice.setTotalAmount(new BigDecimal("1100.00"));
        invoice.setDetails(new ArrayList<>()); // Khởi tạo list trống

        // 2. Tạo Detail
        EinvInvoiceDetail detail = new EinvInvoiceDetail();
        detail.setItemName("Sản phẩm Đồ Án Tốt Nghiệp");
        detail.setTotalAmount(new BigDecimal("1000.00"));
        detail.setTaxAmount(new BigDecimal("100.00"));
        detail.setInvoice(invoice); // Gán quan hệ cha-con

        invoice.getDetails().add(detail);

        // 3. Thực thi lưu xuống DB
        EinvInvoice saved = entityManager.persistAndFlush(invoice);

        // 4. Kiểm chứng
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDetails()).hasSize(1); // Kiểm tra có đúng 1 detail không
        assertThat(saved.getDetails().get(0).getItemName()).isEqualTo("Sản phẩm Đồ Án Tốt Nghiệp");

        System.out.println("--- ĐÃ INSERT THÀNH CÔNG UUID: " + saved.getId());
    }
}