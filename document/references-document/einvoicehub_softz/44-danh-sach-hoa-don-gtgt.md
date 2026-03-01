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
        - **Step 1:** Người dùng chọn bản ghi trên danh sách và nhấn button chức năng thực hiện tác vụ nghiệp vụ tùy theo mục đích như : Tạo/ Cấp số, Ký, Điều chỉnh, Thay thế hoặc Hủy.
        - **Step 2:** eInvoive thực hiện build bản tin nghiệp vụ và lưu bản tin tạm vào database để tracking tiến trình xử lý. Tại bước này, hệ thống xác định loại lệnh CmdType của Provider tương ứng với yêu cầu từ hệ thống bán hàng
        - **Step 3:** Thực hiện gọi API của Provider dựa trên mã lệnh đã xác định (theo D. Phụ Lục tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") ) 
            - Tác vụ Cấp số/ Tạo hóa đơn: Sử dụng CmdType: 101 . Thêm mới hóa đơn và để Bkav tự cấp số hóa đơn.
            - Tác vụ Ký số (HSM): Sử dụng CmdType: 205 . Ký hóa đơn bằng chữ ký số HSM (áp dụng khi hóa đơn đã có số và ở trạng thái chờ ký).
            - Tác vụ Điều chỉnh: Sử dụng CmdType: 124 . Điều chỉnh hóa đơn và để Bkav cấp số hóa đơn mới.
            - Tác vụ Thay thế: Sử dụng CmdType: 123. Thay thế hóa đơn và để Bkav cấp số hóa đơn mới.
            - Tác vụ Hủy hóa đơn: Sử dụng CmdType: 300 . Thực hiện hủy hóa đơn đã phát hành trên hệ thống.
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

#### **2.2. Function Code**

 **2.2.1. Pseudo Code: Thuật toán xử lý chung**

 Thuật toán này áp dụng cho các tác vụ thay đổi trạng thái hóa đơn trên màn hình danh sách hóa đơn

> Pseudo Code:
> 
> **FUNCTION processInvoiceOperation(invoiceId, actionType):**  
>  // 1. Lấy thông tin hóa đơn từ Database  
>   **invoice = DB.findInvoiceById(invoiceId)**  // hóa đơn lấy từ database qua id hóa đơn  
>   **IF invoice NOT FOUND THEN RETURN Error("Hóa đơn không tồn tại")**
> 
>  // 2. Kiểm tra điều kiện trạng thái (Validation)   
>  // chỉ thực hiện hành động nếu trạng thái hóa đơn hiện tại cho phép hành động thực hiện  
>   **IF NOT isActionAllowed(invoice.status\_id, actionType) THEN**   
>  **RETURN Error("Trạng thái hóa đơn không hợp lệ cho tác vụ này")**
> 
>  // 3. Xác định Provider và Cấu hình tích hợp  
>   **provider = invoice.getProvider()** // lấy thông tin Provider   
>  **config = Hub.getIntegrationConfig(invoice.store\_id)** //lấy cấu hình tích hợp hệ thống bán hàng của hóa đơn
> 
>  // 4. Gọi API tương ứng theo tác vụ (Chi tiết tại 2.2.2)  
>   **response = ProviderService.callAPI(provider, actionType, invoice, config)** //gọi API tới Provider truyền tham số hành động (cấp số, ký, điều chỉnh,...) , hóa đơn, cấu hình lưu vào biến phản hồi
> 
>  // 5. Cập nhật Cơ sở dữ liệu  
>   **IF response.isSuccess():**  
>  **updateInvoiceData(invoice, response)** // cập nhật dữ liệu hóa đơn nếu trạng thái phản hồi thành công  
>  **logHistory(invoiceId, actionType, "Success")** // Lưu lịch sử  
>  **RETURN Success("Thực hiện thành công")**  
>  **ELSE:**  
>  **logError(invoiceId, actionType, response.error)** // phản hồi lỗi nếu Provider phản hồi lỗi  
>  **RETURN Error("Thất bại: " + response.errorMessage)**

 **2.2.2. Xử lý API cho từng tác vụ cụ thể**

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
        - **Hủy hóa đơn:**
            - **BKAV:** Sử dụng mã lệnh CmdType: 300 (Thực hiện hủy hóa đơn trên hệ thống).
            - **MobiFone:**
        - **Đồng bộ (Cập nhật thông tin hóa đơn):**
            - **BKAV:** Sử dụng mã lệnh CmdType: 801 (Lấy chi tiết thông tin hóa đơn và Trạng thái phản hồi từ Cơ quan Thuế).
            - **MobiFone:** Sử dụng endpoint {{base\_url}}/api/Invoice68/GetHoadon78ByPartnerInvoiceID để truy vấn lại trạng thái và thông tin hóa đơn dựa trên ID định danh từ hệ thống đối tác.

 **2.2.3. Mã Java SpringBoot dành cho Service Layer**

 Thuật toán này áp dụng cho các tác vụ thay đổi trạng thái hóa đơn trên màn hình danh sách hóa đơn

> Java
> 
> **@Service**  
> **public class InvoiceActionService {**
> 
>  **@Autowired**  
>  **private EinvInvoiceRepository invoiceRepository;**
> 
>  **@Autowired**  
>  **private ProviderFactory providerFactory;**
> 
>  **@Transactional**  
>  **public ProviderResponse processInvoiceOperation(Long invoiceId, ActionType action) {**  
>   // 1. Kiểm tra hóa đơn và trạng thái  
>  **EinvInvoice invoice = invoiceRepository.findById(invoiceId)**  // hóa đơn lấy từ database qua id hóa đơn  
>  **.orElseThrow(() -&gt; new BusinessException("Hóa đơn không tồn tại"));**
> 
>  **validateStatus(invoice.getStatusId(), action);**
> 
>   // 2. Lấy thông tin Provider , lấy cấu hình tích hợp hệ thống bán hàng của hóa đơn  
>  **IProviderStrategy strategy = providerFactory.getStrategy(invoice.getProviderCode());**
> 
>   // 3. Build Request DTO  
>  **InvoiceRequest request = buildRequest(invoice, action);**
> 
>   // 4. Gọi Provider và nhận phản hồi  
>  **ProviderResponse response = strategy.execute(request);** //gọi API tới Provider truyền tham số hành động (cấp số, ký, điều chỉnh,...) , hóa đơn, cấu hình lưu vào biến phản hồi
> 
>   // 5. Xử lý cập nhật DB nếu thành công  
>  **if (response.isSuccess()) {**  // cập nhật dữ liệu hóa đơn nếu trạng thái phản hồi thành công  
>  **invoice.setStatusId(response.getNewStatusId());**   
>  **invoice.setInvoiceNo(response.getInvoiceNo());**  
>  **invoice.setInvoiceSeries(response.getInvoiceSeries());**  
>  **invoice.setInvoiceLookupCode(response.getLookupCode());**  
>  **invoiceRepository.save(invoice);**  
>  **}**
> 
>  **return response;**   
>  **}**
> 
>  **private void validateStatus(Integer currentStatus, ActionType action) {**  
>  // Quy tắc nghiệp vụ  
>  // chỉ thực hiện hành động nếu trạng thái hóa đơn hiện tại cho phép hành động thực hiện  
>  **if (action == ActionType.SIGN &amp;&amp; !Arrays.asList(1, 5, 7).contains(currentStatus)) {**  
>  **throw new BusinessException("Chỉ hóa đơn Chờ ký mới được thực hiện ký số.");**  
>  **}**  
>  **if ((action == ActionType.ADJUST || action == ActionType.REPLACE)**   
>  **&amp;&amp; !Arrays.asList(2, 6, 8).contains(currentStatus)) {**  
>  **throw new BusinessException("Chỉ hóa đơn Đã phát hành mới được Điều chỉnh/Thay thế.");**  
>  **}**  
>  **}**  
> **}**

## **3. Cơ sở dữ liệu**

Phần này đặc tả cấu trúc lưu trữ dữ liệu cho các hóa đơn điện tử

#### **3.1. Table: einv\_invoices: lưu danh sách HĐĐT**

<table border="1" id="bkmrk-no.-field-name-type-" style="border-collapse: collapse; width: 100%; height: 1506.9px;"><colgroup><col style="width: 6.20032%;"></col><col style="width: 20.7472%;"></col><col style="width: 10.1351%;"></col><col style="width: 10.0159%;"></col><col style="width: 10.2544%;"></col><col style="width: 8.10811%;"></col><col style="width: 34.5787%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td class="xl72" height="21" style="height: 30.125px;" width="30">**No.**</td><td class="xl72" style="height: 30.125px;" width="139">**Field Name**</td><td class="xl72" style="height: 30.125px;" width="82">**Type**</td><td class="xl72" style="height: 30.125px;" width="51">**Length**</td><td class="xl72" style="height: 30.125px;" width="66">**Not null**</td><td class="xl72" style="height: 30.125px;" width="32">**Key**</td><td class="xl72" style="height: 30.125px;" width="243">**Description**</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">1</td><td style="height: 30.125px;">id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">Yes</td><td style="height: 30.125px;">PK</td><td style="height: 30.125px;">Khóa chính (UUID v7)</td></tr><tr style="height: 30.625px;"><td style="height: 30.625px;">2</td><td style="height: 30.625px;">tenant\_id</td><td style="height: 30.625px;">varchar</td><td style="height: 30.625px;">36</td><td style="height: 30.625px;">No</td><td style="height: 30.625px;">  
</td><td style="height: 30.625px;">ID của Tenant (Khách hàng)</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">3</td><td style="height: 46.6667px;">store\_id</td><td style="height: 46.6667px;">varchar</td><td style="height: 46.6667px;">36</td><td style="height: 46.6667px;">No</td><td style="height: 46.6667px;">  
</td><td style="height: 46.6667px;">ID của Cửa hàng/Chi nhánh phát hành</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">4</td><td style="height: 46.9167px;">partner\_invoice\_id</td><td style="height: 46.9167px;">varchar</td><td style="height: 46.9167px;">50</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">ID hóa đơn từ hệ thống cửa hàng/ chi nhánh phát hành (Receipt ID)</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">5</td><td style="height: 46.9167px;">provider\_id</td><td style="height: 46.9167px;">varchar</td><td style="height: 46.9167px;">36</td><td style="height: 46.9167px;">No</td><td style="height: 46.9167px;">  
</td><td style="height: 46.9167px;">ID nhà cung cấp HĐĐT (BKAV, MobiFone, MISA, VNPT, Viettel,...)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">6</td><td style="height: 30.125px;">provider\_invoice\_id</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">ID hóa đơn do phía Provider cấp.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">7</td><td style="height: 30.125px;">invoice\_type\_id</td><td style="height: 30.125px;">int</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Loại hóa đơn (1: GTGT, 2: Bán hàng, 6: PXK...)</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">8</td><td style="height: 46.6667px;">reference\_type\_id</td><td style="height: 46.6667px;">int</td><td style="height: 46.6667px;">  
</td><td style="height: 46.6667px;">No</td><td style="height: 46.6667px;">  
</td><td style="height: 46.6667px;">Tính chất (0: Gốc, 2: Điều chỉnh, 3: Thay thế,...)</td></tr><tr style="height: 52.5208px;"><td style="height: 52.5208px;">9</td><td style="height: 52.5208px;">status\_id</td><td style="height: 52.5208px;">int</td><td style="height: 52.5208px;">  
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
</td><td style="height: 30.125px;">Họ tên người mua.</td></tr><tr style="height: 28.125px;"><td style="height: 28.125px;">19</td><td style="height: 28.125px;">buyer\_address</td><td style="height: 28.125px;">varchar</td><td style="height: 28.125px;">300</td><td style="height: 28.125px;">No</td><td style="height: 28.125px;">  
</td><td style="height: 28.125px;">Địa chỉ người mua.</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">20</td><td style="height: 30.125px;">buyer\_mobile</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
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
</td><td style="height: 30.125px;">Ngày phát hành hóa đơn (Ký số)</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">29</td><td style="height: 30.125px;">tax\_authority\_code</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">50</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
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
</td><td style="height: 30.125px;">Người lập hóa đơn</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">43</td><td style="height: 30.125px;">updated\_by</td><td style="height: 30.125px;">varchar</td><td style="height: 30.125px;">36</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Người cập nhật hóa đơn</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">44</td><td style="height: 30.125px;">created\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày lập hóa đơn</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">45</td><td style="height: 30.125px;">updated\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày cập nhật hóa đơn gần nhất</td></tr></tbody></table>

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
</td><td style="height: 30.125px;">Ngày tạo bản ghi</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">27</td><td style="height: 30.125px;">updated\_date</td><td style="height: 30.125px;">datetime</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">No</td><td style="height: 30.125px;">  
</td><td style="height: 30.125px;">Ngày cập nhật bản ghi</td></tr></tbody></table>

<div id="bkmrk-field-name-re-%2A-data-1"></div>

## **4. Mô tả màn hình**

#### **4.1. Màn hình: Xem danh sách HĐĐT**

Màn hình này là trung tâm điều hành, cho phép người dùng thực hiện theo dõi trạng thái và thực hiện các thao tác nhanh trên danh sách hóa đơn.

 **4.1.1. Bộ lọc tìm kiếm**

 Khu vực bộ lọc nằm ở phía trên cùng của màn hình, hỗ trợ người dùng thu hẹp phạm vi tìm kiếm dữ liệu.

<table border="1" id="bkmrk-component-input-type" style="border-collapse: collapse; width: 100%; height: 30.125px;"><colgroup><col style="width: 26.9475%;"></col><col style="width: 16.209%;"></col><col style="width: 56.8832%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td style="height: 30.125px;">**Component**</td><td style="height: 30.125px;">**Input Type**</td><td style="height: 30.125px;">**Description**</td></tr><tr><td>Mẫu số (invoice\_form)</td><td>Combobox</td><td>Lọc theo ký hiệu mẫu số hóa đơn </td></tr><tr><td>Ký hiệu (invoice\_series)</td><td>Combobox</td><td>Lọc theo ký hiệu hóa đơn </td></tr><tr><td>Số hóa đơn (invoice\_no)</td><td>Textbox</td><td>Tìm chính xác số hóa đơn</td></tr><tr><td>Ngày hóa đơn (invoice\_date)</td><td>DatePicker</td><td>Khoảng thời gian lập hóa đơn (Từ ngày - Đến ngày)</td></tr><tr><td>Người mua (buyer\_full\_name)</td><td>Textbox</td><td>Tìm theo tên cá nhân hoặc tên đơn vị mua hàng</td></tr><tr><td>Mả số thuế (buyer\_tax\_code)</td><td>Textbox</td><td>Tìm theo MST của đơn vị mua hàng</td></tr><tr><td>Trạng thái (status\_id)</td><td>Combobox</td><td>Lọc theo tình trạng phát hành (Chờ ký, Đã phát hành, Thay thế...)</td></tr></tbody></table>

 **4.1.2. Thanh tác vụ**

 Các nút chức năng được bố trí phía trên lưới dữ liệu để thực hiện các nghiệp vụ theo lô hoặc khởi tạo mới

- - - <span style="color: rgb(45, 194, 107);">**Thêm mới:** Mở màn hình tạo mới hóa đơn thủ công.</span>
        - **Tạo HĐ từ excel:** Cho phép import dữ liệu hóa đơn hàng loạt từ tệp mẫu.
        - <span style="color: rgb(45, 194, 107);">**Ký thông điệp:** Thực hiện ký số cho các thông điệp gửi cơ quan thuế (dành cho HĐ máy tính tiền)</span>
        - <span style="color: rgb(45, 194, 107);">**Báo cáo/Hủy/Thay thế/Điều chỉnh:** Nhóm các tác vụ xử lý sai sót cho hóa đơn đã phát hành.</span>
        - **Tạo HĐ ĐCCK cuối kỳ:** Chức năng đặc thù dành cho hóa đơn điều chỉnh chiết khấu cuối kỳ.
        - **Kết xuất excel / Sao lưu:** Xuất dữ liệu danh sách đang hiển thị ra tệp .xlsx hoặc tệp lưu trữ.
        - **Bảng kê:** Xem danh sách bảng kê đi kèm hóa đơn (nếu có).
        - <span style="color: rgb(45, 194, 107);">**Refresh:** Cập nhật lại dữ liệu mới nhất từ Cơ qun thuế thông qua Provider.</span>

 **4.1.3. Lưới dữ liệu**

 Màn hình hiển thị danh sách hóa đơn theo các cột thông tin sau:

<table border="1" id="bkmrk-column-description-m" style="border-collapse: collapse; width: 100%; height: 727.379px;"><colgroup><col style="width: 24.3353%;"></col><col style="width: 55.8761%;"></col><col style="width: 19.8489%;"></col></colgroup><tbody><tr style="height: 30.0174px;"><td style="height: 30.0174px;">**Column**</td><td style="height: 30.0174px;">**Description**</td><td style="height: 30.0174px;">**Mapping Field**</td></tr><tr style="height: 46.8056px;"><td style="height: 46.8056px;">STT</td><td style="height: 46.8056px;">Số thứ tự tăng dần của các bản ghi trên lưới hiển thị tại trang hiện tại.</td><td style="height: 46.8056px;">(Auto-gen)</td></tr><tr style="height: 46.8056px;"><td style="height: 46.8056px;"><span style="color: rgb(236, 240, 241);">Mẫu số</span>

</td><td style="height: 46.8056px;"><span style="color: rgb(236, 240, 241);">Mẫu số định danh pháp lý của hóa đơn theo quy định của Cơ quan Thuế.</span></td><td style="height: 46.8056px;"><span style="color: rgb(236, 240, 241);">invoice\_form</span>

</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Ký hiệu </span>

</td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Ký hiệu định danh pháp lý của hóa đơn theo quy định của Cơ quan Thuế.</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">invoice\_series</span>

</td></tr><tr style="height: 33.8021px;"><td style="height: 33.8021px;">Số HĐ

</td><td style="height: 33.8021px;">Số HĐ: Số thứ tự hóa đơn điện tử chính thức

</td><td style="height: 33.8021px;">invoice\_no

</td></tr><tr style="height: 29.8785px;"><td style="height: 29.8785px;">Ngày hóa đơn

</td><td style="height: 29.8785px;">Ngày hóa đơn: Ngày lập hóa đơn được ghi nhận pháp lý.

</td><td style="height: 29.8785px;">invoice-date

</td></tr><tr style="height: 30.0174px;"><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Tên người mua </span></td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Tên người mua: Tên đơn vị hoặc cá nhân mua hàng.</span></td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">buyer\_full\_name</span></td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">MST</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">MST: Mã số thuế của người mua dùng để tra cứu và khấu trừ thuế.</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">buyer\_tax\_code</span></td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">HTTT</td><td style="height: 46.6667px;">Hình thức thanh toán mà khách hàng sử dụng (TM: Tiền mặt, CK: Chuyển khoản).</td><td style="height: 46.6667px;">payment\_method\_id</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Tiền hàng chiết khấu</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Tổng giá trị chiết khấu thương mại được áp dụng cho toàn bộ các dòng hàng hóa trên hóa đơn.</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">discount\_amount</span></td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">Tổng tiền thuế</td><td style="height: 46.6667px;">Tổng số tiền thuế giá trị gia tăng (VAT) được tính dựa trên thuế suất của từng mặt hàng.</td><td style="height: 46.6667px;">tax\_amount</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Tổng thanh toán</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Tổng giá trị thanh toán cuối cùng của hóa đơn, bao gồm Tiền hàng + Thuế - Chiết khấu.</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">total\_amount</span></td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">Người tạo / Ngày tạo</td><td style="height: 46.6667px;">Định danh tài khoản nhân viên khởi tạo hóa đơn create\_by và thời điểm hệ thống ghi nhận bản ghi create\_date.</td><td style="height: 46.6667px;">create\_by, create\_date</td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Mã cơ quan thuế</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">Chuỗi ký tự định danh duy nhất (MTC) do hệ thống của Cơ quan Thuế cấp sau khi hóa đơn được kiểm tra và chấp nhận.</span></td><td style="height: 46.6667px;"><span style="color: rgb(236, 240, 241);">tax\_authority\_code</span></td></tr><tr style="height: 46.6667px;"><td style="height: 46.6667px;">Ghi chú</td><td style="height: 46.6667px;">Các diễn giải bổ sung hoặc lý do thực hiện nghiệp vụ (Lý do điều chỉnh/thay thế cho hóa đơn số...).</td><td style="height: 46.6667px;">org\_invoice\_reason</td></tr><tr style="height: 30.0174px;"><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Nguồn</span></td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Phương thức dữ liệu đi vào Hub:</span>

<span style="color: rgb(236, 240, 241);">- BWS: Tích hợp qua Web Service.</span>

<span style="color: rgb(236, 240, 241);">- BES: Import bằng tệp Excel.</span>

<span style="color: rgb(236, 240, 241);">- PMKT: Đồng bộ từ phần mềm kế toán.</span>

</td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">provider\_id </span></td></tr><tr style="height: 30.0174px;"><td style="height: 30.0174px;">Trạng thái </td><td style="height: 30.0174px;">Tình trạng hiện tại của hóa đơn trong vòng đời (Ví dụ: Chờ ký, Đã phát hành, Đã gửi CQT, Hóa đơn bị thay thế...).</td><td style="height: 30.0174px;">status\_id</td></tr><tr style="height: 30.0174px;"><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Chức năng</span></td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">Nhóm các icon/button thao tác nhanh cho từng dòng:</span>

- <span style="color: rgb(236, 240, 241);">Xem : xem chi tiết hóa đơn</span>
- <span style="color: rgb(236, 240, 241);">Ký: Phát hành hóa đơn</span>
- <span style="color: rgb(236, 240, 241);">Chỉnh sửa: Thay thế, điều chỉnh</span>

</td><td style="height: 30.0174px;"><span style="color: rgb(236, 240, 241);">(Action buttons)</span></td></tr></tbody></table>

 **4.1.4. Màn hình giao diện tham khảo**

[![bkav_invoice_list.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/bkav-invoice-list.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/bkav-invoice-list.jpg)

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-02/scaled-1680-/nwcimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-02/nwcimage.png)

#### **4.2. Màn hình: Xem chi tiết và chỉnh sửa 1 HĐĐT**

Màn hình này xuất hiện khi người dùng chọn "Thêm mới" hoặc "Xem chi tiết" một hóa đơn cụ thể.

 **4.2.1. Thông tin chung và người mua**

 Phần Header của màn hình chứa các thông tin hành chính và định danh khách hàng.

- - - **Thông tin chung:** Hiển thị Trạng thái HĐ (Read-only), Loại HĐ, Mẫu số, Ký hiệu, Ngày HĐ, Số HĐ và ô ghi chú.
        - **Thông tin người mua:** Thanh tìm kiếm (Search): Hỗ trợ tìm nhanh khách hàng từ danh mục có sẵn.
            
            
            - **Dữ liệu chi tiết:** Mã người mua, Tên người mua (Bắt buộc), MST/CCCD, Mã ngân sách, Sổ hộ khẩu, Địa chỉ, Số TK ngân hàng, Hình thức thanh toán.
            - **Nhận hóa đơn:** Cấu hình Hình thức nhận (Email/SMS) và thông tin liên lạc tương ứng

 **4.2.2. Lưới chi tiết hàng hóa**

 Danh sách các sản phẩm, dịch vụ nằm trong hóa đơn

<table border="1" id="bkmrk-column-description-m-1" style="border-collapse: collapse; width: 100%; height: 445.458px;"><colgroup><col style="width: 24.3243%;"></col><col style="width: 55.8029%;"></col><col style="width: 19.7933%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td style="height: 30.125px;">**Column**</td><td style="height: 30.125px;">**Description**</td><td style="height: 30.125px;">**Mapping Field**</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">STT</td><td style="height: 46.9167px;">Số thứ tự tăng dần của các bản ghi trên lưới hiển thị tại trang hiện tại.</td><td style="height: 46.9167px;">line\_no</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">Tên hàng hóa dịch vụ</td><td style="height: 46.9167px;">Tên diễn giải chi tiết của sản phẩm hoặc nội dung dịch vụ cung cấp.</td><td style="height: 46.9167px;">item\_name</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">ĐVT </td><td style="height: 30.125px;">Đơn vị tính của hàng hóa (Cái, Chiếc, Kg, Gói...).</td><td style="height: 30.125px;">unit</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">Số lượng </td><td style="height: 30.125px;">Số lượng thực tế của hàng hóa, dịch vụ xuất trên hóa đơn.</td><td style="height: 30.125px;">quantity</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">Đơn giá</td><td style="height: 30.125px;">Giá bán của một đơn vị sản phẩm chưa bao gồm thuế GTGT.</td><td style="height: 30.125px;">price</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">Thành tiền</td><td style="height: 30.125px;">Giá trị hàng hóa trước thuế và chiết khấu (Số lượng x Đơn giá)</td><td style="height: 30.125px;">gross\_amount</td></tr><tr style="height: 30.125px;"><td style="height: 30.125px;">Thuế suất </td><td style="height: 30.125px;">Mức thuế suất giá trị gia tăng áp dụng (0%, 5%, 8%, 10%, KCT...).</td><td style="height: 30.125px;">tax\_rate</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">Tiền thuế</td><td style="height: 46.9167px;">Tổng giá trị thuế GTGT của dòng hàng (Số lượng x đơn giá x thuế suất).</td><td style="height: 46.9167px;">tax\_amount</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">Tỷ giá</td><td style="height: 46.9167px;">Tỷ giá quy đổi tại thời điểm lập hóa đơn chỉ áp dụng nếu tiền tệ khác VND</td><td style="height: 46.9167px;">exchange\_rate</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">Chức năng</td><td style="height: 46.9167px;">Các icon thao tác trên từng dòng hàng: Sửa (Chỉnh sửa nội dung) / Xóa (Loại bỏ dòng).</td><td style="height: 46.9167px;">(Action icons)</td></tr></tbody></table>

 Phần Footer của lưới hiển thị các ô tổng hợp:

- - **Tiền tệ:** Mã loại tiền tệ được sử dụng trên hóa đơn (VND, USD...) 
        - Mapping Field: currency\_code
    - **Tỷ giá:** Tỷ giá quy đổi tại thời điểm lập hóa đơn chỉ áp dụng nếu tiền tệ khác VND 
        - Mapping Field: exchange\_rate
    - **Cộng tiền hàng**: Tổng giá trị hàng hóa, dịch vụ chưa bao gồm thuế và chiết khấu. 
        - Mapping Field: gross\_amount
    - **Tiền chiết khấu**: Tổng số tiền chiết khấu thương mại được giảm trừ cho hóa đơn. 
        - Mapping Field: discount\_amount
    - **Tiền thuế**: Tổng số tiền thuế giá trị gia tăng (VAT) của toàn bộ hóa đơn. 
        - Mapping Field: tax\_amount
    - **Tổng tiền thanh toán**: Giá trị cuối cùng khách hàng phải trả (Cộng tiền hàng + Tiền thuế - Chiết khấu). 
        - Mapping Field: total\_amount

 **4.2.3. Màn hình giao diện tham khảo**

**[![bkav_edit_invoice.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/bkav-edit-invoice.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/bkav-edit-invoice.jpg)**


## **5. Mô tả Chức năng**

#### **5.1. Chức năng - Cấp số hóa đơn**

#### **5.2. Chức năng - Ký hóa đơn**

#### **5.3. Chức năng - Tạo hóa đơn thay thế**

#### **5.4. Chức năng - Cập nhật thông tin**

#### **5.4. Chức năng - Điều chỉnh hóa đơn**