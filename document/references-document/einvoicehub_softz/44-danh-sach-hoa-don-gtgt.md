# ....4.4. Danh sách hóa đơn GTGT

## **1. GIỚI THIỆU CHUNG**

Tài liệu này mô tả màn hình hiển thị danh sách HĐĐT và các chức năng tương ứng.

Màn hình này hỗ trợ người dùng quản lý, tra cứu, và thực hiện các tác vụ đối với hóa đơn điện tử được phát hành từ hệ thống bán hàng

Mục tiêu của màn hình này :

- Quản lý trạng thái phát hành hóa đơn đồng nhất cho đa nhà cung cấp như BKAV, VNPT, MISA, MobiFone...
- Cung cấp các chức năng nghiệp vụ như : cấp số hóa đơn, ký số, thay thế , điều chỉnh, cập nhật dữ liệu mới nhất từ đối tác hệ thống bán hàng.

## **2. Sơ đồ và FUNC\_CODE**

#### **2.1. Sơ đồ** 

 **2.1.1. Mô hình flow (đối với nhà cung cấp hóa đơn điện tử BKAV)**

 Dựa trên đậc tả luồng xử lý tại mục [4.3. Đặc tả luồng xử lý](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/43-dac-ta-luong-xu-ly "4.3-dac-ta-luong-xu-ly") quy trình tương tác khi thực hiện tác vụ (Cấp số, Ký, Điều chỉnh, Thay thế, Cập nhật) trên màn hình danh sách theo các bước sau

 **2.1.2. Logic xử lý:**

- - **Truy vấn danh sách hóa đơn**
        - **Step 1:** Hệ thống bán hàng gửi yêu cầu lấy danh sách hóa đơn kèm theo các bộ lọc như Khoảng ngày, Trạng thái, hoặc Từ khóa tìm kiếm như Số HĐ, MST khách hàng.
        - **Step 2:** Hệ thống eInvoce thực hiện truy vấn dữ liệu từ cơ sở dữ liệu thông qua các bảng einv\_invoices thông tin chung hóa đơn và einv\_invoice\_detail thông tin hàng hóa.
        - **Step 3:** Hệ thống trả về dữ liệu danh sách hóa đơn để hiển thị trên giao diện bảng danh sách hóa đơn.

