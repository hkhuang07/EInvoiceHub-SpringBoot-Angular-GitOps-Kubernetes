# ....4.2. Tích hợp với NCC HĐĐT

## 1. GIỚI THIỆU CHUNG

Tài liệu này mô tả các yêu cầu chức năng (FR) và thiết kế cơ sở dữ liệu cần thiết để Người dùng thực hiện Chức năng nhập liệu và xác minh thông tin tích hợp với Nhà cung cấp hóa đơn.

### 1.1. Các Đơn vị NCC HĐĐT

<table id="bkmrk-g%C3%B3i-d%E1%BB%8Bch-v%E1%BB%A5-t%C3%ADnh-n%C4%83n" style="width: 92.2632%; height: 147.685px;"><tbody><tr style="height: 29.537px;"><th style="width: 14.5944%; height: 29.537px;">**NCC**

</th><th style="width: 47.7869%; height: 29.537px;">**Thông Tin Chính**

</th><th style="width: 13.8084%;">**Loại bản tin**

</th><th style="width: 23.7754%; height: 29.537px;">**Ghi chú**

</th></tr><tr style="height: 29.537px;"><td style="width: 14.5944%; height: 29.537px;">**BKAV**

</td><td style="width: 47.7869%; height: 29.537px;">PartnerGUID, PartnerToken.

</td><td style="width: 13.8084%;">Json

</td><td style="width: 23.7754%; height: 29.537px;">Ưu tiên 1

</td></tr><tr style="height: 29.537px;"><td style="width: 14.5944%; height: 29.537px;">**VNPT**

</td><td style="width: 47.7869%; height: 29.537px;">Account, ACpass (Webservice Auth)</td><td style="width: 13.8084%;">XML</td><td style="width: 23.7754%; height: 29.537px;"></td></tr><tr style="height: 29.537px;"><td style="width: 14.5944%; height: 29.537px;">**MISA**

</td><td style="width: 47.7869%; height: 29.537px;">accessToken

</td><td style="width: 13.8084%;">JSON

</td><td style="width: 23.7754%; height: 29.537px;">Ưu tiên 3

</td></tr><tr style="height: 29.537px;"><td style="width: 14.5944%; height: 29.537px;">**VIETTEL**

</td><td style="width: 47.7869%; height: 29.537px;">Basic Auth (Username/Password)

</td><td style="width: 13.8084%;">JSON

</td><td style="width: 23.7754%; height: 29.537px;"></td></tr><tr><td style="width: 14.5944%;">**MOBI**

</td><td style="width: 47.7869%;"></td><td style="width: 13.8084%;">JSON

</td><td style="width: 23.7754%;">Ưu tiên 2

</td></tr></tbody></table>

## 1.2 Mục tiêu nghiệp vụ

Khi có một khách hàng mới (doanh nghiệp/đơn vị kinh doanh) sử dụng hệ thống bán hàng, hệ thống cần cho phép:  
\- Khai báo thông tin khách hàng vào hệ thống bán hàng.  
\- Khai báo thêm các thông tin cần thiết để tích hợp với nhà cung cấp hóa đơn điện tử (HĐĐT), nhằm: Tự động gửi dữ liệu hóa đơn bán hàng sang nhà cung cấp HĐĐT, Giảm thao tác nhập liệu, đảm bảo tính đồng bộ giữa hệ thống bán hàng và hệ thống HĐĐT.1.3 Phạm vi

\- Hệ thống: Hệ thống bán hàng PosORA.  
\- Đối tượng: Khách hàng là doanh nghiệp/hộ kinh doanh sử dụng hệ thống (không phải người mua lẻ).  
\- Tích hợp: Các nhà cung cấp HĐĐT (MISA, Viettel, VNPT, …)   
– Cấu hình mang tính tùy chọn: Khách hàng có thể chưa tích hợp ngay khi khai báo và bổ sung sau.

## 2. SƠ ĐỒ USE CASE (USE CASE DIAGRAM)

