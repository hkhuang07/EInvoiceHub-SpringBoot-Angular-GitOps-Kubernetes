# ...4.3. Đặc tả luồng xử lý

## 1. GIỚI THIỆU CHUNG

Tài liệu này mô tả các thông số kỹ thuật của các API được thiết lập cho PosORA gọi tới, xử lý logic nghiệp vụ và gửi và nhận bản tin các NCC HĐĐT.

## 2. DANH SÁCH API

#### 2.1. API SubmitInvoice

2.1.1 Mô hình flow

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-01/scaled-1680-/JApimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-01/JApimage.png)

2.1.2 Thông số kỹ thuật

Mục 3.2 của tài liệu API: [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB")

2.1.3 Logic xử lý: Các step tại mục 2.1.1

- - - - Step 1: Hệ thống build bản tin đáp ứng API SubmitInvoice, trong đó 
                - - PartnerID: là sys\_store\_id
                    - PartnerInvoiceID: là biz\_invoice\_id
                    - InvoiceTypeID = 0
                    - <span style="background-color: rgb(251, 238, 184);">Detail</span>
                        - - <span style="background-color: rgb(251, 238, 184);">API SubmitInvoice.Price = biz\_retail\_detail.net\_price</span>
                            - <span style="background-color: rgb(251, 238, 184);">API SubmitInvoice.GrossAmount = biz\_retail\_detail.net\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">API SubmitInvoice.DiscountRate = 0</span>
                            - <span style="background-color: rgb(251, 238, 184);">API SubmitInvoice.DiscountAmount (không truyền vào API)</span>
                            - <span style="background-color: rgb(251, 238, 184);">API SubmitInvoice.TaxAmount = biz\_retail\_detail.tax\_amount</span>
            - Step 2: Gửi bản tin từ step 1 đến eInvoice = API SubmitInvoice với SubmitInvoiceType = 100, 101, 102
            - Step 3: Lưu các value từ request vào table **einv\_invoices** và **einv\_invoice\_detail** và mặc định các field không có trong request: 
                - - reference\_type\_id = 0
                    - status\_id = 0
                    - <span style="background-color: rgb(251, 238, 184);">Tính tổng các cột Amount từ detail sum lại</span>
                        - - <span style="background-color: rgb(251, 238, 184);">gross\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">discount\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">net\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">tax\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">total\_amount</span>
                    - <span style="background-color: rgb(251, 238, 184);">Detail:</span>
                        - - <span style="background-color: rgb(251, 238, 184);">net\_amount = gross\_amount - discount\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">total\_amount = net\_amount + tax\_amount</span>
                            - <span style="background-color: rgb(251, 238, 184);">net\_price = net\_amount / quantity</span>
                            - <span style="background-color: rgb(251, 238, 184);">net\_price\_vat = total\_amount / quantity </span>
            - Step 4: Tùy theo provider để call API 
                - <table border="1" style="border-collapse: collapse; width: 111.486%;"><colgroup><col style="width: 24.1229%;"></col><col style="width: 31.3798%;"></col><col style="width: 44.5631%;"></col></colgroup><tbody><tr><td class="align-center">**SubmitInvoiceType**</td><td class="align-center">**Provider**</td><td class="align-center">**Lệnh/API**</td></tr><tr><td>100</td><td>BKAV</td><td>CmdType100</td></tr><tr><td>101</td><td>BKAV</td><td>CmdType101</td></tr><tr><td>102</td><td>BKAV</td><td>CmdType101 + CmdType205</td></tr></tbody></table>
                - <table border="1" style="border-collapse: collapse; width: 111.486%;"><colgroup><col style="width: 24.1229%;"></col><col style="width: 31.3798%;"></col><col style="width: 44.5631%;"></col></colgroup><tbody><tr><td class="align-center">**SubmitInvoiceType**</td><td class="align-center">**Provider**</td><td class="align-center">**Lệnh/API**</td></tr><tr><td>100</td><td>MOBI</td><td>{{base\_url}}/api/Invoice68/SaveListHoadon78
                    
                    Editmode: 1
                    
                    </td></tr><tr><td>101</td><td>MOBI</td><td> {{base\_url}}/api/Invoice68/SaveListHoadon78MTT
                    
                    Editmode:1
                    
                    </td></tr><tr><td>102</td><td>MOBI</td><td>{{base\_url}}/api/Invoice68/SaveListHoadon78
                    
                    **or**
                    
                    {{base\_url}}/api/Invoice68/SaveListHoadon78MTT
                    
                    {base\_url}}/api/Invoice68/SaveAndSignHoadon78
                    
                    </td></tr></tbody></table>
                - Sử dụng mapping table để đóng gói bản tin tùy theo Provider, bao gồm <table border="1" style="border-collapse: collapse; width: 111.486%; height: 152.415px;"><colgroup><col style="width: 24.1229%;"></col><col style="width: 31.3798%;"></col><col style="width: 44.5631%;"></col></colgroup><tbody><tr style="height: 29.7017px;"><td class="align-center" style="height: 29.7017px;">**Field**</td><td class="align-center" style="height: 29.7017px;">**Table**</td><td class="align-center" style="height: 29.7017px;">**Ghi chú**</td></tr><tr style="height: 29.7017px;"><td style="height: 29.7017px;">Data.InvoiceTypeID</td><td style="height: 29.7017px;">einv\_mapping\_invoice\_type</td><td style="height: 29.7017px;"> </td></tr><tr style="height: 46.5057px;"><td style="height: 46.5057px;">Data.PaymentMethodID</td><td style="height: 46.5057px;">einv\_mapping\_payment\_method</td><td style="height: 46.5057px;"> </td></tr><tr style="height: 46.5057px;"><td style="height: 46.5057px;">Data.ReferenceTypeID</td><td style="height: 46.5057px;">einv\_mapping\_reference\_type</td><td style="height: 46.5057px;"> </td></tr><tr><td>Data.Details.ItemTypeID</td><td>einv\_mapping\_item\_type</td><td>  
                    </td></tr><tr><td>Data.Details.TaxTypeID</td><td>einv\_mapping\_tax\_type</td><td>  
                    </td></tr></tbody></table>
                -
            - Step 5: Nhận bản tin trả về từ provider, nếu thành công thì parse data để thực hiện lưu data
            - Step 6: Lưu các thông tin được trả về vào các field 
                - - provider\_invoice\_id : là id hóa đơn của Provider
                    - status\_id: theo mapping của table
                    - invoice\_form
                    - invoice\_series
                    - invoice\_no
                    - invoice\_lookup\_code: Là mã tra cứu hóa đơn
            - Step 7: Phản hồi kết quả API, <span style="background-color: rgb(251, 238, 184);">**lưu ý:** dùng data của các table trong eInvoice</span>, <span style="background-color: rgb(251, 238, 184);">ko dùng data của provider map vào,</span> trong đó 
                - - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8">Provider: là mã của Provider. VD: "BKAV", "MOBI"</span></span>
                    - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="TextRun SCXW241776031 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">ProviderIn</span><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">voiceID</span></span><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> : Là ID Hóa đơn của NCC HĐ</span></span></span>
                    - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"><span class="TextRun SCXW261755675 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">UrlLoo</span><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">kup: là link tra cứu. Được kết hợp bởi dùng einv\_provider.url\_lookup + einv\_invoices.</span></span>invoice\_lookup\_code</span></span></span>