[![get-list.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/get-list.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/get-list.jpg)

- - **Xử lý nghiệp vụ phát hành và thay đổi hóa đơn**
        - **Step 1:** Người dùng chọn bản ghi trên danh sách và nhấn button chức năng thực hiện tác vụ nghiệp vụ tùy theo mục đích như : Tạo/ Cấp số, Ký, Điều chỉnh, Thay thế.
        - **Step 2:** eInvoive thực hiện build bản tin nghiệp vụ và lưu bản tin tạm vào database để tracking tiến trình xử lý. Tại bước này, hệ thống xác định loại lệnh CmdType của Provider tương ứng với yêu cầu từ hệ thống bán hàng
        - **Step 3:** Thực hiện gọi API của Provider dựa trên mã lệnh đã xác định (theo D. Phụ Lục tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") ) 
            - Tác vụ Cấp số/ Tạo hóa đơn: Sử dụng CmdType: 101 . Thêm mới hóa đơn và để Bkav tự cấp số hóa đơn.
            - Tác vụ Ký số (HSM): Sử dụng CmdType: 205 . Ký hóa đơn bằng chữ ký số HSM (áp dụng khi hóa đơn ở trạng thái chờ ký).
            - Tác vụ Điều chỉnh: Sử dụng CmdType: 124 . Điều chỉnh hóa đơn và để Bkav cấp số hóa đơn mới.
            - Tác vụ Thay thế: Sử dụng CmdType: 123. Thay thế hóa đơn và để Bkav cấp số hóa đơn mới.
        - **Step 4:** Provider tiếp nhận, xử lý và phản hồi kết quả về eInvoice. Kết quả được coi là thành công khi trường Status trả về giá trị 0. (theo 2.Danh sách InvoiceStatusID tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") )
        - **Step 5:** Trường hợp thành công: eInvoice parse dữ liệu trả về từ Provider để cập nhật vào database các thông tin: status\_id (trạng thái mới), invoice\_no (số hóa đơn), invoice\_series (ký hiệu), và invoice\_lookup\_code (mã tra cứu).
        - **Step 6:** Trường hợp thất bại: eInvoice ghi nhận log lỗi chi tiết từ Provider trả về, giữ nguyên trạng thái cũ của hóa đơn và hiển thị thông báo lỗi để người dùng có hướng xử lý.
        - **Step 7**: Phản hồi kết quả xử lý cho hệ thống bán hàng để cập nhật trạng thái hiển thị trên giao diện người dùng.

[![process-invoice.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/process-invoice.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/process-invoice.jpg)

- - **Cập nhật đồng bộ thông tin hóa đơn**
        - **Step 1:** Khi người dùng nhấn Cập nhật đồng bộ thông tin (Refesh), eInvoice gửi yêu cầu tra cứu tới Provider bằng CmdType: 801 . Provider lấy chi tiết thông tin hóa đơn và trạng thái từ Cơ quan thuế
        - **Step 2:** Provider trả về thông tin trạng thái thực tế của hóa đơn
        - **Step 3:** Hệ thống eInvoice thực hiện đồng bộ hóa status\_id trong database nội bộ dựa trên dữ liệu mới nhận được để đảm bảo tính nhất quán giữa hệ thống eInvoice và Cơ quan thuế.
        - **Step 4:** Phản hồi dữ liệu hóa đơn, trạng thái hóa đơn mới nhất về giao diện danh sách hóa đơn của hệ thống bán hàng.

[![sync-invoice.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/sync-invoice.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/sync-invoice.jpg)

 **2.1.3. Xử lý API cho từng tác vụ cụ thể**

 Dựa vào ( D. Phụ Lục tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") ) và (<span class="fontstyle0">4. Các API liên quan đến Hóa đơn </span>tài liệu [ver 4\_7\_MobiFone Invoice\_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf](https://drive.google.com/file/d/1jqHw3ivh1MzgcxS0Q3KuHeuwP3cKIQcn/view?usp=sharing "ver 4_7_MobiFone Invoice_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf") ) mã lệnh CmdType và endpoint đươc áp dụng như sau:

- - - **Tạo/Cấp số hóa đơn:**
            - **BKAV:** Sử dụng mã lệnh CmdType: 101 (Thêm mới hóa đơn, để Bkav tự cấp số hóa đơn).
            - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/SaveListHoadon78 với tham số Editmode = 1 (Lưu hóa đơn mới).
        - **Ký hóa đơn:**
            - **BKAV:** Sử dụng mã lệnh CmdType: 205 (Thực hiện ký hóa đơn bằng chứng thư số HSM).
            - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/SaveAndSignHoadon78 để thực hiện đồng thời việc lưu dữ liệu và ký số.
        - **Điều chỉnh / Thay thế:**
            - **BKAV:**
                
                
                - **Điều chỉnh:** Sử dụng mã lệnh CmdType: 124 (Điều chỉnh hóa đơn, Bkav cấp số).
                - **Thay thế:** Sử dụng mã lệnh CmdType: 123 (Thay thế hóa đơn, Bkav cấp số).
            - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/SaveListHoadon78 kết hợp với tham số:
                
                
                - Editmode = 2 : Lưu hóa đơn điều chỉnh.
                - Editmode = 3: Lưu hóa đơn thay thế.
        - **Đồng bộ (Cập nhật thông tin hóa đơn):**
            - **BKAV:** Sử dụng mã lệnh CmdType: 801 (Lấy chi tiết thông tin hóa đơn và Trạng thái phản hồi từ Cơ quan Thuế).
            - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/GetHoadon78ByPartnerInvoiceID để truy vấn lại trạng thái và thông tin hóa đơn dựa trên ID định danh từ hệ thống đối tác.

#### **2.2. Function Code**

 **2.2.1. Các vai trò**

<table border="1" id="bkmrk-role-group-group-nam" style="border-collapse: collapse; width: 100%; height: 289.903px;"><colgroup><col style="width: 12.0397%;"></col><col style="width: 31.6198%;"></col><col style="width: 56.3537%;"></col></colgroup><tbody><tr style="height: 29.8785px;"><td style="height: 29.8785px;">**Role Group**</td><td style="height: 29.8785px;">**Group Name**</td><td style="height: 29.8785px;">**Description**</td></tr><tr><td>Admin</td><td>Administrators - PosORA</td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Quản trị viên hệ thống: Có toàn quyền thao tác, cấu hình hệ thống và là nhóm duy nhất được quyền xóa hóa đơn sai sót trong giai đoạn nháp</span></td></tr><tr><td>Business</td><td>Nhóm QL DM kinh doanh</td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Nhân viên nghiệp vụ: Thực hiện các tác vụ phát hành hóa đơn (cấp số, ký số), xử lý điều chỉnh hoặc thay thế hóa đơn theo yêu cầu bán hàng.</span></td></tr><tr style="height: 51.875px;"><td style="height: 51.875px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Reporter</span></td><td style="border: 1pt solid windowtext; padding: 0.75pt; height: 51.875px;">Nhóm QL báo cáo &amp; Group Marketing

</td><td style="height: 51.875px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Nhân viên khai thác dữ liệu: Chỉ có quyền xem danh sách, chi tiết và kết xuất báo cáo phục vụ thống kê, không được phép thay đổi trạng thái hóa đơn </span></td></tr><tr style="height: 51.875px;"><td style="border: 1pt solid windowtext; padding: 0.75pt; height: 51.875px;"> <span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Master Data</span>

</td><td style="border: 1pt solid windowtext; padding: 0.75pt; height: 51.875px;">Nhóm QL DM dùng chung

</td><td style="height: 51.875px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Quản lý dữ liệu danh mục: Theo dõi tính đồng bộ của dữ liệu và thực hiện cập nhật thông tin trạng thái từ Nhà cung cấp để đảm bảo tính nhất quán</span></td></tr></tbody></table>

 **2.2.2. Phân quyền chức năng**

<table border="1" id="bkmrk-danh-s%C3%A1ch-t%C3%A1c-v%E1%BB%A5-adm" style="border-collapse: collapse; width: 100%; height: 298.959px;"><colgroup><col style="width: 37.0728%;"></col><col style="width: 15.9735%;"></col><col style="width: 14.7815%;"></col><col style="width: 16.2119%;"></col><col style="width: 15.9735%;"></col></colgroup><tbody><tr style="height: 29.8785px;"><td style="height: 29.8785px;">**Danh sách tác vụ**</td><td style="height: 29.8785px;">**Admin**</td><td style="height: 29.8785px;">**Bussiness**</td><td style="height: 29.8785px;">**Reporter**</td><td style="height: 29.8785px;">**Dùng chung**</td></tr><tr style="height: 29.9653px;"><td style="height: 29.9653px;">Xem danh sách - Tìm kiếm</td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td></tr><tr style="height: 29.9653px;"><td style="height: 29.9653px;">Xem chi tiết hóa đơn</td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.9653px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Cấp số hóa đơn</td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Ký phát hành </td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Tạo hóa đơn thay thế</td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Tạo hóa đơn điều chỉnh</td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="color: rgb(224, 62, 45);">X</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Cập nhật thông tin - Đồng bộ</td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Kết xuất Excel - Báo cáo</td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td><td class="align-center" style="height: 29.8785px;"><span style="font-size: 11pt; line-height: 115%; font-family: 'Segoe UI Symbol', sans-serif; color: rgb(45, 194, 107);">✔</span></td></tr></tbody></table>

 **2.2.3. Nghiệp vụ kiểm soát quyền**

- - - **Kiểm soát nút Xóa:** Chỉ nhóm Administrators mới có quyền Xóa hóa đơn khi ở trạng thái Mới tạo/Chờ ký. Tuyệt đối không cho phép xóa hóa đơn đã có số chính thức (status\_id &gt;= 2).
        - **Ràng buộc trạng thái Ký:** Button 'Ký số' chỉ hiển thị/kích hoạt với nhóm Admin/Kinh doanh khi hóa đơn có status\_id thuộc tập hợp {1, 5, 7} (Đã cấp số, chờ ký).
        - <span style="mso-list: Ignore;"><span style="font: 7.0pt 'Times New Roman';"> </span></span>**Giao diện:** Nhóm Báo cáo/Marketing sẽ không nhìn thấy các nút tác vụ gây thay đổi dữ liệu như Cấp số, Ký, Điều chỉnh, Thay thế. Các thành phần này phải được ẩn hoàn toàn khỏi giao diện người dùng.
        - **Ràng buộc Thay thế/Điều chỉnh:** Chỉ hóa đơn ở trạng thái 'Đã phát hành' (status\_id = 2, 6, 8) mới được thực hiện lệnh Điều chỉnh hoặc Thay thế.

## **3. Cơ sở dữ liệu**

Phần này đặc tả cấu trúc lưu trữ dữ liệu cho các hóa đơn điện tử

#### **3.1. Table: einv\_invoices: lưu danh sách HĐĐT**

<table border="1" id="bkmrk-no.-field-name-type-" style="border-collapse: collapse; width: 100%; height: 1525.94px;"><colgroup><col style="width: 6.20032%;"></col><col style="width: 20.7472%;"></col><col style="width: 10.1351%;"></col><col style="width: 10.0159%;"></col><col style="width: 10.2544%;"></col><col style="width: 8.10811%;"></col><col style="width: 34.5787%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td class="xl72" height="21" style="height: 30.125px;" width="30">**No.**</td><td class="xl72" style="height: 30.125px;" width="139">**Field Name**</td><td class="xl72" style="height: 30.125px;" width="82">**Type**</td><td class="xl72" style="height: 30.125px;" width="51">**Length**</td><td class="xl72" style="height: 30.125px;" width="66">**Not null**</td><td class="xl72" style="height: 30.125px;" width="32">**Key**</td><td class="xl72" style="height: 30.125px;" width="243">**Description**</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">1</td><td style="height: 30.125px;">id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">Yes</td><td style="height: 30.125px;">PK</td><td style="height: 30.125px;">Khóa chính (UUID v7)</td></tr><tr style="height: 30.625px;"><td style="height: 30.625px;">2</td><td style="height: 30.625px;">tenant\_id</td><td style="height: 30.625px;">varchar</td><td style="height: 30.625px;">36</td><td style="height: 30.625px;">No</td><td style="height: 30.625px;">  
</td><td style="height: 30.625px;">ID của Tenant (Khách hàng)</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">3</td><td style="height: 46.6667px;">store\_id</td><td style="height: 46.6667px;">varchar</td><td style="height: 46.6667px;">36</td><td style="height: 46.6667px;">No</td><td style="height: 46.6667px;">  
</td><td style="height: 46.6667px;">ID của Cửa hàng/Chi nhánh phát hành</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">4</td><td style="height: 46.9167px;">partner\_invoice\_id</td><td style="height: 46.9167px;">varchar</td><td style="height: 46.9167px;">50</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">ID hóa đơn từ hệ thống cửa hàng/ chi nhánh phát hành (Receipt ID)</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">5</td><td style="height: 46.9167px;">provider\_id</td><td style="height: 46.9167px;">varchar</td><td style="height: 46.9167px;">36</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">ID nhà cung cấp HĐĐT (BKAV, MobiFone, MISA, VNPT, Viettel,...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">6</td><td style="height: 30.125px;">provider\_invoice\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">ID hóa đơn do phía Provider cấp.</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">7</td><td style="height: 46.9167px;">invoice\_type\_id</td><td style="height: 46.9167px;">int</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">Loại hóa đơn (1: GTGT, 2: Bán hàng, 6: PXK...)</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">8</td><td style="height: 46.9167px;">reference\_type\_id</td><td style="height: 46.9167px;">int</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">Tính chất (0: Gốc, 2: Điều chỉnh, 3: Thay thế,...)</td></tr><tr style="height: 52.5208px;"><td style="height: 52.5208px;">9</td><td style="height: 52.5208px;">status\_id</td><td style="height: 52.5208px;">int</td><td style="height: 52.5208px;">  
</td><td style="height: 52.5208px;">No</td><td style="height: 52.5208px;">  
</td><td style="height: 52.5208px;">Trạng thái HĐ (Xem InvoiceStatusID) [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") </td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">10</td><td style="height: 30.125px;">invoice\_form</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mẫu số hóa đơn </td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">11</td><td style="height: 30.125px;">invoice\_series</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ký hiệu hóa đơn </td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">12</td><td style="height: 30.125px;">invoice\_no</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số hóa đơn </td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">13</td><td style="height: 30.125px;">invoice\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày lập hóa đơn.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">14</td><td style="height: 30.125px;">payment\_method\_id</td><td style="height: 30.125px;">int</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Hình thức thanh toán (TM, CK,...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">15</td><td style="height: 30.125px;">buyer\_tax\_code</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mã số thuế người mua.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">16</td><td style="height: 30.125px;">buyer\_company</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">300</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tên đơn vị mua hàng.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">17</td><td style="height: 30.125px;">buyer\_id\_no</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">20</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số CCCD/Hộ chiếu người mua.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">18</td><td style="height: 30.125px;">buyer\_full\_name</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">200</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Họ tên người mua.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">19</td><td style="height: 30.125px;">buyer\_address</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">300</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Địa chỉ người mua.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">20</td><td style="height: 30.125px;">buyer\_mobile</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số điện thoại người mua</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">21</td><td style="height: 30.125px;">buyer\_bank\_account</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tài khoản ngân hàng người mua</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">22</td><td style="height: 30.125px;">buyer\_bank\_name</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">200</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tên tài khoản ngân hàng người mua</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">23</td><td style="height: 46.9167px;">buyer\_budget\_code</td><td style="height: 46.9167px;">varchar </td><td style="height: 46.9167px;">20</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">Mã số đơn vị quan hệ ngân sách (MSĐVCQHVNS)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">24</td><td style="height: 30.125px;">receive\_type\_id</td><td style="height: 30.125px;">int </td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Kiểu nhận hóa đơn (sms, email...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">25</td><td style="height: 30.125px;">receiver\_email</td><td style="height: 30.125px;">varchar </td><td style="height: 30.125px;">300</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Email nhận hóa đơn của người mua</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">26</td><td style="height: 30.125px;">currency\_code</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">20</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mã tiền tệ (VND, USD...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">27</td><td style="height: 30.125px;">exchange\_rate</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">10,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tỷ giá</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">28</td><td style="height: 30.125px;">signed\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày phát hành hóa đơn (Ký số) (dd/MM/YYYY hhmmss)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">29</td><td style="height: 30.125px;">tax\_authority\_code</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mã của cơ quan thuế cấp</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">30</td><td style="height: 30.125px;">org\_invoice\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">ID hóa đơn gốc (UUID tham chiếu)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">31</td><td style="height: 30.125px;">org\_invoice\_form</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mẫu số hóa đơn gốc</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">32</td><td style="height: 30.125px;">org\_invoice\_series</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ký hiệu hóa đơn gốc</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">33</td><td style="height: 30.125px;">org\_invoice\_no</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số hóa đơn gốc</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">34</td><td style="height: 30.125px;">org\_invoice\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày lập hóa đơn gốc</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">35</td><td style="height: 30.125px;">org\_invoice\_reason</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">500</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Lý do điều chỉnh/thay thế hóa đơn gốc</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">36</td><td style="height: 46.9167px;">invoice\_lookup\_code</td><td style="height: 46.9167px;">varchar </td><td style="height: 46.9167px;">50</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">Mã tra cứu hóa đơn trên website Provider</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">37</td><td style="height: 30.125px;">gross\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tổng thành tiền hàng hóa (Chưa thuế)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">38</td><td style="height: 30.125px;">discount\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tổng số tiền chiết khấu</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">39</td><td style="height: 30.125px;">net\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tổng thành tiền trước thuế</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">40</td><td style="height: 30.125px;">tax\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tổng tiền thuế GTGT</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">41</td><td style="height: 30.125px;">total\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Trị giá thanh toán (Tổng cộng có thuế)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">42</td><td style="height: 30.125px;">created\_by</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Người lập hóa đơn (dd/MM/YYYY hhmmss)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">43</td><td style="height: 30.125px;">updated\_by</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Người cập nhật hóa đơn (dd/MM/YYYY hhmmss)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">44</td><td style="height: 30.125px;">created\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày lập hóa đơn (dd/MM/YYYY hhmmss)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;"> 45</td><td style="height: 30.125px;">updated\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày cập nhật hóa đơn gần nhất (dd/MM/YYYY hhmmss)</td></tr></tbody></table>

#### **3.2. Table: einv\_invoices\_detail: lưu chi tiết HĐĐT**

<table border="1" id="bkmrk-no.-field-name-type--1" style="border-collapse: collapse; width: 100%; height: 843.5px;"><colgroup><col style="width: 6.08108%;"></col><col style="width: 21.1049%;"></col><col style="width: 10.1351%;"></col><col style="width: 9.65819%;"></col><col style="width: 10.6121%;"></col><col style="width: 8.11625%;"></col><col style="width: 34.2128%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td class="xl72" height="21" style="height: 30.125px;" width="30">**No.**</td><td class="xl72" style="height: 30.125px;" width="139">**Field Name**</td><td class="xl72" style="height: 30.125px;" width="82">**Type**</td><td class="xl72" style="height: 30.125px;" width="51">**Length**</td><td class="xl72" style="height: 30.125px;" width="66">**Not null**</td><td class="xl72" style="height: 30.125px;" width="32">**Key**</td><td class="xl72" style="height: 30.125px;" width="243">**Description**</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">1</td><td style="height: 30.125px;">id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">Yes</td><td style="height: 30.125px;">PK</td><td style="height: 30.125px;">Khóa chính (UUID v7)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">2</td><td style="height: 30.125px;">tenant\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">ID của Tenant (Khách hàng)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">3</td><td style="height: 30.125px;">store\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">ID của Cửa hàng/Chi nhánh</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">4</td><td style="height: 30.125px;">doc\_id</td><td style="height: 30.125px;">varchar36</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Liên kết tới trường id của bảng einv\_invoices</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">5</td><td style="height: 30.125px;">line\_no</td><td style="height: 30.125px;">int</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số thứ tự dòng hàng trên hóa đơn</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">6</td><td style="height: 30.125px;">is\_free</td><td style="height: 30.125px;">tinyint</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Đánh dấu hàng tặng, hàng khuyến mãi (0: Không, 1: Có)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">7</td><td style="height: 30.125px;">item\_type\_id</td><td style="height: 30.125px;">int</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Loại dòng hàng (1: HHDV, 2: Khuyến mãi, 3: Chiết khấu...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">8</td><td style="height: 30.125px;">quantity</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số lượng hàng hóa, dịch vụ</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">9</td><td style="height: 30.125px;">item\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mã hàng hóa, dịch vụ từ hệ thống gốc</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">10</td><td style="height: 30.125px;">item\_name</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">500</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tên hàng hóa, dịch vụ hiển thị trên hóa đơn</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">11</td><td style="height: 30.125px;">unit</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Đơn vị tính (Cái, Chiếc, Kg...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">12</td><td style="height: 30.125px;">price</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Đơn giá hàng hóa (Chưa thuế)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">13</td><td style="height: 30.125px;">gross\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Thành tiền hàng hóa (quantity \* price)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">14</td><td style="height: 30.125px;">discount\_rate</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tỷ lệ chiết khấu (%)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">15</td><td style="height: 30.125px;">discount\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số tiền chiết khấu của dòng hàng</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">16</td><td style="height: 30.125px;">net\_price\_vat</td><td style="height: 30.125px;">decimal </td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Đơn giá đã bao gồm thuế (Thanh toán / Số lượng)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">17</td><td style="height: 30.125px;">net\_price</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Đơn giá sau chiết khấu, trước thuế</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">18</td><td style="height: 30.125px;">net\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Thành tiền sau chiết khấu, trước thuế</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">19</td><td style="height: 30.125px;">tax\_type\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Mã loại thuế suất (Tham chiếu category\_tax\_type)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">20</td><td style="height: 30.125px;">tax\_rate</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Thuế suất thực tế (%)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">21</td><td style="height: 30.125px;">tax\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Số tiền thuế GTGT của dòng hàng</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">22</td><td style="height: 30.125px;">total\_amount</td><td style="height: 30.125px;">decimal</td><td style="height: 30.125px;">15,2</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Tổng cộng tiền thanh toán của dòng hàng (sau thuế)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">23</td><td style="height: 30.125px;">notes</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">500</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ghi chú hoặc diễn giải chi tiết cho dòng hàng</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">24</td><td style="height: 30.125px;">created\_by</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Người tạo dòng hàng</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">25</td><td style="height: 30.125px;">updated\_by</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Người cập nhật dòng hàng</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">26</td><td style="height: 30.125px;">created\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày tạo bản ghi (dd/MM/YYYY hhmmss)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">27</td><td style="height: 30.125px;">updated\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày cập nhật bản ghi (dd/MM/YYYY hhmmss)</td></tr></tbody></table>

<div id="bkmrk-field-name-re-%2A-data-1"></div>

## **4. Mô tả màn hình**

#### **4.1. Màn hình: Xem danh sách HĐĐT**

Màn hình này là trung tâm điều hành, cho phép người dùng thực hiện theo dõi trạng thái và thực hiện các thao tác nhanh trên danh sách hóa đơn.

 **4.1.1. Bộ lọc tìm kiếm**

 Khu vực bộ lọc nằm ở phía trên cùng của màn hình, hỗ trợ người dùng thu hẹp phạm vi tìm kiếm dữ liệu.

<table border="1" id="bkmrk-component-input-type" style="border-collapse: collapse; width: 100%; height: 30.125px;"><colgroup><col style="width: 5.25591%;"></col><col style="width: 21.6845%;"></col><col style="width: 16.2119%;"></col><col style="width: 56.8609%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td>**STT**</td><td style="height: 30.125px;">**Component**</td><td style="height: 30.125px;">**Input Type**</td><td style="height: 30.125px;">**Description**</td></tr><tr><td>1</td><td>Mẫu số (invoice\_form)</td><td>Combobox</td><td>Lọc theo ký hiệu mẫu số hóa đơn </td></tr><tr><td>2</td><td>Ký hiệu (invoice\_series)</td><td>Combobox</td><td>Lọc theo ký hiệu hóa đơn </td></tr><tr><td>3</td><td>Số hóa đơn (invoice\_no)</td><td>Textbox</td><td>Tìm chính xác số hóa đơn</td></tr><tr><td>4</td><td>Ngày hóa đơn (invoice\_date)</td><td>DatePicker</td><td>Khoảng thời gian lập hóa đơn (Từ ngày - Đến ngày)</td></tr><tr><td>5</td><td>Người mua (buyer\_full\_name)</td><td>Textbox</td><td>Tìm theo tên cá nhân hoặc tên đơn vị mua hàng</td></tr><tr><td>6</td><td>Mả số thuế (buyer\_tax\_code)</td><td>Textbox</td><td>Tìm theo MST của đơn vị mua hàng</td></tr><tr><td>7</td><td>Trạng thái (status\_id)</td><td>Combobox</td><td>Lọc theo tình trạng phát hành (Chờ ký, Đã phát hành, Thay thế...)</td></tr></tbody></table>

 **4.1.2. Thanh tác vụ**

 Các nút chức năng được bố trí phía trên lưới dữ liệu để thực hiện các nghiệp vụ theo lô hoặc khởi tạo mới các tác vụ nghiệp vụ.

<div class="horizontal-scroll-wrapper" id="bkmrk-stt-function-button-"><div class="table-block-component"><div _ngcontent-ng-c455667023="" class="table-block has-export-button is-at-scroll-start is-at-scroll-end"><div _ngcontent-ng-c455667023="" class="table-content not-end-of-paragraph" data-hveid="0" data-ved="0CAAQ3ecQahgKEwjZgrKHgoCTAxUAAAAAHQAAAAAQkAU" decode-data-ved="1" jslog="275421;track:impression,attention" not-end-of-paragraph=""><table border="1" style="border-collapse: collapse; width: 100%; height: 406.524px;"><colgroup><col style="width: 5.96125%;"></col><col style="width: 27.2085%;"></col><col style="width: 66.8601%;"></col></colgroup><tbody><tr style="height: 30.0391px;"><td style="height: 30.0391px;">**<span data-path-to-node="5,0,0,0">STT</span>**</td><td style="height: 30.0391px;">**<span data-path-to-node="5,0,0,0">Function button </span>**</td><td style="height: 30.0391px;">**<span data-path-to-node="5,0,1,0">Description</span>**</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;"><span style="color: rgb(52, 73, 94);">1</span></td><td style="height: 30.0391px;"><span style="color: rgb(52, 73, 94);">Thêm mới</span></td><td style="height: 30.0391px;"><span style="color: rgb(52, 73, 94);">Mở modal form khởi tạo hóa đơn thủ công</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">2</span></td><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">Ký thông điệp</span></td><td style="height: 49.4922px;"><span style="font-size: 11pt; line-height: 115%; font-family: Calibri, sans-serif; color: rgb(52, 73, 94);">Thực hiện ký số tập trung cho các thông điệp dữ liệu gửi lên Cơ quan thuế bằng cách gọi API tới Provider</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">3</span></td><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">Thay thế / Điều chỉnh</span></td><td style="height: 49.4922px;"><span style="font-size: 11pt; line-height: 115%; font-family: Calibri, sans-serif; color: rgb(52, 73, 94);">Nhóm các chức năng xử lý sai sót cho hóa đơn đã ở trạng thái "Đã phát hành" bằng cách gọi API tới Provider</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">4</span></td><td style="height: 49.4922px;"><span style="color: rgb(52, 73, 94);">Cập nhật thông tin - đồng bộ</span></td><td style="height: 49.4922px;"><span style="font-size: 11pt; line-height: 115%; font-family: Calibri, sans-serif; color: rgb(52, 73, 94);">Thực hiện gọi API để lấy trạng thái mới nhất từ Cơ quan thuế thông qua Provider.</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">5</td><td style="height: 49.4922px;">Tạo HĐ từ excel</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Cho phép người dùng tải tệp dữ liệu mẫu để import hóa đơn hàng loạt vào hệ thống.</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">6</td><td style="height: 49.4922px;">Tạo HĐ ĐCCK cuối kỳ</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Nghiệp vụ đặc thù để lập hóa đơn điều chỉnh chiết khấu thương mại vào cuối kỳ kế toán</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">7</td><td style="height: 49.4922px;">Kết xuất excel / Sao lưu</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Trích xuất toàn bộ dữ liệu đang hiển thị trên danh sách ra tệp .xlsx</span><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"> hoặc tệp định dạng sao lưu.</span></td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">8</td><td style="height: 49.4922px;">Bảng kê</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Hiển thị và quản lý danh sách các bảng kê đính kèm đối với hóa đơn có nhiều danh mục hàng hóa</span></td></tr></tbody></table>

</div></div></div></div> **4.1.3. Lưới dữ liệu**

 Màn hình hiển thị danh sách hóa đơn theo các cột thông tin sau:

<table border="1" id="bkmrk-column-description-m" style="border-collapse: collapse; width: 100%; height: 727.379px;"><colgroup><col style="width: 6.44465%;"></col><col style="width: 17.8732%;"></col><col style="width: 55.7881%;"></col><col style="width: 19.7881%;"></col></colgroup><tbody><tr style="height: 30.0174px;"><td>**STT**</td><td style="height: 30.0174px;">**Column**</td><td style="height: 30.0174px;">**Description**</td><td style="height: 30.0174px;">**Mapping Field**</td></tr><tr style="height: 46.8056px;"><td class="align-left">1</td><td style="height: 46.8056px;">STT</td><td style="height: 46.8056px;">Số thứ tự tăng dần của các bản ghi trên lưới hiển thị tại trang hiện tại.</td><td style="height: 46.8056px;">(Auto-gen)</td></tr><tr style="height: 46.8056px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">2</span>

</td><td style="height: 46.8056px;"><span style="color: rgb(52, 73, 94);">Mẫu số</span>

</td><td style="height: 46.8056px;"><span style="color: rgb(52, 73, 94);">Mẫu số định danh pháp lý của hóa đơn theo quy định của Cơ quan Thuế.</span></td><td style="height: 46.8056px;"><span style="color: rgb(52, 73, 94);">invoice\_form</span>

</td></tr><tr style="height: 46.6667px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">3</span>

</td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Ký hiệu </span>

</td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Ký hiệu định danh pháp lý của hóa đơn theo quy định của Cơ quan Thuế.</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">invoice\_series</span>

</td></tr><tr style="height: 33.8021px;"><td class="align-left">4

</td><td style="height: 33.8021px;">Số HĐ

</td><td style="height: 33.8021px;">Số HĐ: Số thứ tự hóa đơn điện tử chính thức

</td><td style="height: 33.8021px;">invoice\_no

</td></tr><tr style="height: 29.8785px;"><td class="align-left">5

</td><td style="height: 29.8785px;">Ngày hóa đơn

</td><td style="height: 29.8785px;">Ngày hóa đơn: Ngày lập hóa đơn được ghi nhận pháp lý.

</td><td style="height: 29.8785px;">invoice-date

</td></tr><tr style="height: 30.0174px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">6</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Tên người mua </span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Tên người mua: Tên đơn vị hoặc cá nhân mua hàng.</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">buyer\_full\_name</span></td></tr><tr style="height: 46.6667px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">7</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">MST</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">MST: Mã số thuế của người mua dùng để tra cứu và khấu trừ thuế.</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">buyer\_tax\_code</span></td></tr><tr style="height: 46.6667px;"><td class="align-left">8</td><td style="height: 46.6667px;">HTTT</td><td style="height: 46.6667px;">Hình thức thanh toán mà khách hàng sử dụng (TM: Tiền mặt, CK: Chuyển khoản).</td><td style="height: 46.6667px;">payment\_method\_id</td></tr><tr style="height: 46.6667px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">9</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Tiền hàng chiết khấu</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Tổng giá trị chiết khấu thương mại được áp dụng cho toàn bộ các dòng hàng hóa trên hóa đơn.</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">discount\_amount</span></td></tr><tr style="height: 46.6667px;"><td class="align-left">10</td><td style="height: 46.6667px;">Tổng tiền thuế</td><td style="height: 46.6667px;">Tổng số tiền thuế giá trị gia tăng (VAT) được tính dựa trên thuế suất của từng mặt hàng.</td><td style="height: 46.6667px;">tax\_amount</td></tr><tr style="height: 46.6667px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">11</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Tổng thanh toán</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Tổng giá trị thanh toán cuối cùng của hóa đơn, bao gồm Tiền hàng + Thuế - Chiết khấu.</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">total\_amount</span></td></tr><tr style="height: 46.6667px;"><td class="align-left">12</td><td style="height: 46.6667px;">Người tạo / Ngày tạo</td><td style="height: 46.6667px;">Định danh tài khoản nhân viên khởi tạo hóa đơn create\_by và thời điểm hệ thống ghi nhận bản ghi create\_date.</td><td style="height: 46.6667px;">create\_by, create\_date</td></tr><tr style="height: 46.6667px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">13</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Mã cơ quan thuế</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">Chuỗi ký tự định danh duy nhất (MTC) do hệ thống của Cơ quan Thuế cấp sau khi hóa đơn được kiểm tra và chấp nhận.</span></td><td style="height: 46.6667px;"><span style="color: rgb(52, 73, 94);">tax\_authority\_code</span></td></tr><tr style="height: 46.6667px;"><td class="align-left">14</td><td style="height: 46.6667px;">Ghi chú</td><td style="height: 46.6667px;">Các diễn giải bổ sung hoặc lý do thực hiện nghiệp vụ (Lý do điều chỉnh/thay thế cho hóa đơn số...).</td><td style="height: 46.6667px;">org\_invoice\_reason</td></tr><tr style="height: 30.0174px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">15</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Nguồn</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Phương thức dữ liệu đi vào Hub:</span>

<span style="color: rgb(52, 73, 94);">- BWS: Tích hợp qua Web Service.</span>

<span style="color: rgb(52, 73, 94);">- BES: Import bằng tệp Excel.</span>

<span style="color: rgb(52, 73, 94);">- PMKT: Đồng bộ từ phần mềm kế toán.</span>

</td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">provider\_id </span></td></tr><tr style="height: 30.0174px;"><td class="align-left">16</td><td style="height: 30.0174px;">Trạng thái </td><td style="height: 30.0174px;">Tình trạng hiện tại của hóa đơn trong vòng đời (Chờ ký, Đã phát hành, Đã gửi CQT, Hóa đơn bị thay thế...).</td><td style="height: 30.0174px;">status\_id</td></tr><tr style="height: 30.0174px;"><td class="align-left"><span style="color: rgb(52, 73, 94);">17</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Chức năng</span></td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">Nhóm các icon/button thao tác nhanh cho từng dòng:</span>

- <span style="color: rgb(52, 73, 94);">Xem : xem chi tiết hóa đơn</span>
- <span style="color: rgb(52, 73, 94);">Ký: Phát hành hóa đơn</span>
- <span style="color: rgb(52, 73, 94);">Chỉnh sửa: Thay thế, điều chỉnh</span>

</td><td style="height: 30.0174px;"><span style="color: rgb(52, 73, 94);">(Action buttons)</span></td></tr></tbody></table>

 **4.1.4. Màn hình giao diện tham khảo**

[![bkav_invoice_list.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/bkav-invoice-list.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/bkav-invoice-list.jpg)

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-02/scaled-1680-/nwcimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-02/nwcimage.png)

#### **4.2. Màn hình: Xem chi tiết và chỉnh sửa 1 HĐĐT**

Màn hình này xuất hiện khi người dùng chọn "Thêm mới" hoặc "Xem chi tiết" một hóa đơn cụ thể.

 **4.2.1. Thông tin chung và người mua**

 Phần Header của màn hình chứa các thông tin hành chính và định danh khách hàng.

- - **Thông tin chung của hóa đơn**

<table border="1" id="bkmrk-stt-field-name-descr" style="border-collapse: collapse; width: 100%; height: 240.313px;"><colgroup><col style="width: 6.32151%;"></col><col style="width: 20.7381%;"></col><col style="width: 39.2185%;"></col><col style="width: 16.2119%;"></col><col style="width: 17.5232%;"></col></colgroup><tbody><tr style="height: 30.0391px;"><td style="height: 30.0391px;">**STT**</td><td style="height: 30.0391px;">**Field Name**</td><td style="height: 30.0391px;">**Description**</td><td>**Component Type**</td><td style="height: 30.0391px;">**Mapping\_Field**</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">1</td><td style="height: 30.0391px;">Trạng thái hóa đơn</td><td style="height: 30.0391px;">Hiển thị trạng thái hiện tại của hóa đơn (Mới tạo, Chờ ký, Đã phát hành...).

Chế độ: Chỉ đọc (Read-only).

</td><td>Text</td><td style="height: 30.0391px;">status\_id</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">2</td><td style="height: 30.0391px;">Loại hóa đơn</td><td style="height: 30.0391px;">Phân loại hóa đơn (HĐ Giá trị gia tăng, HĐ Bán hàng, Phiếu xuất kho...).</td><td>Combobox</td><td style="height: 30.0391px;">invoice\_type\_id</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">3</td><td style="height: 30.0391px;">Mẫu số</td><td style="height: 30.0391px;">Mẫu số hóa đơn theo đăng ký với cơ quan thuế</td><td>Combobox</td><td style="height: 30.0391px;">invoice\_form</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">4</td><td style="height: 30.0391px;">Ký hiệu</td><td style="height: 30.0391px;">Ký hiệu hóa đơn theo năm phát hành và loại hóa đơn</td><td>Combobox</td><td style="height: 30.0391px;">invoice\_series</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">5</td><td style="height: 30.0391px;">Ngày hóa đơn</td><td style="height: 30.0391px;">Ngày lập hóa đơn hiển thị trên bản in pháp lý.</td><td>DatePicker</td><td style="height: 30.0391px;">invoice\_date</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">6</td><td style="height: 30.0391px;">Số hóa đơn</td><td style="height: 30.0391px;">Số thứ tự háo đơn do hệ thống hoặc Provider cấp sau khi phát hành thành công</td><td>Text</td><td style="height: 30.0391px;">invoice\_no</td></tr><tr style="height: 30.0391px;"><td style="height: 30.0391px;">7</td><td style="height: 30.0391px;">Ghi chú</td><td style="height: 30.0391px;">Ô nhập liệu tự do ghi lại các diễn giải bổ sung hoặc lý do điều chỉnh thay thế</td><td>TextBox</td><td style="height: 30.0391px;">org\_invoice\_reason</td></tr></tbody></table>

- - **Thông tin người mua và hình thức nhận hóa đơn**

<table border="1" id="bkmrk-stt-field-name-descr-1" style="border-collapse: collapse; width: 100%; height: 573.368px;"><colgroup><col style="width: 6.5596%;"></col><col style="width: 20.5%;"></col><col style="width: 39.2185%;"></col><col style="width: 16.2119%;"></col><col style="width: 17.5232%;"></col></colgroup><tbody><tr style="height: 30.0174px;"><td style="height: 30.0174px;">**STT**</td><td style="height: 30.0174px;">**Field Name**</td><td style="height: 30.0174px;">**Description**</td><td style="height: 30.0174px;">**Component Type**</td><td style="height: 30.0174px;">**Mapping\_Field**</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">1</td><td style="height: 46.6667px;">Thanh tìm kiếm (Search)</td><td style="height: 46.6667px;">Thanh công cụ hỗ trợ tìm nhanh khách hàng từ danh mục dựa trên MST hoặc Tên đơn vị.</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">(UI Component)</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">2</td><td style="height: 46.6667px;">Mã người mua</td><td style="height: 46.6667px;">Mã định danh nội bộ của khách hàng trên hệ thống PosORA.</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_id\_no</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">3</td><td style="height: 46.6667px;">Tên người mua</td><td style="height: 46.6667px;">Tên cá nhân hoặc Tên đơn vị mua hàng (Trường bắt buộc)</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_company, buyer\_fullname</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">4</td><td style="height: 46.6667px;">MST / CCCD</td><td style="height: 46.6667px;">Mã số thuế đơn vị hoặc Số định danh cá nhân của người mua.</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_tax\_code, buyer\_id\_no</td></tr><tr style="height: 30.0174px;"><td style="height: 30.0174px;">5</td><td style="height: 30.0174px;">Mã ngân sách</td><td style="height: 30.0174px;">Mã đơn vị quan hệ ngân sách </td><td style="height: 30.0174px;">TextBox</td><td style="height: 30.0174px;">buyer\_budget\_code</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">6</td><td style="height: 46.6667px;">Sổ hộ khẩu / Định danh</td><td style="height: 46.6667px;">Thông tin bổ sung về nơi cư trú hoặc số định danh khác </td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_id\_no</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">7</td><td style="height: 46.6667px;">Địa chỉ</td><td style="height: 46.6667px;">Địa chỉ giao dịch hoặc địa chỉ thường trú của khách hàng</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_address</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">8</td><td style="height: 46.6667px;">Số TK ngân hàng</td><td style="height: 46.6667px;">Số tài khoản ngân hàng của khách hàng dùng để hiển thị trên hóa đơn.</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_bank\_account</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">9</td><td style="height: 46.6667px;">Tên ngân hàng</td><td style="height: 46.6667px;">Tên ngân hàng tương ứng với số tài khoản của khách hàng</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">buyer\_bank\_name</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">10</td><td style="height: 46.6667px;">Hình thức thanh toán</td><td style="height: 46.6667px;">Lựa chọn phương thức thanh toán (Tiền mặt, Chuyển khoản,iPay, Momo...)</td><td style="height: 46.6667px;">Combobox</td><td style="height: 46.6667px;">payment\_method\_id</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">11</td><td style="height: 46.6667px;">Hình thức nhận HĐ</td><td style="height: 46.6667px;">Cấu hình kênh gửi hóa đơn tự động (1: Email, 2: SMS, ...)</td><td style="height: 46.6667px;">Combobox</td><td style="height: 46.6667px;">receive\_type\_id</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">12</td><td style="height: 46.6667px;">Thông tin nhận HĐ</td><td style="height: 46.6667px;">Địa chỉ Email hoặc Số điện thoại di động nhận hóa đơn của người mua</td><td style="height: 46.6667px;">TextBox</td><td style="height: 46.6667px;">received\_email,

buyer\_mobile

</td></tr></tbody></table>

 **4.2.2. Lưới chi tiết hàng hóa**

- - Danh sách các sản phẩm, dịch vụ nằm trong hóa đơn

<table border="1" id="bkmrk-column-description-m-1" style="border-collapse: collapse; width: 100%; height: 445.458px;"><colgroup><col style="width: 6.32561%;"></col><col style="width: 17.9923%;"></col><col style="width: 55.7881%;"></col><col style="width: 19.7881%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td>**STT**</td><td style="height: 30.125px;">**Column**</td><td style="height: 30.125px;">**Description**</td><td style="height: 30.125px;">**Mapping Field**</td></tr><tr style="height: 46.9167px;"><td>1</td><td style="height: 46.9167px;">STT</td><td style="height: 46.9167px;">Số thứ tự tăng dần của các bản ghi trên lưới hiển thị tại trang hiện tại.</td><td style="height: 46.9167px;">line\_no</td></tr><tr style="height: 46.9167px;"><td>2</td><td style="height: 46.9167px;">Tên hàng hóa dịch vụ</td><td style="height: 46.9167px;">Tên diễn giải chi tiết của sản phẩm hoặc nội dung dịch vụ cung cấp.</td><td style="height: 46.9167px;">item\_name</td></tr><tr style="height: 30.125px;"><td>3</td><td style="height: 30.125px;">ĐVT </td><td style="height: 30.125px;">Đơn vị tính của hàng hóa (Cái, Chiếc, Kg, Gói...).</td><td style="height: 30.125px;">unit</td></tr><tr style="height: 30.125px;"><td>4</td><td style="height: 30.125px;">Số lượng </td><td style="height: 30.125px;">Số lượng thực tế của hàng hóa, dịch vụ xuất trên hóa đơn.</td><td style="height: 30.125px;">quantity</td></tr><tr style="height: 30.125px;"><td>5</td><td style="height: 30.125px;">Đơn giá</td><td style="height: 30.125px;">Giá bán của một đơn vị sản phẩm chưa bao gồm thuế GTGT.</td><td style="height: 30.125px;">price</td></tr><tr style="height: 30.125px;"><td>6</td><td style="height: 30.125px;">Thành tiền</td><td style="height: 30.125px;">Giá trị hàng hóa trước thuế và chiết khấu (Số lượng x Đơn giá)</td><td style="height: 30.125px;">gross\_amount</td></tr><tr style="height: 30.125px;"><td>7</td><td style="height: 30.125px;">Thuế suất </td><td style="height: 30.125px;">Mức thuế suất giá trị gia tăng áp dụng (0%, 5%, 8%, 10%, KCT...).</td><td style="height: 30.125px;">tax\_rate</td></tr><tr style="height: 46.9167px;"><td>8</td><td style="height: 46.9167px;">Tiền thuế</td><td style="height: 46.9167px;">Tổng giá trị thuế GTGT của dòng hàng (Số lượng x đơn giá x thuế suất).</td><td style="height: 46.9167px;">tax\_amount</td></tr><tr style="height: 46.9167px;"><td>9</td><td style="height: 46.9167px;">Chức năng</td><td style="height: 46.9167px;">Các icon thao tác trên từng dòng hàng: Sửa (Chỉnh sửa nội dung) / Xóa (Loại bỏ dòng).</td><td style="height: 46.9167px;">(Action icons)</td></tr></tbody></table>

- - Phần Footer của lưới hiển thị các ô tổng hợp, khu vực này tổng hợp các giá trị tài chính của hóa đơn, hỗ trợ người dùng đối soát nhanh trước khi thực hiện ký số.

<table border="1" id="bkmrk-stt-column-descripti" style="border-collapse: collapse; width: 100%; height: 308.945px;"><colgroup><col style="width: 6.43709%;"></col><col style="width: 17.8808%;"></col><col style="width: 38.4962%;"></col><col style="width: 17.2919%;"></col><col style="width: 19.9073%;"></col></colgroup><tbody><tr style="height: 30.1172px;"><td style="height: 30.1172px;">**STT**</td><td style="height: 30.1172px;">**Column**</td><td style="height: 30.1172px;">**Description**</td><td>**Component Type**</td><td style="height: 30.1172px;">**Mapping Field**</td></tr><tr style="height: 31.3672px;"><td style="height: 31.3672px;">1</td><td style="height: 31.3672px;">Tiền tệ</td><td style="height: 31.3672px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Mã loại tiền tệ sử dụng trên hóa đơn (VND, USD, EUR...)</span></td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Combobox</span></td><td style="height: 31.3672px;">currency\_code</td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">2</td><td style="height: 49.4922px;">Tỷ giá </td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Tỷ giá quy đổi tại thời điểm lập hóa đơn (chỉ áp dụng nếu tiền tệ khác VND).</span></td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Textbox</span></td><td style="height: 49.4922px;">exchange\_rate</td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">3</td><td style="height: 49.4922px;">Cộng tiền hàng</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Tổng giá trị hàng hóa, dịch vụ trên tất cả các dòng (chưa thuế và chiết khấu).</span></td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Text</span></td><td style="height: 49.4922px;">gross\_amount</td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">4</td><td style="height: 49.4922px;">Tiền chiết khấu</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Tổng giá trị chiết khấu thương mại được giảm trừ trực tiếp trên hóa đơn</span>.</td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Text</span></td><td style="height: 49.4922px;">discount\_amount</td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">5</td><td style="height: 49.4922px;">Tiền thuế</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Tổng số tiền thuế Giá trị gia tăng (VAT) tính theo các mức thuế suất tương ứng</span></td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Text</span></td><td style="height: 49.4922px;">tax\_amount</td></tr><tr style="height: 49.4922px;"><td style="height: 49.4922px;">6</td><td style="height: 49.4922px;">Tổng tiền thanh toán</td><td style="height: 49.4922px;"><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Giá trị cuối cùng khách hàng phải trả (gross\_amount + tax\_amount - discount\_amount</span></td><td><span style="font-size: 11.0pt; line-height: 115%; font-family: 'Calibri',sans-serif; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: Calibri; mso-fareast-theme-font: minor-latin; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Text</span></td><td style="height: 49.4922px;">total\_amount</td></tr></tbody></table>

 **4.2.3. Màn hình giao diện tham khảo**

**[![bkav_edit_invoice.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/bkav-edit-invoice.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/bkav-edit-invoice.jpg)**


## **5. Mô tả Chức năng**

#### **5.1. Chức năng - Cấp số hóa đơn**

Chức năng này cho phép người dùng gửi thông tin hóa đơn nháp lên hệ thống Nhà cung cấp (Provider) để xác định Số hóa đơn và Ký hiệu trước khi thực hiện ký số.

- **Điều kiện thực hiện:** <span style="color: rgb(45, 194, 107);">Hóa đơn phải ở trạng thái "Mới tạo" (status\_id = 0)</span>
- **Luồng xử lý:**
    
    
    1. Người dùng chọn hóa đơn trên Danh sách và nhấn nút "Cấp số".
    2. Hệ thống kiểm tra cấu hình tích hợp PartnerGUID, Token.
    3. Gửi bản tin đến Provider:
        
        
        - **BKAV:** Sử dụng mã lệnh CmdType 101 (Tạo hóa đơn và để Bkav tự cấp số hóa đơn) hoặc CmdType 111 (Tạo hóa đơn với Mẫu số/Ký hiệu/Số HĐ do PMKT cấp/chỉ định).
        - **MobiFone:** Gọi endpoint {{base\_url}}/api/Invoice68/SaveListHoadon78 với tham số Editmode = 1.
    4. Provider trả về kết quả thành công Status = 0
    5. Hệ thống cập nhật invoice\_no, invoice\_series, invoice\_form và đổi trạng thái sang status\_id = 1 (Đã cấp số, chờ ký).
- **Dữ liệu Input/Output:**
    
    
    - Input: PartnerInvoiceID, dữ liệu Header và Detail của hóa đơn.
    - Output: InvoiceNo, InvoiceSeries, InvoiceLookupCode

#### **5.2. Chức năng - Ký hóa đơn**

Thực hiện ký số phát hành hóa đơn chính thức để gửi lên Cơ quan Thuế.

- **Điều kiện thực hiện:** <span style="color: rgb(45, 194, 107);">Chỉ các hóa đơn ở trạng thái "Chờ ký" (status\_id = 1, 5, 7) mới được thực hiện chức năng này</span>
- **Luồng xử lý:**
    
    
    1. Hệ thống xác định phương thức ký (HSM).
    2. Gửi yêu cầu ký số:
        
        
        - **BKAV:** Sử dụng mã lệnh **CmdType 205** (Ký hóa đơn bằng HSM).
        - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/SaveAndSignHoadon78.
    3. Sau khi ký thành công, Provider thực hiện cấp Mã của cơ quan thuế (MTC).
    4. Hub nhận phản hồi, cập nhật signed\_date và tax\_authority\_code
    5. Chuyển trạng thái hóa đơn sang "Đã phát hành" (status\_id = 2, 6, 8)
- **Dữ liệu Input/Output:**
    
    
    - Input: InvoiceID (hoặc PartnerInvoiceID).
    - Output: SignedDate, TaxAuthorityCode, UrlLookup.

#### **5.3. Chức năng - Tạo hóa đơn thay thế**

Nghiệp vụ áp dụng khi hóa đơn đã phát hành có sai sót và cần hủy bỏ để thay bằng hóa đơn mới.

- **Điều kiện thực hiện:<span style="color: rgb(45, 194, 107);"> C</span>**<span style="color: rgb(45, 194, 107);">hỉ hóa đơn trạng thái "Đã phát hành (Đã ký)" (status\_id = 2, 6, 8) mới được thực hiện Thay thế</span>.
- **Quy tắc chọn hóa đơn gốc:** Hệ thống hiển thị danh sách hóa đơn đã phát hành để người dùng chọn làm hóa đơn gốc ().
- **Luồng xử lý:**
    
    
    1. Người dùng thực hiện lệnh "Thay thế" từ hóa đơn gốc.
    2. Hệ thống tạo bản ghi mới trong Hub, gán reference\_type\_id = 3 và liên kết với hóa đơn gốc qua org\_invoice\_id.
    3. Gửi bản tin thay thế:
        
        
        - **BKAV:** Sử dụng mã lệnh **CmdType 123** (Thay thế hóa đơn, Bkav cấp số mới).
        - **MobiFone:** Gọi {{base\_url}}/api/Invoice68/SaveListHoadon78 với Editmode = 3.
    4. Provider thực hiện hủy hóa đơn gốc và phát hành hóa đơn thay thế.
    5. Cập nhật trạng thái hóa đơn gốc thành status\_id = 9 (Bị thay thế).
- **Dữ liệu Input/Output:**
    
    
    - Input: Thông tin hóa đơn gốc (OrgInvNo, OrgInvDate) và nội dung hóa đơn mới.
    - Output: InvoiceNo mới, liên kết quan hệ giữa hai hóa đơn.

#### **5.4. Chức năng - Điều chỉnh hóa đơn**

Nghiệp vụ điều chỉnh tăng, giảm hoặc điều chỉnh thông tin cho hóa đơn đã phát hành sai sót.

- **Điều kiện thực hiện:**<span style="color: rgb(45, 194, 107);"> Chỉ hóa đơn trạng thái "Đã phát hành" (status\_id = 2, 6, 8) mới được thực hiện Điều chỉnh.</span>
- **Quy tắc truyền dữ liệu:**
    
    
    - **OriginalInvoiceIdentify:** Bắt buộc truyền thông tin định danh hóa đơn gốc (Mẫu số, Ký hiệu, Số HĐ).
    - **IsIncrease:** Xác định tính chất điều chỉnh trên từng dòng hàng hóa:
        
        
        - True: Điều chỉnh tăng
        - False: Điều chỉnh giảm.
        - Null: Điều chỉnh thông tin (không thay đổi giá trị số).
- **Luồng xử lý:**
    
    
    1. Hệ thống bán hàng gửi bản tin điều chỉnh kèm tham số IsIncrease.
    2. Hub tiếp nhận và đóng gói bản tin:
        
        
        - **BKAV:** Sử dụng mã lệnh CmdType 124 (Điều chỉnh hóa đơn, Bkav cấp số).
        - **MobiFone:** Gọi {{base\_url}}/api/Invoice68/SaveListHoadon78 với Editmode = 2.
    3. Provider cấp số hóa đơn điều chỉnh và trả về.
    4. Cập nhật hóa đơn gốc thành status\_id = 10 (Bị điều chỉnh).
- **Dữ liệu Input/Output:**
    
    
    - Input: OriginalInvoiceIdentify, IsIncrease, giá trị chênh lệch.
    - Output: InvoiceNo điều chỉnh, InvoiceStatusID mới.

#### **5.5. Chức năng - Cập nhật thông tin**

Đồng bộ trạng thái thực tế của hóa đơn từ hệ thống Cơ quan Thuế/Provider về hệ thống nội bộ.

- **Điều kiện thực hiện:** <span style="color: rgb(45, 194, 107);">Áp dụng cho mọi hóa đơn đã gửi lên Provider nhưng chưa có kết quả cuối cùng từ CQT</span>
- **Luồng xử lý:**
    
    
    1. Hub khởi tạo tiến trình Sync định kỳ hoặc theo yêu cầu người dùng.
    2. Gửi yêu cầu tra cứu:
        
        
        - **BKAV:** Sử dụng mã lệnh CmdType 801 (Lấy chi tiết trạng thái từ CQT).
        - **MobiFone:** Gọi endpoint {{base\_url}}/api/Invoice68/GetHoadon78ByPartnerInvoiceID.
    3. Nhận phản hồi về trạng thái thuế (TaxStatusID) và mã giao dịch.
    4. Cập nhật lại status\_id và các thông tin định danh còn thiếu vào database Hub.
- **Dữ liệu Input/Output:**
    
    
    - Input: PartnerInvoiceID và StoreConfig.
    - Output: Trạng thái mới nhất status\_id và mã tax\_authority\_code