[![image.png](https://bookstack.softz.vn/uploads/images/gallery/2025-12/scaled-1680-/IHhimage.png)](https://bookstack.softz.vn/uploads/images/gallery/2025-12/IHhimage.png)

### 3.Luồng nghiệp vụ chính

#### 3.1 Khai báo thông tin tích hợp

- Chia theo cụm tương ứng với mỗi store thuộc tenant(vd: tenant có 2 store thì phải chia làm 2 cụm thông tin tích hợp, và cho phép store có thể lựa chọn Tích hợp HĐĐT hoặc không)
- Ngay tại màn hình khai báo hoặc sau khi lưu khách hàng, người dùng có thể cấu hình phần: “Tích hợp hóa đơn điện tử”.  
    Người dùng chọn: Có tích hợp HĐĐT / Không tích hợp (checkbox hoặc switch).
- Nếu chọn "Tích hợp": 
    - Chọn nhà cung cấp hóa đơn muốn tích hợp (**Mỗi lần chỉ được phép tích hợp 1 NCC Hóa đơn điện tử**)
    - Nhập các thông tin kết nối đến NCCHĐ đã chọn: Mục 4.3
- Nếu chọn "Không tích hợp": 
    - Không hiển các thông tin tích hợp
    - Không tạo mới bản tin tại bảng **einv\_invoices** và không gửi dữ liệu sang hệ thống HĐĐT cho khách hàng này.

#### 3.2 Thực hiện tích hợp

Có Nút "Xác thực" --&gt; Sẽ tiến hành thực hiện xác thực thông tin đến NCC HĐĐT tương ứng

- PosORA kiểm tra tính hợp lệ của thông tin nhập và tiến hành test kết nối
- Trả về thông tin kết nối thành công, nếu lỗi thì báo chi tiết trường nhập nào không hợp lệ
- PosORA lưu thông tin tích hợp theo từng khách hàng (lưu lịch sử kết nối)

##### 3.3. Hủy thông tin tích hợp

- Khách hàng không còn nhu cầu Xuất hóa đơn ĐT cho Loại hình kinh doanh --&gt; Hủy thông tin tích hợp
- Đổi sang NCC hóa đơn khác --&gt; PosORA yêu cầu hủy tất cả thông tin tích hợp với NCC cũ

##### 3.4. Sửa thông tin tích hợp

- Khách hàng có nhu cầu điều chỉnh lại thông tin tích hợp --&gt; Thực hiện tích hợp với thông tin mới, giống luồng thêm mới

##### 3.5. Khai báo thông tin Loại/Mẫu/Ký hiệu hóa đơn

- Người dùng chọn: Hóa đơn điện tử do: NCC mặc định/ Người dùng chọn mẫu, ký hiệu hóa đơn.
- Nếu chọn: "Người dùng chọn mẫu, ký hiệu hóa đơn"

### 4. Thông tin các bảng

##### 4.1. Bảng Danh sách Đơn vị Nhà cung cấp HĐĐT - einv\_provider

\- Bảng này do Nhân viên vận hành hệ thống insert trực tiếp vào database, mỗi khi thêm Đơn vị NCC HĐĐT mới cần insert vào bảng này

<table border="1" id="bkmrk-stt-t%C3%AAn-c%E1%BB%99t-gi%C3%A1-tr%E1%BB%8B-" style="border-collapse: collapse; width: 100%; height: 219.931px;"><colgroup><col style="width: 7.62813%;"></col><col style="width: 16.6865%;"></col><col style="width: 29.559%;"></col><col style="width: 46.1263%;"></col></colgroup><tbody><tr style="height: 29.8785px;"><td style="height: 29.8785px;">**STT**</td><td style="height: 29.8785px;">**Tên cột**</td><td style="height: 29.8785px;">**Giá trị**</td><td style="height: 29.8785px;">**Mô tả**</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">1</td><td style="height: 31.6493px;">`id`

</td><td style="height: 31.6493px;">uuid</td><td style="height: 31.6493px;">  
</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">2</td><td style="height: 31.6493px;">`provider_code`</td><td style="height: 31.6493px;">Mã đơn vị</td><td style="height: 31.6493px;">  
</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">3</td><td style="height: 31.6493px;">`provider_name`</td><td style="height: 31.6493px;">Tên đơn vị.</td><td style="height: 31.6493px;">  
</td></tr><tr style="height: 63.4549px;"><td style="height: 63.4549px;">4</td><td style="height: 63.4549px;">`integration_url`</td><td style="height: 63.4549px;">link tích hợp</td><td style="height: 63.4549px;">Môi trường dev: dùng link tích hợp demo của từng đối tác

Trừ VNPT: Sẽ có link cho từng đối tác

</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">5</td><td style="width: 19.7704%; height: 31.6493px;">`is_inactive`

</td><td style="height: 31.6493px;">true/false</td><td style="height: 31.6493px;">Mặc định ban đâu = **false** </td></tr></tbody></table>

##### 4.2. Bảng Danh sách loại hình Hóa đơn điện tử: einv\_invoice\_type

\- Bảng này do Nhân viên vận hành hệ thống insert trực tiếp vào database, mỗi khi thêm 1 loại hình mới cần insert vào bảng này

<table border="1" id="bkmrk-stt-t%C3%AAn-c%E1%BB%99t-gi%C3%A1-tr%E1%BB%8B--1" style="border-collapse: collapse; width: 100%; height: 251.58px;"><colgroup><col style="width: 7.62813%;"></col><col style="width: 16.6865%;"></col><col style="width: 29.559%;"></col><col style="width: 46.1263%;"></col></colgroup><tbody><tr style="height: 29.8785px;"><td style="height: 29.8785px;">**STT**</td><td style="height: 29.8785px;">**Tên cột**</td><td style="height: 29.8785px;">**Giá trị**</td><td style="height: 29.8785px;">**Mô tả**</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">1</td><td style="height: 31.6493px;">`id`

</td><td style="height: 31.6493px;">ID đơn vị</td><td style="height: 31.6493px;">sysadmin tạo cho từng đơn vị khi thực hiện tích hợp</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">2</td><td style="height: 31.6493px;">`invoice_type_name`</td><td style="height: 31.6493px;">Tên đơn vị</td><td style="height: 31.6493px;">  
</td></tr><tr style="height: 31.6493px;"><td style="height: 31.6493px;">3</td><td style="height: 31.6493px;">`sort_order`</td><td style="height: 31.6493px;">Tên đơn vị.</td><td style="height: 31.6493px;">  
</td></tr></tbody></table>

##### 4.3. Bảng Liên kết Store và Provider: einv\_store\_provider

\- Bảng này do Nhân viên vận hành hệ thống insert trực tiếp vào database, mỗi khi thêm 1 loại hình mới cần insert vào bảng này

<table border="1" id="bkmrk-stt-t%C3%AAn-c%E1%BB%99t-gi%C3%A1-tr%E1%BB%8B--2" style="border-collapse: collapse; width: 100%; height: 536.022px;"><colgroup><col style="width: 7.6273%;"></col><col style="width: 16.6847%;"></col><col style="width: 29.5558%;"></col><col style="width: 46.1213%;"></col></colgroup><tbody><tr style="height: 29.8722px;"><td style="height: 29.8722px;">**STT**</td><td style="height: 29.8722px;">**Tên cột**</td><td style="height: 29.8722px;">**Giá trị**</td><td style="height: 29.8722px;">**Mô tả**</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">1</td><td style="height: 31.6335px;">`id`

</td><td style="height: 31.6335px;">uuid</td><td style="height: 31.6335px;">  
</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">2</td><td style="height: 31.6335px;">`tenant_id`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">  
</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">3</td><td style="height: 31.6335px;">`store_id`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">Liên kết theo từng Store\_ID</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">4</td><td style="height: 31.6335px;">`provider_id`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">ID của NCC HĐĐT</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">5</td><td style="height: 31.6335px;">`partner_id`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">Bkav:PartnerGUID

Vnpt: Account

</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">6</td><td style="height: 31.6335px;">`partner_token`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">Bkav: PartnerToken

Vnpt: ACPass

</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">7</td><td style="height: 31.6335px;">`partner_usr`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">MISA: app\_id,

Vnpt: username

</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">8</td><td style="height: 31.6335px;">`partner_pwd`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">Vnpt: password</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">9</td><td style="height: 31.6335px;">`status`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">0: Chưa tích hợp; 1: Tích hợp thành công; 8: In Active</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">10</td><td style="height: 31.6335px;">`integrated_date`</td><td style="height: 31.6335px;">Ngày tích hợp thành công</td><td style="height: 31.6335px;">Khi bấm Xác thực thông tin --&gt; Nhận kết quả thành công thì lưu Ngày hiện tại vào</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">11</td><td style="height: 31.6335px;">`integration_url`</td><td style="height: 31.6335px;">Link tích hợp</td><td style="height: 31.6335px;">Chỉ hiển thị nếu chọn NCC HĐĐT là: VNPT</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">12</td><td style="height: 31.6335px;">`tax_code`</td><td style="height: 31.6335px;">Mã số thuế của Nhà bán hàng</td><td style="height: 31.6335px;">Mặc định từ tenant qua, cho phép điều chỉnh và lưu lại

</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">13</td><td style="height: 31.6335px;">`created_by`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">  
</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">14</td><td style="height: 31.6335px;">`updated_by`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">  
</td></tr><tr style="height: 31.6335px;"><td style="height: 31.6335px;">15</td><td style="height: 31.6335px;">`created_date`</td><td style="height: 31.6335px;">  
</td><td style="height: 31.6335px;">  
</td></tr><tr style="height: 31.6477px;"><td style="height: 31.6477px;">16</td><td style="height: 31.6477px;">`updated_date`</td><td style="height: 31.6477px;">  
</td><td style="height: 31.6477px;">  
</td></tr></tbody></table>