#### 2.2. API SignInvoices

2.2.1 Mô hình flow

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-01/scaled-1680-/SrHimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-01/SrHimage.png)

2.2.2 Thông số kỹ thuật

Mục 3.3 của tài liệu API: [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB")

2.2.1 Logic xử lý: Các step tại mục 2.2.1

- - - Step 1: Hệ thống build bản tin đáp ứng API SignInvoices, trong đó 
            - - PartnerID: là sys\_store\_id
                - Data: Là 1 JSONArray, đáp ứng Ký 1 danh sách hóa đơn, giới hạn 30 hóa đơn 1 lần
                - <span class="TextRun SCXW84188182 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">ID</span><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">Type</span></span><span class="EOP SCXW84188182 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> </span>: <span class="NormalTextRun SCXW76838374 BCX8">0: </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">thì</span><span class="NormalTextRun SCXW76838374 BCX8"> </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">gửi</span><span class="NormalTextRun SCXW76838374 BCX8"> einv\_einvoice.id; **1**: thì gửi einv\_einvoice.partner\_invoice\_id</span>
                - InvoiceID: Dựa theo IDType để truyền vào
        - Step 2: Gửi bản tin từ step 1 đến eInvoice = API SignInvoices
        - Step 3: Validate data 
            - - Không tồn tại ID theo IDType
                - Kiểm tra trạng thái của eInvoice tại database
        - Step 4: Tùy theo provider để call API 
            - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 41.3654%;"></col><col style="width: 58.6389%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td style="padding-left: 80px;">BKAV</td><td style="padding-left: 80px;">CmdType206</td></tr></tbody></table>
            - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 41.3654%;"></col><col style="width: 58.6389%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td style="padding-left: 80px;">MOBI</td><td> https://hcm.mobifone.vn/gateway/mbfinv/api/Invoice68/SignInvoiceCertFile68 </td></tr></tbody></table>
            -
        - Step 5: Nhận bản tin trả về từ provider, nếu thành công thì parse data để thực hiện lưu data
        - Step 6: Lưu các thông tin được trả về vào các field 
            - - signed\_date: là Ngày hiện tại
        - Step 7: Phản hồi kết quả API, **lưu ý:** dùng data của các table trong eInvoice, ko dùng data của provider map vào

#### 2.3. API GetInvoices

2.3.1 Mô hình flow

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-01/scaled-1680-/jO1image.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-01/jO1image.png)

