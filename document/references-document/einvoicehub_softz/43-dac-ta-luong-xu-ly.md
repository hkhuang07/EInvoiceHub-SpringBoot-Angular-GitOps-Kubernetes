# ...4.3. Đặc tả luồng xử lý

## 1. GIỚI THIỆU CHUNG

Tài liệu này mô tả các thông số kỹ thuật của các API được thiết lập cho PosORA gọi tới, xử lý logic nghiệp vụ và gửi và nhận bản tin các NCC HĐĐT.

## 2. DANH SÁCH API

#### 2.1. API SubmitInvoice

2.1.1 Mô hình flow

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2026-01/scaled-1680-/JApimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-01/JApimage.png)

2.1.2 Thông số kỹ thuật

Mục 3.2 của tài liệu API: [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi)

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

Mục 3.3 của tài liệu API: [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi)

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

Mục 3.4 của tài liệu API: [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi)

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

 Mục 3.4 của tài liệu API: [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi)

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

[![api_adjustinvoice_sequence_diagra.png](https://bookstack.softz.vn/uploads/images/gallery/2026-02/scaled-1680-/api-adjustinvoice-sequence-diagra.png)](https://bookstack.softz.vn/uploads/images/gallery/2026-02/api-adjustinvoice-sequence-diagra.png)

 **2.5.2 Thông số kỹ thuật**

 Mục 3.5 của tài liệu API: [API INTEGRATION DOCUMENTATION.docx ](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) cập nhật mới nhất bên dưới

<table border="1" id="bkmrk-3.5.%C2%A0%C2%A0%C2%A0-api-submitad" style="border-collapse: collapse; width: 100%; height: 29.8785px;"><colgroup><col style="width: 99.881%;"></col></colgroup><tbody><tr style="height: 29.8785px;"><td style="height: 29.8785px;">## **<span style="font-size: 14.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span style="mso-list: Ignore;">3.5.<span style="font: 7.0pt 'Times New Roman';"> </span></span></span>****<span style="font-size: 14.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">API SubmitAdjustInvoice</span>**

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Header</span>**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" style="border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0in 5.4pt 0in 5.4pt;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 117.0pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 67.5pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 252.75pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 67.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 252.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 67.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 252.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 67.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 252.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 67.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 252.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="337">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>*

</td></tr></tbody></table>

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>**

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request</span>**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" style="border-collapse: collapse; border: none; width: 79.5695%; height: 1371.22px;"><tbody><tr style="height: 21.0938px;"><td style="width: 7.35985%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lv</span>**

</td><td style="width: 28.6727%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 9.19981%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Re\*</span>**

</td><td style="width: 13.953%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 40.9391%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 61.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 61.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestDateTime</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request submission time</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DDTHH:mm:ssZ</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ex: 2024-01-02T10:16:01Z</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID of the request</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UserAgent</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoiceType</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(3)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng SubmitInvoiceType</span>*

</td></tr><tr style="height: 61.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 61.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReferenceTypeID</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 61.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Giá trị cố định = 2: Xác định đây là hóa đơn điều chỉnh. Xem bảng ReferenceTypeID</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceForm</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mẫu số hóa đơn gốc</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceSeries</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ký hiệu hóa đơn gốc</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceNo</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số hóa đơn gốc</span>

</td></tr><tr style="height: 41.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 41.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceReason</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(500)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lý do điều chỉnh hóa đơn gốc (sẽ hiển thị trên bản in hóa đơn điều chỉnh)</span>

</td></tr><tr style="height: 96px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 96px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 96px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">IsIncrease</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 96px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 96px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Boolean</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 96px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Trường đặc biệt cho điều chỉnh: </span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">true = Điều chỉnh tăng số tiền; </span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">false = Điều chỉnh giảm số tiền; null/không truyền = Điều chỉnh thông tin (không thay đổi số tiền)</span>

</td></tr><tr style="height: 38.6406px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 38.6406px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 38.6406px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 38.6406px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 38.6406px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(50)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 38.6406px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID Hóa đơn của Partner gửi đến eInvoice</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceTypeID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng InvoiceTypeID</span>*

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceDate</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(10)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DD</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Mẫu số hóa đơn điều chỉnh</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceSeries</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Ký hiệu hóa đơn điều chỉnh</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PaymentMethodID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng PaymentMethodID</span>*

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerTaxCode</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã số thuế người mua</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerCompany</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td><td rowspan="3" style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 117px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Phải có 1 trong 3 thông tin: BuyerCompany, BuyerName, BuyerAddress</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerName</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,200)</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerAddress</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td></tr><tr style="height: 22.4375px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 22.4375px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4375px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerIDNo</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4375px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4375px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4375px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Số CCCD/Hộ chiếu người mua</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerMobile</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Số điện thoại người mua</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBankAccount</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Tài khoản ngân hàng người mua</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBankName</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,200)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Tên tài khoản ngân hàng người mua</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBudgetCode</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Mã số đơn vị quan hệ ngân sách</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReceiveTypeID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng ReceiveTypeID</span>*

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReceiverEmail</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,50)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Email nhận hóa đơn của người mua</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">CurrencyCode</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,3)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã tiền tệ. Mặc định = "VND"</span>

