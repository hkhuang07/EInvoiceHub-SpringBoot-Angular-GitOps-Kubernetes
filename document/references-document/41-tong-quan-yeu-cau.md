# ....4.1. Tổng quan yêu cầu

1. **Yêu cầu**
    - PosORA cung cấp cho khách hàng phần mềm bán hàng, nhưng cần phải đáp ứng yêu cầu các bill bán hàng của Khách hàng xuất được Hóa đơn điện tử (HĐĐT) lên Cơ quan thuế
    - PosORA cung cấp giải pháp này thông qua các Đơn vị xuất HĐĐT có liên kết với Cơ quan thuế
    - Khách hàng được phép lựa chọn Đơn vị HĐĐT mà PosORA đã tích hợp
    - 1 Khách hàng (theo tenant) có thể có 1 hoặc nhiều template hóa đơn theo từng thời điểm khác nhau và theo mục đích xuất khác nhau
    - Ký hiệu hóa đơn sẽ có thay đổi qua từng năm theo Nghị định 70
    - Đảm bảo thông tin nhất quán từ PosORA và NCC HĐĐT
2. **Giải pháp**
    - Khách hàng trực tiếp đăng ký Sử dụng HĐĐT với Nhà cung cấp HĐ, cung cấp các giấy tờ cần thiết. Đăng ký thêm Chữ ký số để có thể thực hiện Ký số tự động. Khách hàng lựa chọn Template hóa đơn theo yêu cầu và đúng mục đích sử dụng. Lưu ý phần Hóa đơn GTGT có VAT và Hóa đơn bán hàng (không thể hiện VAT)
    - Sau khi đăng ký với NCC HĐĐT thì cung cấp thông tin tích hợp vào trong hệ thống PosORA và thực hiện tích hợp
    - Cần thiết kế cấu trúc theo Yêu cầu từ mục 1
    - Cần thiết kế mapping Tình trạng hóa đơn giữa các NCC HĐĐT
    - Cần thiết kế Nghiệp vụ tương đồng với mỗi NCC, nếu NCC ko đáp ứng được Nghiệp vụ thì cần phải off tính năng
3. **Nghiệp vụ**  
    
    1. Danh sách Chứng từ cần xuất HĐĐT
    2. Tích hợp từ hệ thống PosORA đến NCC HĐĐT để xác minh thông tin tích hợp **(904)**
    3. Xuất hóa đơn lên hệ thống NCC HĐĐT **(100)**
    4. Ký số (Phát hành) hóa đơn **(205)**
    5. Ký số tự động hóa đơn theo khung giờ
    6. Thay thế hóa đơn
    7. Điều chỉnh hóa đơn (bằng chứng từ Nhập trả, Nhập trả bán lẻ)
    8. Lấy thông tin full của 1 Hóa đơn **(800)**
    9. Lấy trạng thái hóa đơn của NCC HĐĐT **(850)**
    10. Xuất hóa đơn theo block
    11. **Lấy thông tin hóa đơn trả về PDF -**
    12. Xuất hóa đơn tự động theo khung giờ
4. **Định hướng**
    1. Xây dựng thành HUB độc lập, xem PosORA là 1 đơn vị Merchant tích hợp đến HUB --&gt; Cần BA cách vận hành như Portal, User, Kiến trúc tổng quan, Cấu trúc code và Cấu trúc database
    2. Vấn đề bảo mật đầu vào