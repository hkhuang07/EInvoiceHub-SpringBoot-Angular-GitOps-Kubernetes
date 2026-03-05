#!/bin/bash
# verify_db.sh
DB_CMD="docker exec -i einvoice-mariadb mariadb -u einvoice_user -peinvoice_pass einvoicehub"

echo "--- 1. KIỂM TRA RÀNG BUỘC UNIQUE ---"
$DB_CMD -e "SELECT partner_invoice_id, COUNT(*) FROM einv_invoices GROUP BY partner_invoice_id HAVING COUNT(*) > 1;"

echo "--- 2. KIỂM TRA SAI LỆCH TIỀN (PRECISION) ---"
$DB_CMD -e "SELECT i.partner_invoice_id, (i.total_amount - SUM(d.total_amount)) as diff FROM einv_invoices i JOIN einv_invoices_detail d ON i.id = d.doc_id GROUP BY i.id HAVING diff != 0;"

echo "--- 3. KIỂM TRA AUDIT LOGS ---"
$DB_CMD -e "SELECT id, table_name, action_type, created_date FROM einv_audit_logs ORDER BY created_date DESC LIMIT 5;"