</td></tr><tr style="height: 22.1875px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ExchangeRate</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tỷ giá. Mặc định = 1.0</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Notes</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Ghi chú trên hóa đơn điều chỉnh</span>

</td></tr><tr style="height: 41.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Details</span>**

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Danh sách chi tiết hàng hóa/dịch vụ điều chỉnh</span>

</td></tr><tr style="height: 41.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemTypeID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng ItemTypeID</span>*

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 1 (Hàng hóa, dịch vụ)</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemCode</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Mã hàng hóa, dịch vụ</span>

</td></tr><tr style="height: 39px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 39px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemName</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(1,300)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 39px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Tên hàng hóa, dịch vụ</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UnitName</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(1,20)</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Đơn vị tính</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Quantity</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Số lượng hàng hóa, dịch vụ</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Price</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Đơn giá hàng hóa (Chưa thuế)</span>

</td></tr><tr style="height: 41.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">GrossAmount</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 41.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thành tiền. </span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = Quantity \* Price</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountRate</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Chiết khấu. Vd: 10.0</span>

</td></tr><tr style="height: 54.7656px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 54.7656px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 54.7656px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountAmount</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 54.7656px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 54.7656px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 54.7656px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền Chiết khấu.</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = GrossAmount \* DiscountRate/100</span>

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxTypeID</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng TaxTypeID</span>*

</td></tr><tr style="height: 21.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxRate</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Thuế. Vd: 8.0</span>

</td></tr><tr style="height: 47.0938px;"><td style="width: 7.35985%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 47.0938px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.6727%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 47.0938px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAmount</span>

</td><td style="width: 9.19981%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 47.0938px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 13.953%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 47.0938px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 40.9391%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 47.0938px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền thuế. Mặc định = (GrossAmount – DiscountAmount) \* TaxRate/100</span>

</td></tr></tbody></table>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Response success</span>**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" style="border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0in 5.4pt 0in 5.4pt;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 130.5pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 48.75pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 74.25pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 182.25pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 200</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number </span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 0: Thành công</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 43.5pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 43.5pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 43.5pt;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 43.5pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 43.5pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 43.5pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> ID Hóa đơn của Partner</span>

</td></tr><tr style="mso-yfti-irow: 6; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> ID hóa đơn trên eInvoice</span>

</td></tr><tr style="mso-yfti-irow: 7; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Mẫu số hóa đơn điều chỉnh</span>

</td></tr><tr style="mso-yfti-irow: 8; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceNo</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Số hóa đơn điều chỉnh</span>

</td></tr><tr style="mso-yfti-irow: 9; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceLookupCode </span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã tra cứu hóa đơn</span>

</td></tr><tr style="mso-yfti-irow: 10; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAuthorityCode</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã của Cơ quan thuế</span>

</td></tr><tr style="mso-yfti-irow: 11; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Provider</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã của NCC HĐĐT</span>

</td></tr><tr style="mso-yfti-irow: 12; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ProviderInvoiceID</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID hóa đơn của NCC HĐĐT</span>

</td></tr><tr style="mso-yfti-irow: 13; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 130.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UrlLookup</span>

</td><td style="width: 48.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Link tra cứu của NCC HĐĐT</span>

</td></tr></tbody></table>

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>**

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Response error</span>**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" style="border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0in 5.4pt 0in 5.4pt;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 117.0pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 63.0pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 74.25pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 182.25pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; background: #BFBFBF; mso-background-themecolor: background1; mso-background-themeshade: 191; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 63.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 63.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 63.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String </span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 1: thất bại</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseDesc</span>

</td><td style="width: 63.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả nội dung</span>

</td></tr><tr style="mso-yfti-irow: 5; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 53.75pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 117.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>