2.3.2 Thông số kỹ thuật

Mục 3.4 của tài liệu API: [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB")

2.3.1 Logic xử lý: Các step tại mục 2.3.1

- - - Step 1: Hệ thống build bản tin đáp ứng API GetInvoices, trong đó 
            - - PartnerID: là sys\_store\_id
                - Data: Là 1 JSONArray, đáp ứng 1 danh sách hóa đơn, giới hạn 30 hóa đơn 1 lần
                - <span class="TextRun SCXW84188182 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">ID</span><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">Type</span></span><span class="EOP SCXW84188182 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> </span>: <span class="NormalTextRun SCXW76838374 BCX8">0: </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">thì</span><span class="NormalTextRun SCXW76838374 BCX8"> </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">gửi</span><span class="NormalTextRun SCXW76838374 BCX8"> einv\_einvoice.id; **1**: thì gửi einv\_einvoice.partner\_invoice\_id</span>
                - InvoiceID: Dựa theo IDType để truyền vào
        - Step 2: Gửi bản tin từ step 1 đến eInvoice = API GetInvoices
        - Step 3.1: Validate data 
            - - Không tồn tại ID theo IDType
        - Step 3.2: Check status\_id 
            - - Nếu status\_id nằm trong danh sách: Hóa đơn đã phát hành, Hóa đơn điều chỉnh đã ký, Hóa đơn thay thế đã ký, Hóa đơn Bị điều chỉnh, Hóa đơn bị thay thế --&gt; **Thì dùng data của eInvoice trả về** luôn mà ko cần gọi qua Provider để cập nhật trạng thái
        - Step 4: implentment function riêng để update thông tin từ Provider, tùy theo provider để call API 
            - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 41.3654%;"></col><col style="width: 58.6389%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td style="padding-left: 80px;">BKAV</td><td style="padding-left: 80px;">CmdType800</td></tr></tbody></table>
            - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 41.3654%;"></col><col style="width: 58.6389%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td style="padding-left: 80px;">MOBI</td><td>  
                </td></tr></tbody></table>
            -
        - Step 5: Nhận bản tin trả về từ provider, nếu thành công thì parse data để thực hiện lưu data
        - Step 6: Lưu các thông tin được trả về vào các field 
            - - Lưu các thông tin về: 
                    - - status\_id
                        - invoice\_date
                        - invoice\_form
                        - invoice\_series
                        - invoice\_no
                        - signed\_date
                        - tax\_authority\_code
                        - invoice\_lookup\_code
                        - provider\_invoice\_id
        - Step 7: Phản hồi kết quả API,<span style="background-color: rgb(251, 238, 184);"> **lưu ý:** dùng data của các table trong eInvoice, ko dùng data của provider map vào</span>

#### 2.4. API GetStatusInvoices

  **2.4.1. Mô hình flow**

 [![api_getinvoice_status.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/api-getinvoice-status.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/api-getinvoice-status.jpg)

 **2.5.2 Thông số kỹ thuật**

 Mục 3.4 của tài liệu API: [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB")

 **2.5.3 Logic xử lý: Các step tại mục 2.5.1**

- - **Step 1:** Hệ thống build bản tin tra cứu trạng thái hóa đơn đáp ứng API GetStatusInvoice 
        - ParnerID : là sys\_store\_id
        - Data: Là 1 JSONArray, đáp ứng 1 danh sách hóa đơn, giới hạn 30 hóa đơn 1 lần
        - <span class="TextRun SCXW84188182 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">ID</span><span class="NormalTextRun SpellingErrorV2Themed SCXW84188182 BCX8">Type</span></span><span class="EOP SCXW84188182 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> </span>: <span class="NormalTextRun SCXW76838374 BCX8">0: </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">thì</span><span class="NormalTextRun SCXW76838374 BCX8"> </span><span class="NormalTextRun SpellingErrorV2Themed SCXW76838374 BCX8">gửi</span><span class="NormalTextRun SCXW76838374 BCX8"> einv\_einvoice.id; **1**: thì gửi einv\_einvoice.partner\_invoice\_id</span>
        - InvoiceID: Dựa theo IDType để truyền vào
        - ForceSyn : flag cho biết hệ thống bán hàng có muốn đồng bộ trạng thái từ CQT qua Provider hay không (true: có , false: không)
    - **Step 2:** Hệ thống bán hàng gửi API GetInvoiceStatus cho Hệ thống eInvoice . eInvoice xác định đối tượng hóa đơn dựa trên ParnerID (mã chi nhánh) và InvoiceID
    - **Step 3:** Phân loại trạng thái và Validation
        
        
        - Không tồn tại ID theo IDType 3.1 Validate data**:** Kiểm tra hóa đơn có thuộc quyền quản lý của ParnerId gửi lên không ?
        - 3.2
            
            
            - Nếu status\_id thuộc các trạng thái:<span style="background-color: rgb(191, 237, 210);"> 2, 6, 8, 9,10</span>
                
                
                - 2 : Đã phát hành (HĐ Gốc).
                - 6: HĐ thay thế đã phát hành.
                - 8: HĐ điều chỉnh đã phát hành.
                - 9: Hóa đơn bị thay thế.
                - 10 : Hóa đơn bị điều chỉnh.
            - Nếu status\_id thuộc các trạng thái:<span style="background-color: rgb(191, 237, 210);"> 0,1, 5, 7</span>
                
                
                - 0: HĐ mới tạo (chưa cấp số).
                - 1: HĐ đã cấp số (chờ ký).
                - 5: HĐ thay thế chờ ký.
                - 7: HĐ điều chỉnh chờ ký.
        - 3.3 Return
            
            
            - Điều kiện: Nếu status\_id thuộc (2, 6, 8, 9,10) AND tham số ForceSync = False
            - Trả kết quả trực tiếp từ Database nội bộ về POS (Chuyển đến Step 7).
    - **Step 4:** Nếu status\_id thuộc (0, 1, 5, 7) HOẶC ForceSync = False, Hub thực hiện gọi API từ Provider
        
        
        - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 22.8089%;"></col><col style="width: 77.1194%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">BKAV</td><td class="align-center" style="padding-left: 80px;">CmdType801</td></tr></tbody></table>
        - <table border="1" style="border-collapse: collapse; width: 100.018%;"><colgroup><col style="width: 22.8468%;"></col><col style="width: 77.2173%;"></col></colgroup><tbody style="padding-left: 80px;"><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">**Provider**</td><td class="align-center" style="padding-left: 80px;">**Lệnh/API**</td></tr><tr style="padding-left: 80px;"><td class="align-center" style="padding-left: 80px;">MOBI</td><td class="align-center">  
            </td></tr></tbody></table>
    - **Step 5:** Hub nhận bản tin phản hồi từ Provider nếu Status = 0 (Thành công với BKAV), thực hiện bóc tách các trường thông tin pháp lý do Provider trả về
    - **Step 6:** Hệ thống thực hiện lưu trữ các giá trị mới nhất vào bảng einv\_invoices
        
        
        - Cập nhật status\_id
        - Cập nhật signed\_date
        - Cập nhật invoice\_lookup\_code
    - **Step 7:**  Hub đóng gói record và phản hồi cho Hệ thống bán hàng. Dữ liệu trả về là các thông tin
        
        
        - InvoiceStatusID: Trạng thái đã được chuẩn hóa.
        - InvoiceNo: Số hóa đơn chính thức.
        - InvoiceLookupCode: Mã tra cứu.
        - UrlLookup : Link tra cứu (ghép từ einv\_provider.url\_lookup + invoice\_lookup\_code).

#### 2.5. API SubmitAdjustInvoice

 **2.5.1. Mô hình flow**

[![api_adjustinvoice_sequence_diagram.jpg](https://bookstack.softz.vn/uploads/images/gallery/2026-03/scaled-1680-/api-adjustinvoice-sequence-diagram.jpg)](https://bookstack.softz.vn/uploads/images/gallery/2026-03/api-adjustinvoice-sequence-diagram.jpg)

 **2.5.2 Thông số kỹ thuật**

 Mục 3.5 của tài liệu API: [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB")

 **2.5.4 Logic xử lý: Các step tại mục 2.5.1**

- - **Step 1:** <span class="">Hệ thống Hub thực hiện chuẩn hóa dữ liệu từ PosORA truyền vào để build bản tin đáp ứng API SubmitAdjustinvoice</span><span class="">.</span><span class=""> Các thông tin định danh hóa đơn gốc được trích xuất từ bảng einv\_invoice</span>  
        
        - ParterID: là sys\_store\_id
        - **Master:**
            - <span style="background-color: rgb(255, 255, 255);">ParterInvoiceID: là biz\_invoice\_id của chứng từ điều chỉnh</span>
            - InvoiceTypeID = 0
            - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReferenceTypeID = 2</span> (hóa đơn điều chỉnh theo mục 4.3 Danh sách ReferenceTypeID tài liệu [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB"))
            - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceIdentify</span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif;"> = </span></span><span style="background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\[</span></span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">einv\_invoice.org\_invoice\_form<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\]\_\[</span></span>einv\_invoice.org\_invoice\_series<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\]</span></span></span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\_<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\[</span></span></span></span> einv\_invoice.org\_invoice\_no\]</span></span>
                - <span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);"><span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: maroon;"><span style="color: rgb(0, 0, 0);">Định dạng: \[InvoiceForm\]\_<span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);"><span style="color: maroon;"><span style="color: rgb(0, 0, 0);">\[InvoiceSeries\]</span></span></span></span><span class="italic">\_</span>\[InvoiceNo\]. </span></span></span></span></span>
                - <span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);">Ví dụ: OriginalInvoiceIdentify"</span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0);">: "\[1\]\_\[C22TAA\]\_\[0000001\]"</span>
    - **Step 2:** Gửi bản tin từ Step 1 đến Hub - phần mềm tích hợp nhà cung cấp hóa đơn điện tử với API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice </span>với 
        - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoiceType= 111 (Tạo hóa đơn điều chỉnh chỉ cấp số, chờ ký),</span>
        - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoiceType = 112 (Tạo hóa đơn điều chỉnh và ký phát hành luôn)</span>.
    - **Step 3:** Lưu các value từ request vào table einv\_invoces và einv\_invoice\_detail 
        - Master: 
            - reference\_type\_id= 2
            - invoice\_type\_id= 0
            - status\_id: 
                - SubmitInvoiceType = 111: status\_id = 7 (Hóa đơn điều chỉnh đã cấp số, chờ ký (theo 2.Danh sách InvoiceStatusID tài liệu [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB") )
                - SubmitInvoiceType = 112: status\_id = 7 (Hóa đơn điều chỉnh<span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"> mới hóa đơn điều chỉnh và ký phát hành luôn)</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_form</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_series</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_no</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_reason</span>
        - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Details:</span>
            - gross\_amount
            - discount\_amount
            - net\_amount
            - tax\_amount
            - total\_amount
            - net\_amount
            - total\_amount
            - net\_price
            - net\_price\_vat
    - **Step 4:** Tùy theo provider để call API: 
        - BKAV Provider (theo D. Phụ Lục tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx") )  
            
            - Đối với Master thông tin chung hóa đơn, cần xác định tính chất điều chỉnh qua tr<span style="background-color: rgb(255, 255, 255);">ường<span style="color: rgb(255, 255, 255);">ng<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(255, 255, 255);"> </span></span></span></span>
            - <span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceIdentify =</span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"> </span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\[</span></span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">einv\_invoice.org\_invoice\_form<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\]\_\[</span></span>einv\_invoice.org\_invoice\_series<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\]</span></span></span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\_<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\[</span></span></span></span> einv\_invoice.org\_invoice\_no\]</span></span>
            - <span style="color: rgb(0, 0, 0);"> <span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">IsIncrease =</span></span><span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> False</span> (hệ thống đang xử lý cho hóa đơn giảm) (True: tăng , False: giảm, Null: điều chỉnh thông tin)</span>
            - <span style="background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"> Reason = einv\_reason</span></span>
            - <table border="1" style="border-collapse: collapse; width: 108.649%;"><colgroup><col style="width: 24.8732%;"></col><col style="width: 12.7719%;"></col><col style="width: 62.3028%;"></col></colgroup><tbody><tr><td style="height: 30.125px;">**SubmitInvoiceType**</td><td style="height: 30.125px;">**Provider**</td><td style="height: 30.125px;">**Lệnh/API (CmdType)**</td></tr><tr><td style="height: 32.25px;">111</td><td style="height: 32.25px;">BKAV</td><td style="height: 32.25px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">CmdType 124 (Điều chỉnh, Bkav cấp số) </span></span><span data-path-to-node="8,3,1,0,0,3"><span class="citation-73 citation-end-73"><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup></span></span></td></tr><tr><td style="height: 30.125px;">112</td><td style="height: 30.125px;">BKAV</td><td style="height: 30.125px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">CmdType 124 + CmdType 205 (Ký HSM)</span></span></td></tr></tbody></table>

- - - MOBI Provider (theo <span class="fontstyle0">4. Các API liên quan đến Hóa đơn </span>tài liệu [ver 4\_7\_MobiFone Invoice\_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf](https://drive.google.com/file/d/1jqHw3ivh1MzgcxS0Q3KuHeuwP3cKIQcn/view?usp=sharing "ver 4_7_MobiFone Invoice_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf") )  
            
            - JSON editmode: 1 (Tạo mới hóa đơn)
            - Đối với Master thông tin chung hóa đơn, cần xác định tính chất thông báo điều chỉnh qua trường 
                - Master 
                    - <span style="background-color: rgb(191, 237, 210);"> <span style="background-color: rgb(191, 237, 210);"><span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">original\_invoice\_id </span></span></span>= <span style="background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"> </span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\[</span></span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">einv\_invoice.org\_invoice\_form<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\]\_\[</span></span>einv\_invoice.org\_invoice\_series<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\]</span></span></span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">\_<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);">\[</span></span></span></span> einv\_invoice.org\_invoice\_no\]</span></span> </span>
                    - <span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">invoice\_status=</span> <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> 21 ( Điều chỉnh giảm ) </span></span>
                    - <span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">addition\_reference\_note: einv.org\_invoice\_reason</span></span>
            - <table border="1" style="border-collapse: collapse; width: 100.024%; height: 123.958px;"><colgroup><col style="width: 24.7141%;"></col><col style="width: 12.8877%;"></col><col style="width: 62.316%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td style="height: 30.125px;">**SubmitInvoiceType**</td><td style="height: 30.125px;">**Provider**</td><td style="height: 30.125px;">**Lệnh/API (CmdType)**</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">111</td><td style="height: 46.9167px;">MOBI</td><td style="height: 46.9167px;">{{base\_url}}/api/Invoice68/SaveListHoadon78MTT (Editmode: 1)  
                <span data-path-to-node="8,3,1,0,0,3"><span class="citation-73 citation-end-73"><sup class="superscript" data-turn-source-index="15"></sup></span></span></td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">112</td><td style="height: 46.9167px;">MOBI</td><td style="height: 46.9167px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">{{base\_url}}/api/Invoice68/SaveAndSignHoadon78 (Editmode: 1)</span></span></td></tr></tbody></table>

- - Step 5: Hub nhận record trả về từ Provider, nếu thành công (Reponse có BKAV return Status = 0, Mobifone return code 200 ) thì thực hiện parse data (C. Sample Code theo tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx"))
    - Step 6: Lưu các thông tin định danh mới của hóa đơn điều chỉnh vào Database 
        - provider\_invoice\_id: ID hóa đơn từ nhà cung cấp
        - status\_id: Cập nhật theo kết quả trả về 7 (<span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Hóa đơn điều chỉnh chưa ký</span>) (theo 2.Danh sách InvoiceStatusID tài liệu [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB"))
        - invoice\_no: Số hóa đơn được trả về với các lệnh 111, 112
        - invoice\_series: Ký hiệu Hoá đơn trên Bkav
        - invoice\_lookup\_code: Mã tra cứu của Hoá đơn trên Website: http://tracuu.ehoadon.vn (với hệ thống test là [https://demo.ehoadon.vn/TCHD)](https://demo.ehoadon.vn/TCHD))
    - Step 7: Phản hồi kết quả API cho PosORA. **lưu ý:** dùng data của các table trong eInvoice, ko dùng data của provider map vào, trong đó 
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8">Provider: là mã của Provider. VD: "BKAV", "MOBI"</span></span>
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="TextRun SCXW241776031 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">ProviderIn</span><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">voiceID</span></span><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> : Là ID Hóa đơn của NCC HĐ</span></span></span>
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"><span class="TextRun SCXW261755675 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">UrlLoo</span><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">kup: là link tra cứu. Được kết hợp bởi dùng einv\_provider.url\_lookup + einv\_invoices.</span></span>invoice\_lookup\_code</span></span></span>

 **2.5.5 Validation nghiệp vụ**

- - Hóa đơn gốc phải tồn tại trong hệ thống (tham chiếu qua OrgInvoiceID)
    - Hóa đơn gốc phải có trạng thái đã phát hành (status\_id = 2 (đã ký), hoặc 8 (điều chỉnh)) (theo Danh sách InvoiceStatusID tài liệu [4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB"))
    - Hóa đơn gốc chưa bị thay thế (không có hóa đơn thay thế reference\_type\_id = 3 (<span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Hóa đơn thay thế) </span>) (theo Danh sách ReferrenceTypeID tài liệu[4.5. Tài liệu tích hợp API - EINVOICEHUB](https://bookstack.softz.vn/books/c-erp-software-tai-lieu-nghiep-vu/page/45-tai-lieu-tich-hop-api-einvoicehub "4.5. Tài liệu tích hợp API - EINVOICEHUB") )
    - Lý do điều chỉnh (OrgInvoiceReason) là bắt buộc và phải có nội dung hợp lệ

#### 2.6. API ReplaceInvoice