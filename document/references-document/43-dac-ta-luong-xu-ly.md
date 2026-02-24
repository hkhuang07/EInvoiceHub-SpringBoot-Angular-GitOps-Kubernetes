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
        - Step 7: Phản hồi kết quả API, **lưu ý:** dùng data của các table trong eInvoice, ko dùng data của provider map vào

#### 2.4. API GetStatusInvoices

#### 2.5. API AdjustInvoice

#### 2.6. API ReplaceInvoice