</td><td style="width: 63.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 74.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 182.25pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0in 5.4pt 0in 5.4pt; height: 15.0pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr></tbody></table>

</td></tr></tbody></table>

 **2.5.3 Mô tả nghiệp vụ**

 API AdjustInvoice dùng để tạo và phát hành hóa đơn điều chỉnh cho một hóa đơn đã tồn tại. Hóa đơn điều chỉnh được sử dụng khi phát hiện sai sót trên hóa đơn đã phát hành và cần điều chỉnh nội dung. Có 3 loại điều chỉnh:

- - **Điều chỉnh tăng (IsIncrease = true)**: Điều chỉnh tăng số tiền hàng hóa/dịch vụ hoặc thuế so với hóa đơn gốc
    - **Điều chỉnh giảm (IsIncrease = false)**: Điều chỉnh giảm số tiền hàng hóa/dịch vụ hoặc thuế so với hóa đơn gốc
    - **Điều chỉnh thông tin (IsIncrease = null)**: Chỉ thay đổi thông tin trên hóa đơn như tên người mua, địa chỉ, mã số thuế mà không thay đổi số tiền

 **2.5.4 Logic xử lý: Các step tại mục 2.5.1**

- - **Step 1:** <span class="">Hệ thống Hub thực hiện chuẩn hóa dữ liệu từ PosORA truyền vào để build bản tin đáp ứng API SubmitAdjustinvoice</span><span class="">.</span><span class=""> Các thông tin định danh hóa đơn gốc được trích xuất từ bảng einv\_invoice</span>  
        
        - ParterID: là sys\_store\_id
        - ParterInvoiceID: là biz\_invoice\_id của chứng từ điều chỉnh
        - **Master:**  
            
            - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.ReferenceTypeID = 2</span> (hóa đơn điều chỉnh theo mục 4.3 Danh sách ReferenceTypeID tài liệu [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) )
            - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.IsIncrease: False</span> (hệ thống đang xử lý cho hóa đơn giảm) (True: tăng , False: giảm, Null: điều chỉnh thông tin)
            - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceID = einv\_invoice.org\_invoice\_id</span>
            - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceForm = einv\_invoice.org\_invoice\_form</span>
            - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceSeries</span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= einv\_invoice.org\_invoice\_series</span>
            - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">API SubmitAdjustInvoice.OrgInvoiceNo= einv\_invoice.org\_invoice\_no</span>
            - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">API SubmitAdjustInvoice.OrgInvoiceReason = einv\_invoice.org\_invoice\_reason</span>
            - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">API SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceIdentify</span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif;"> = </span></span><span style="background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(255, 255, 255);">\[</span></span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">einv\_invoice.org\_invoice\_form<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(255, 255, 255);">\]\_\[</span></span>einv\_invoice.org\_invoice\_series<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);">\]</span></span></span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(255, 255, 255);">\_<span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);">\[</span></span></span></span> einv\_invoice.org\_invoice\_no\]</span></span>
                - <span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);"><span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: maroon;"><span style="color: rgb(0, 0, 0);">Định dạng: \[InvoiceForm\]\_<span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);"><span style="color: maroon;"><span style="color: rgb(0, 0, 0);">\[InvoiceSeries\]</span></span></span></span><span class="italic">\_</span>\[InvoiceNo\]. </span></span></span></span></span>
                - <span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: #8000ff; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"><span style="color: rgb(0, 0, 0);">Ví dụ: OriginalInvoiceIdentify"</span></span><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0);">: "\[1\]\_\[C22TAA\]\_\[0000001\]"</span>
            - **Details**  
                
                - API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.Details = danh sách thông tin hàng hóa dịch vụ cần điều chỉnh từ table einv\_invoice\_detail</span>
    - **Step 2:** Gửi bản tin từ Step 1 đến Hub - phần mềm tích hợp nhà cung cấp hóa đơn điện tử với API <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice </span>với 
        - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoicetype= 111 (Tạo hóa đơn điều chỉnh chỉ cấp số, chờ ký),</span>
        - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoicetype = 112 (Tạo hóa đơn điều chỉnh và ký phát hành luôn)</span>.
    - **Step 3:** Lưu các value từ request vào table einv\_invoces và einv\_invoice\_detail 
        - Master: 
            - reference\_type\_id= 2
            - status\_id: 
                - SubmitInvoiceType = 111: status\_id = 7 (Hóa đơn điều chỉnh đã cấp số, chờ ký (theo 2.Danh sách InvoiceStatusID tài liệu [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) )
                - SubmitInvoiceType = 112: status\_id = 7 (Hóa đơn điều chỉnh<span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;"> mới hóa đơn điều chỉnh và ký phát hành luôn)</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_form: </span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_series</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_no</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_reason</span>
            - <span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">org\_invoice\_id</span>
            -
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
            
            - Đối với Master thông tin chung hóa đơn, cần xác định tính chất điều chỉnh qua trường
            - <span style="color: rgb(0, 0, 0);"><span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);"> OriginalInvoiceIdentify = </span>SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceIdentify</span></span></span>
            - <span style="color: rgb(0, 0, 0);"> <span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; background-color: rgb(191, 237, 210);">IsIncrease = </span></span><span style="background-color: rgb(191, 237, 210);"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span style="color: rgb(0, 0, 0);">SubmitAd</span>justInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">IsIncrease =</span> (True: tăng , False: giảm, Null: điều chỉnh thông tin)</span>
            - <span style="background-color: rgb(191, 237, 210);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif; color: rgb(0, 0, 0); background-color: rgb(191, 237, 210);"> Reason = </span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span style="color: rgb(0, 0, 0);">SubmitAd</span>justInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Reason</span></span>
            - <span style="font-family: 'Times New Roman', serif; color: rgb(0, 0, 0);"><span lang="vi" style="font-size: 12pt; line-height: 115%; font-family: 'Times New Roman', serif;">ListInvoiceDetailsWS = </span></span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"><span style="color: rgb(0, 0, 0);">SubmitAd</span>justInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Details</span>
            - <table border="1" style="border-collapse: collapse; width: 108.649%;"><colgroup><col style="width: 24.8732%;"></col><col style="width: 12.7719%;"></col><col style="width: 62.3028%;"></col></colgroup><tbody><tr><td style="height: 30.125px;">**SubmitInvoiceType**</td><td style="height: 30.125px;">**Provider**</td><td style="height: 30.125px;">**Lệnh/API (CmdType)**</td></tr><tr><td style="height: 32.25px;">111</td><td style="height: 32.25px;">BKAV</td><td style="height: 32.25px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">CmdType 124 (Điều chỉnh, Bkav cấp số) </span></span><span data-path-to-node="8,3,1,0,0,3"><span class="citation-73 citation-end-73"><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup><sup class="superscript" data-turn-source-index="15"></sup></span></span></td></tr><tr><td style="height: 30.125px;">112</td><td style="height: 30.125px;">BKAV</td><td style="height: 30.125px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">CmdType 124 + CmdType 205 (Ký HSM)</span></span></td></tr></tbody></table>

- - - MOBI Provider (theo <span class="fontstyle0">4. Các API liên quan đến Hóa đơn </span>tài liệu [ver 4\_7\_MobiFone Invoice\_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf](https://drive.google.com/file/d/1jqHw3ivh1MzgcxS0Q3KuHeuwP3cKIQcn/view?usp=sharing "ver 4_7_MobiFone Invoice_ Tài liệu API tích hợp theo Nghị định 70 với đơn vị tích hợp.pdf") )  
            
            - JSON editmode: 2 (chế độ chỉnh sửa) 
                - 1. Tạo mới 2. Chỉnh sửa 3. Xóa hóa đơn
            - Đối với Master thông tin chung hóa đơn, cần xác định tính chất thông báo điều chỉnh qua trường 
                - Master 
                    - <span style="background-color: rgb(191, 237, 210);">JSON <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">khieu</span>: <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>.<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">OrgInvoiceSeries</span></span>
                    - <span style="background-color: rgb(191, 237, 210);">JSON <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">shdon</span>: <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.OrgInvoiceNo</span></span>
                    - <span style="background-color: rgb(191, 237, 210);">JSON <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">tthdon</span>: <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.IsIncrease = Fals -&gt; (21 : Điều chỉnh giảm ) , True -&gt; ( 19 : Điều chỉnh tăng) , Null -&gt; </span>(23<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>: <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> Điều chỉnh thông tin</span>)</span>
                    - JSON ... : <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice.OrgInvoiceReason</span><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>
                    - <span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSON details: SubmitAdjustInvoice.Details </span>
            - <table border="1" style="border-collapse: collapse; width: 100.024%; height: 123.958px;"><colgroup><col style="width: 24.7216%;"></col><col style="width: 12.8981%;"></col><col style="width: 62.3282%;"></col></colgroup><tbody><tr style="height: 30.125px;"><td style="height: 30.125px;">**SubmitInvoiceType**</td><td style="height: 30.125px;">**Provider**</td><td style="height: 30.125px;">**Lệnh/API (CmdType)**</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">111</td><td style="height: 46.9167px;">MOBI</td><td style="height: 46.9167px;">{{base\_url}}/api/Invoice68/SaveListHoadon78MTT (Editmode: 2)  
                <span data-path-to-node="8,3,1,0,0,3"><span class="citation-73 citation-end-73"><sup class="superscript" data-turn-source-index="15"></sup></span></span></td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px;">112</td><td style="height: 46.9167px;">MOBI</td><td style="height: 46.9167px;"><span data-path-to-node="8,3,1,0,0,2"><span class="citation-73">{{base\_url}}/api/Invoice68/SaveAndSignHoadon78 (Editmode: 2)</span></span></td></tr></tbody></table>

- - Step 5: Hub nhận record trả về từ Provider, nếu thành công (Reponse có BKAV return Status = 0, Mobifone return code 200 ) thì thực hiện parse data (C. Sample Code theo tài liệu [FAQ\_WebServices\_Bkav.docx](https://1drv.ms/w/c/cd3ce7f17db28040/IQBCrl55Ir0WS5eDMB1IhpqrATHQVqQ_Vu86O0V7Y5gZaqQ?e=hE7ukI "FAQ_WebServices_Bkav.docx"))
    - Step 6: Lưu các thông tin định danh mới của hóa đơn điều chỉnh vào Database 
        - provider\_invoice\_id: ID hóa đơn từ nhà cung cấp
        - status\_id: Cập nhật theo kết quả trả về 7 (<span lang="vi" style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: #002A; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Hóa đơn điều chỉnh chưa ký</span>) (theo 2.Danh sách InvoiceStatusID tài liệu [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) )
        - invoice\_no: Số hóa đơn được trả về với các lệnh 111, 112
        - invoice\_series: Ký hiệu Hoá đơn trên Bkav
        - invoice\_lookup\_code: Mã tra cứu của Hoá đơn trên Website: http://tracuu.ehoadon.vn (với hệ thống test là [https://demo.ehoadon.vn/TCHD)](https://demo.ehoadon.vn/TCHD))
    - Step 7: Phản hồi kết quả API cho PosORA. **lưu ý:** dùng data của các table trong eInvoice, ko dùng data của provider map vào, trong đó 
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8">Provider: là mã của Provider. VD: "BKAV", "MOBI"</span></span>
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="TextRun SCXW241776031 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">ProviderIn</span><span class="NormalTextRun SpellingErrorV2Themed SCXW241776031 BCX8">voiceID</span></span><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"> : Là ID Hóa đơn của NCC HĐ</span></span></span>
        - <span class="TextRun SCXW252618073 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW252618073 BCX8"><span class="EOP SCXW241776031 BCX8" data-ccp-props="{"134233117":false,"134233118":false,"201341983":0,"335551550":1,"335551620":1,"335559685":270,"335559737":0,"335559738":0,"335559739":0,"335559740":240}"><span class="TextRun SCXW261755675 BCX8" data-contrast="auto" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">UrlLoo</span><span class="NormalTextRun SpellingErrorV2Themed SCXW261755675 BCX8">kup: là link tra cứu. Được kết hợp bởi dùng einv\_provider.url\_lookup + einv\_invoices.</span></span>invoice\_lookup\_code</span></span></span>

 **2.5.5 Validation nghiệp vụ**

- - Hóa đơn gốc phải tồn tại trong hệ thống (tham chiếu qua OrgInvoiceID)
    - Hóa đơn gốc phải có trạng thái đã phát hành (status\_id = 2 (đã ký), hoặc 8 (điều chỉnh)) (theo Danh sách InvoiceStatusID tài liệu [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) )
    - Hóa đơn gốc chưa bị thay thế (không có hóa đơn thay thế reference\_type\_id = 3 (<span style="font-size: 12.0pt; line-height: 115%; font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA;">Hóa đơn thay thế) </span>) (theo Danh sách ReferrenceTypeID tài liệu [API INTEGRATION DOCUMENTATION.docx](https://softzvn.sharepoint.com/:w:/s/ERPforSOFTZ/IQBkyN2AMFx5R6XqlEcqCioiAZnc66yq4TXq1pTkspHDrEU?e=E0dDJi) )
    - Lý do điều chỉnh (OrgInvoiceReason) là bắt buộc và phải có nội dung hợp lệ

#### 2.6. API ReplaceInvoice