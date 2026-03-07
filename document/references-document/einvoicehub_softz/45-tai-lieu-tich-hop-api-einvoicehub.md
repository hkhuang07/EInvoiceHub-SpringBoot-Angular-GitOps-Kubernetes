# ....4.5. Tài liệu tích hợp API - EINVOICEHUB

## **1. GIỚI THIỆU CHUNG**

## **2. ĐẶC TẢ QUY TRÌNH KẾT NỐI VÀ CẤU TRÚC MESSAGE** 

## **<span class="TextRun SCXW182286961 BCX8" data-contrast="none" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW182286961 BCX8" data-ccp-parastyle="heading 1">3. API </span><span class="NormalTextRun SCXW182286961 BCX8" data-ccp-parastyle="heading 1">INTEGRATION</span></span><span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}"> </span>**

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.1. API ... - Tích hợp eInvoiceHub</span>**

<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">(Đang cập nhật)</span>

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.2. API SubmitInvoice</span>**

 **3.2.1 Header**

<div id="bkmrk-stt-name-values-desc" style="mso-element: comment-list;"><table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" style="width: 1032px; border-collapse: collapse; border: none;" width="655"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 76.6146px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 233.958px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 81.986px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 449.976px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 76.6146px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 233.958px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 81.986px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 449.976px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 76.6146px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 233.958px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 81.986px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 449.976px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 76.6146px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 233.958px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 81.986px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 449.976px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="mso-comment-continuation: 2;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span></span><span style="mso-comment-continuation: 2;"><span class="MsoCommentReference"><span style="font-size: 8.0pt;"></span></span></span><span class="MsoCommentReference"><span style="font-size: 8.0pt;"></span></span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 76.6146px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 233.958px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 81.986px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 449.976px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>

</td></tr></tbody></table>

</div><div id="bkmrk-" style="mso-element: comment-list;"><div style="mso-element: comment-list;"><div style="mso-element: comment;"><div class="msocomtxt" id="bkmrk--1" language="JavaScript" onmouseout="msoCommentHide('_com_2')" onmouseover="msoCommentShow('_anchor_2','_com_2')"></div></div></div></div><div id="bkmrk--2" style="mso-element: comment-list;"><div style="mso-element: comment;"><div class="msocomtxt" language="JavaScript" onmouseout="msoCommentHide('_com_1')" onmouseover="msoCommentShow('_anchor_1','_com_1')"><span style="mso-comment-author: 'Nguyen Chi Nhan'; mso-comment-providerid: AD; mso-comment-userid: 'S::nhan\.nguyen\@softz\.vn::d3f2a1c2-b1d9-4fe9-9769-24accbea2fac';"></span></div></div></div><div id="bkmrk--3" style="mso-element: comment-list;"><div style="mso-element: comment;"><div class="msocomtxt" id="bkmrk--4" language="JavaScript" onmouseout="msoCommentHide('_com_2')" onmouseover="msoCommentShow('_anchor_2','_com_2')"></div></div></div> **3.2.2 Request**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-lv-parameter-re%2A-dat" style="width: 1023px; border-collapse: collapse; border: none; height: 911.824px;" width="652"><tbody><tr style="height: 42.9883px;"><td style="width: 72.5903px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 42.9883px;" valign="top" width="36">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lv</span>**

</td><td style="width: 240.125px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 42.9883px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 78.9965px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 42.9883px;" valign="top" width="42">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Re\*</span>**

</td><td style="width: 150.993px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 42.9883px;" valign="top" width="114">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 293.997px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 42.9883px;" valign="top" width="257">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 71.25px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 71.25px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 71.25px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestDateTime</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 71.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 71.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 71.25px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request submission time</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DDTHH:mm:ssZ</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ex: 2</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">024-01-02T10:16:01Z</span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID of the request</span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UserAgent</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 32.3242px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 32.3242px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 32.3242px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 32.3242px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 32.3242px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 32.3242px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoiceType</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(3)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng SubmitInvoiceType</span>*

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(1,50)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID Hóa đơn của Partner gửi đến <span style="mso-comment-continuation: 2;">eInvoice</span></span><span style="mso-comment-continuation: 2;"><span class="MsoCommentReference"><span style="font-size: 8.0pt;"></span></span></span><span class="MsoCommentReference"><span style="font-size: 8.0pt;"></span></span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceTypeID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng InvoiceTypeID</span>*

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceDate</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(10)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DD</span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceSeries</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PaymentMethodID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng PaymentMethodID</span>*

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerTaxCode</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerCompany</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td><td rowspan="3" style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 57.8907px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Phải có 1 trong 3 thông tin: BuyerCompany, BuyerName, BuyerAddress</span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerName</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,200)</span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerAddress</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">C</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td></tr><tr style="height: 22.4609px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 22.4609px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4609px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerIDNo</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4609px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4609px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.4609px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerMobile</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 14.9883px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 14.9883px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 14.9883px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBankAccount</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 14.9883px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 14.9883px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 14.9883px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBankName</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,200)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerBudgetCode</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReceiveTypeID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng ReceiveTypeID</span>*

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReceiverEmail</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,50)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">CurrencyCode</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,3)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = “VND”</span>

</td></tr><tr style="height: 22.1875px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ExchangeRate</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 22.1875px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 1.0</span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Notes</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,300)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 18.75px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Details</span>**

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 18.75px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 36.25px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemTypeID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng ItemTypeID</span>*

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 1 (Hàng hóa, dịch vụ)</span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemCode</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(0,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemName</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(1,300)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UnitName</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(1,20)</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Quantity</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Price</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 36.25px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">GrossAmount</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thành tiền. </span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = Quantity \* Price</span>

</td></tr><tr style="height: 19.2969px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountRate</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.2969px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Chiết khấu. Vd: 10.0</span>

</td></tr><tr style="height: 37.3438px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 37.3438px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 37.3438px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountAmount</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 37.3438px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 37.3438px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 37.3438px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền Chiết khấu.</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = GrossAmount \* DiscountRate/100</span>

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxTypeID</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng TaxTypeID</span>*

</td></tr><tr style="height: 20px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxRate</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Thuế. Vd: 8.0</span>

</td></tr><tr style="height: 36.25px;"><td style="width: 72.5903px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="36"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 240.125px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAmount</span>

</td><td style="width: 78.9965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 150.993px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 293.997px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 36.25px;" valign="top" width="257"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền thuế. Mặc định = (GrossAmount – DiscountAmount) \* TaxRate/100</span>

</td></tr></tbody></table>

<div id="bkmrk--5" style="mso-element: comment-list;">  
<div style="mso-element: comment;"></div></div><div id="bkmrk--6" style="mso-element: comment-list;"><div style="mso-element: comment;"><div class="msocomtxt" id="bkmrk--7" language="JavaScript" onmouseout="msoCommentHide('_com_2')" onmouseover="msoCommentShow('_anchor_2','_com_2')"></div></div></div> **3.2.3 Response Success**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ" style="width: 1030px; border-collapse: collapse; border: none; height: 298.42px;" width="653"><tbody><tr style="height: 21.1111px;"><td style="width: 75.219px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 239.573px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 80.5729px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 154.514px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 290.556px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 200</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number </span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 0: Thành công</span>

</td></tr><tr style="height: 23.9757px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 23.9757px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 23.9757px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 23.9757px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 23.9757px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 23.9757px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceNo</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceLookupCode </span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã tra cứu hóa đơn</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAuthorityCode</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã của Cơ quan thuế</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Provider</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mã của NCC HĐĐT</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ProviderInvoiceID</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID hóa đơn của NCC HĐĐT</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 75.219px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 239.573px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UrlLookup</span>

</td><td style="width: 80.5729px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="65"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 154.514px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 290.556px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Link tra cứu của NCC HĐĐT</span>

</td></tr></tbody></table>

 **3.2.4 Response Error**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-1" style="width: 842px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes;"><td style="width: 76.496px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 238.172px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 84.2578px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 158.965px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 285.957px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1;"><td style="width: 76.496px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 238.172px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 84.2578px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 158.965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 285.957px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 76.496px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 238.172px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 84.2578px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 158.965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 285.957px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 76.496px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 238.172px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 84.2578px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 158.965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String </span>

</td><td style="width: 285.957px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 1: thất bại</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 76.496px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 238.172px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseDesc</span>

</td><td style="width: 84.2578px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 158.965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 285.957px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả nội dung</span>

</td></tr><tr style="mso-yfti-irow: 5; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 76.496px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 238.172px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>

</td><td style="width: 84.2578px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 158.965px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 285.957px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr></tbody></table>

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.3. API SignInvoices</span>**

 **3.3.1 Header**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-stt-name-values-desc-1" style="border-collapse: collapse; border: none; width: 101.19%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 14.25pt;"><td style="width: 9.77645%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 28.0182%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 10.4914%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 51.7437%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 14.25pt;"><td style="width: 9.77645%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0182%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 10.4914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 51.7437%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 14.25pt;"><td style="width: 9.77645%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0182%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 10.4914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 51.7437%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 9.77645%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0182%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 10.4914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 51.7437%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 14.25pt;"><td style="width: 9.77645%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 28.0182%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 10.4914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 51.7437%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>*

</td></tr></tbody></table>

 **3.3.2 Request**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-lv-parameter-re%2A-dat-1" style="border-collapse: collapse; border: none; width: 100.952%; height: 261.981px;"><tbody><tr style="height: 21.9922px;"><td style="width: 9.77769%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.9922px;" valign="top" width="35">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lv</span>**

</td><td style="width: 28.1359%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.9922px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.3726%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.9922px;" valign="top" width="42">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Re\*</span>**

</td><td style="width: 19.076%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.9922px;" valign="top" width="114">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 32.5484%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.9922px;" valign="top" width="256">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 56.7383px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 56.7383px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 56.7383px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestDateTime</span>

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 56.7383px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 56.7383px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 56.7383px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request submission time</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DDTHH:mm:ssZ</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ex: 2024-01-02T10:16:01Z</span>

</td></tr><tr style="height: 20px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 20px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID of the request</span>

</td></tr><tr style="height: 20px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 20px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UserAgent</span>

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 20px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 36.25px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 36.25px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 36.25px;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 36.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 36.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 36.25px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Danh sách Hóa đơn. Tối đa 30 hóa đơn</span>

</td></tr><tr style="height: 53.75px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 53.75px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.75px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">IDType</span>

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.75px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.75px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.75px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 0.</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: InvoiceID</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: PartnerInvoiceID</span>

</td></tr><tr style="height: 53.25px;"><td style="width: 9.77769%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 53.25px;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.1359%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.25px;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.3726%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.25px;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.076%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.25px;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(36,50)</span>

</td><td style="width: 32.5484%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 53.25px;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: thì gửi ID của eInvoice (InvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: thì gửi ID của Partner gửi đến (PartnerInvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr></tbody></table>

 **3.3.3 Response Success**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-2" style="border-collapse: collapse; border: none; width: 100.714%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 9.77742%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.4976%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.0159%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.4277%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 32.3211%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 200</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 14.25pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number </span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 0: Thành công</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 14.25pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="mso-yfti-irow: 6; height: 14.25pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="mso-yfti-irow: 7; mso-yfti-lastrow: yes; height: 24.0pt;"><td style="width: 9.77742%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 24pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.4976%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 24pt;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SignedDate</span>

</td><td style="width: 10.0159%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 24pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.4277%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 24pt;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.3211%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 24pt;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">YYYY-MM-DD</span>

</td></tr></tbody></table>

 **3.3.4 Response Error**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-3" style="border-collapse: collapse; border: none; width: 100%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 10.2544%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.2584%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="175">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.2551%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.0779%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="98">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 32.194%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="240">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 10.2544%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.2584%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="175"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.2551%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.0779%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="98"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.194%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="240"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 14.25pt;"><td style="width: 10.2544%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.2584%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="175"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.2551%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.0779%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="98"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.194%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="240"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 14.25pt;"><td style="width: 10.2544%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.2584%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="175"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.2551%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.0779%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="98"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String </span>

</td><td style="width: 32.194%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="240"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 1: thất bại</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 14.25pt;"><td style="width: 10.2544%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.2584%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="175"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseDesc</span>

</td><td style="width: 10.2551%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.0779%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="98"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.194%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="240"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả nội dung</span>

</td></tr><tr style="mso-yfti-irow: 5; mso-yfti-lastrow: yes; height: 14.25pt;"><td style="width: 10.2544%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.2584%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="175"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>

</td><td style="width: 10.2551%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.0779%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="98"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 32.194%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="240"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr></tbody></table>

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.4. API GetInvoices </span>**

 **3.4.1 Header**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-stt-name-values-desc-2" style="border-collapse: collapse; border: none; width: 100.833%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 14.25pt;"><td style="width: 10.493%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 27.9012%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 10.8506%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 50.6757%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="337">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>*

</td></tr></tbody></table>

 **3.4.2 Request**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-lv-parameter-re%2A-dat-2" style="border-collapse: collapse; border: none; width: 100.119%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 10.8506%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="35">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lv</span>**

</td><td style="width: 27.7822%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.6121%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="42">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Re\*</span>**

</td><td style="width: 19.1971%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="114">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 31.4785%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="256">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestDateTime</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request submission time</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DDTHH:mm:ssZ</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ex: 2024-01-02T10:16:01Z</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID of the request</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UserAgent</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Danh sách Hóa đơn. Tối đa 30 hóa đơn</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 14.25pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">IDType</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 0.</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: InvoiceID</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: PartnerInvoiceID</span>

</td></tr><tr style="mso-yfti-irow: 6; mso-yfti-lastrow: yes; height: 14.25pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(36,50)</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: thì gửi ID của eInvoice (InvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: thì gửi ID của Partner gửi đến (PartnerInvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr></tbody></table>

 **3.4.3 Response Success**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-4" style="border-collapse: collapse; border: none; width: 100%; height: 1023.96px;"><tbody><tr style="height: 19.9805px;"><td style="width: 10.6121%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.0188%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.7332%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.3164%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 31.2401%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 19.9805px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 200</span>

</td></tr><tr style="height: 19.9805px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number </span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 0: Thành công</span>

</td></tr><tr style="height: 19.9805px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.9805px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng InvoiceTypeID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReferenceTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng RefecenceTypeID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceDate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">YYYY-MM-DD</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceNo</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PaymentMethodID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng PaymentMethodID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerTaxCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerCompany</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerAddress</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerIDNo</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerMobile</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBankAccount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBankName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBudgetCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ReceiveTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng ReceiveTypeID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ReceiverEmail</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">CurrencyCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ExchangeRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">InvoiceLookupCode </span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã tra cứu hóa đơn</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TaxAuthorityCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã của Cơ quan thuế</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Provider</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã của NCC HĐĐT</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ProviderInvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ID hóa đơn của NCC HĐĐT</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">UrlLookup</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Link tra cứu của NCC HĐĐT</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">GrossAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">DiscountAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TaxAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TotalAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">SignedDate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">YYYY-MM-DD</span>

</td></tr><tr style="height: 23.0469px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 23.0469px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0469px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">InvoiceStatusID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0469px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0469px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0469px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng InvoiceStatusID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Notes</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Details</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">JSONArray</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ItemTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng ItemTypeID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ItemCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UnitName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Quantity</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Price</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 22.5px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">GrossAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thành tiền</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Chiết khấu.</span>

</td></tr><tr style="height: 21.3438px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.3438px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3438px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3438px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3438px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3438px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền Chiết khấu.</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng TaxTypeID</span>*

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Thuế.</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền thuế.</span>

</td></tr><tr style="height: 19.4922px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TotalAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 19.4922px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr></tbody></table>

 **3.4.4 Response Error**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-5" style="border-collapse: collapse; border: none; width: 100.238%; height: 140.469px;"><tbody><tr style="height: 21.1111px;"><td style="width: 10.4952%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.1337%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.611%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.7914%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 30.8793%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 34.9132px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String </span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 1: thất bại</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseDesc</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả nội dung</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr></tbody></table>

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.5. API GetInvoiceStatus</span>**

 **3.4.1 Header**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-stt-name-values-desc-3" style="border-collapse: collapse; border: none; width: 100.833%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 14.25pt;"><td style="width: 10.493%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 27.9012%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 10.8506%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 50.6757%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 14.25pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 10.493%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 27.9012%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 10.8506%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.6757%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="337">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>*

</td></tr></tbody></table>

 **3.4.2 Request**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-lv-parameter-re%2A-dat-3" style="border-collapse: collapse; border: none; width: 100.119%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 10.8506%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="35">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lv</span>**

</td><td style="width: 27.7822%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.6121%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="42">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Re\*</span>**

</td><td style="width: 19.1971%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="114">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 31.4785%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="256">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestDateTime</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Request submission time</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Format: YYYY-MM-DDTHH:mm:ssZ</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ex: 2024-01-02T10:16:01Z</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ID of the request</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UserAgent</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 15pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="203">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 15pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Danh sách Hóa đơn. Tối đa 30 hóa đơn</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 14.25pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">IDType</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mặc định = 0.</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: InvoiceID</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: PartnerInvoiceID</span>

</td></tr><tr style="mso-yfti-irow: 6; mso-yfti-lastrow: yes; height: 14.25pt;"><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="35"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="203"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="42"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="114"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String(36,50)</span>

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 14.25pt;" valign="top" width="256"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0: thì gửi ID của eInvoice (InvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1: thì gửi ID của Partner gửi đến (PartnerInvoiceID)</span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr><td style="width: 10.8506%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>**

</td><td style="width: 27.7822%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ForceSynC</span>**

</td><td style="width: 10.6121%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>**

</td><td style="width: 19.1971%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Boolean</span>**

</td><td style="width: 31.4785%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">False : không cập nhật trạng thái từ CQT qua Provider</span>**

**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">True : cập nhật trạng thái từ CQT qua Provider</span>**

</td></tr></tbody></table>

 **3.4.3 Response Success**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-6" style="border-collapse: collapse; border: none; width: 100%; height: 1101.3px;"><tbody><tr style="height: 21.1111px;"><td style="width: 10.6121%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.0188%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.7332%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.3164%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 31.2401%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 200</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number </span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 0: Thành công</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">JSONArray</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerInvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng InvoiceTypeID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReferenceTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng RefecenceTypeID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceDate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">YYYY-MM-DD</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceForm</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceNo</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; background: white; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PaymentMethodID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng PaymentMethodID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerTaxCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerCompany</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BuyerAddress</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerIDNo</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerMobile</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBankAccount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBankName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">BuyerBudgetCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ReceiveTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng ReceiveTypeID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ReceiverEmail</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">CurrencyCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ExchangeRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>**

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">InvoiceLookupCode </span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>**

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>**

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã tra cứu hóa đơn</span>**

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TaxAuthorityCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã của Cơ quan thuế</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Provider</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Mã của NCC HĐĐT</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ProviderInvoiceID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ID hóa đơn của NCC HĐĐT</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>**

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">UrlLookup</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>**

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>**

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Link tra cứu của NCC HĐĐT</span>**

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">GrossAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">DiscountAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TaxAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">TotalAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>**

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">SignedDate</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>**

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>**

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">YYYY-MM-DD</span>**

</td></tr><tr style="height: 23.0382px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 23.0382px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>**

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0382px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">InvoiceStatusID</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0382px;" valign="top" width="64">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>**

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0382px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>**

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 23.0382px;" valign="top" width="243">***<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng InvoiceStatusID</span>***

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Notes</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">2</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Details</span>**

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">JSONArray</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ItemTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">Xem bảng ItemTypeID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">ItemCode</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman'; color: black; mso-themecolor: text1;"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">UnitName</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Quantity</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Price</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr><tr style="height: 22.5px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">GrossAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 22.5px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thành tiền</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Chiết khấu.</span>

</td></tr><tr style="height: 21.3194px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.3194px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3194px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">DiscountAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3194px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3194px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.3194px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền Chiết khấu.</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxTypeID</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xem bảng TaxTypeID</span>*

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxRate</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">% Thuế.</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxAmount</span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Số tiền thuế.</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.6121%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0188%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="174"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TotalAmount<span style="mso-spacerun: yes;"> </span></span>

</td><td style="width: 10.7332%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="64"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.3164%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Number</span>

</td><td style="width: 31.2401%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td></tr></tbody></table>

 **3.4.4 Response Error**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-7" style="border-collapse: collapse; border: none; width: 100.238%; height: 140.469px;"><tbody><tr style="height: 21.1111px;"><td style="width: 10.4952%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.1337%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.611%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.7914%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 30.8793%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 34.9132px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseCode</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 34.9132px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">RequestID</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Status</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String </span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">= 1: thất bại</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ResponseDesc</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Y</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả nội dung</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 10.4952%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="71"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.1337%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data</span>

</td><td style="width: 10.611%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="84"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">N</span>

</td><td style="width: 19.7914%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="99"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">String</span>

</td><td style="width: 30.8793%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.25pt; height: 21.1111px;" valign="top" width="243"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Object</span>

</td></tr></tbody></table>

#### **<span class="EOP SCXW182286961 BCX8" data-ccp-props="{"134245418":true,"134245529":true,"201341983":0,"335559685":360,"335559738":360,"335559739":80,"335559740":278}">3.6. API SubmitAdjustInvoice </span>**

 **3.3.1 Header**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-stt-name-values-desc-4" style="border-collapse: collapse; border: none; width: 100.476%;"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 10.495%; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="72">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">STT</span>**

</td><td style="width: 28.0147%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Name</span>**

</td><td style="width: 10.9687%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="90">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Values</span>**

</td><td style="width: 50.4322%; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="337">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 10.495%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 28.0147%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td><td style="width: 10.9687%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Text/plain</span>

</td><td style="width: 50.4322%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Content type</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 10.495%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 28.0147%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">X-Session-ID</span>

</td><td style="width: 10.9687%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid.v4()</span>

</td><td style="width: 50.4322%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Self-generated ID</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 10.495%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 28.0147%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Signature</span>

</td><td style="width: 10.9687%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.4322%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Payload data signature</span>

</td></tr><tr style="mso-yfti-irow: 4; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 10.495%; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="72"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 28.0147%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="156"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PartnerID</span>

</td><td style="width: 10.9687%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="90"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';"> </span>

</td><td style="width: 50.4322%; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="337">*<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Uuid(). Mã này do eInvoice cấp</span>*

</td></tr></tbody></table>

 **3.3.2 Request**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-8" style="border-collapse: collapse; width: 1017px; border-spacing: 0px; height: 1807.25px; border: 1px solid rgb(0, 0, 0);"><thead><tr style="height: 25.5625px;"><td style="width: 89.07px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 25.5625px;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 235.148px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 25.5625px;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 89.082px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 25.5625px;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 166.133px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 25.5625px;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 259.316px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 25.5625px;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr></thead><tbody><tr style="height: 65.3125px;"><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">1

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203">RequestDateTime

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Request submission time

Format: YYYY-MM-DDTHH:mm:ssZ

Ex: 2024-01-02T10:16:01Z

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">1

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203">RequestID

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">ID of the request

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">1

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203">UserAgent

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"></td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">1

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203">**Data**

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Object

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"></td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> SubmitInvoiceType

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(3)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng SubmitInvoiceType*

</td></tr><tr style="height: 65.3125px;"><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 235.148px;"> ReferenceTypeID

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.082px;">Y

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 166.133px;">Number

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 259.316px;">Giá trị cố định = 2: Xác định đây là hóa đơn điều chỉnh. Xem bảng ReferenceTypeID

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;"> OrgInvoiceForm

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;">String(20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;">Mẫu số hóa đơn gốc

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;"> OrgInvoiceSeries

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;">String(20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;">Ký hiệu hóa đơn gốc

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;"> OrgInvoiceNo

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;">String(20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;">Số hóa đơn gốc

</td></tr><tr style="height: 65.3125px;"><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 235.148px;"> OrgInvoiceReason

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.082px;">Y

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 166.133px;">String(500)

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 259.316px;">Lý do điều chỉnh hóa đơn gốc (sẽ hiển thị trên bản in hóa đơn điều chỉnh)

</td></tr><tr style="height: 114.062px;"><td style="height: 114.062px; border-color: rgb(0, 0, 0); width: 89.07px;">2

</td><td style="height: 114.062px; border-color: rgb(0, 0, 0); width: 235.148px;"> IsIncrease

</td><td style="height: 114.062px; border-color: rgb(0, 0, 0); width: 89.082px;">N

</td><td style="height: 114.062px; border-color: rgb(0, 0, 0); width: 166.133px;">Boolean

</td><td style="height: 114.062px; border-color: rgb(0, 0, 0); width: 259.316px;">Trường đặc biệt cho điều chỉnh:

true = Điều chỉnh tăng số tiền;

false = Điều chỉnh giảm số tiền; null/không truyền = Điều chỉnh thông tin (không thay đổi số tiền)

</td></tr><tr style="height: 48.5625px;"><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> PartnerInvoiceID

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(50)

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">ID Hóa đơn của Partner gửi đến eInvoice

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> InvoiceTypeID

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng InvoiceTypeID*

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> InvoiceDate

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(10)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Format: YYYY-MM-DD

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> InvoiceForm

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Mẫu số hóa đơn điều chỉnh

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> InvoiceSeries

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Ký hiệu hóa đơn điều chỉnh

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> PaymentMethodID

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng PaymentMethodID*

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerTaxCode

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Mã số thuế người mua

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerCompany

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">C

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,300)

</td><td rowspan="3" style="height: 95.3125px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Phải có 1 trong 3 thông tin: BuyerCompany, BuyerName, BuyerAddress

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerName

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">C

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,200)

</td></tr><tr style="height: 31.8125px;"><td style="height: 31.8125px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.8125px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerAddress

</td><td style="height: 31.8125px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">C

</td><td style="height: 31.8125px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,300)

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerIDNo

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Số CCCD/Hộ chiếu người mua

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerMobile

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Số điện thoại người mua

</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerBankAccount

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Tài khoản ngân hàng người mua

</td></tr><tr style="height: 48.5625px;"><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerBankName

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,200)

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Tên tài khoản ngân hàng người mua

</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> BuyerBudgetCode

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Mã số đơn vị quan hệ ngân sách

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ReceiveTypeID

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng ReceiveTypeID*

</td></tr><tr style="height: 48.5625px;"><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ReceiverEmail

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,50)

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Email nhận hóa đơn của người mua

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> CurrencyCode

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,3)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Mã tiền tệ. Mặc định = "VND"

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ExchangeRate

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Tỷ giá. Mặc định = 1.0

</td></tr><tr style="height: 46.9167px;"><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> Notes

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,300)

</td><td style="height: 46.9167px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Ghi chú trên hóa đơn điều chỉnh

</td></tr><tr style="height: 48.5625px;"><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">2

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> **Details**

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">JSONArray

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Danh sách chi tiết hàng hóa/dịch vụ điều chỉnh

</td></tr><tr style="height: 36.6875px;"><td style="height: 36.6875px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 36.6875px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ItemTypeID

</td><td style="height: 36.6875px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 36.6875px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 36.6875px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng ItemTypeID*

Mặc định = 1 (Hàng hóa, dịch vụ)

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ItemCode

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(0,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Mã hàng hóa, dịch vụ

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> ItemName

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(1,300)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Tên hàng hóa, dịch vụ

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> UnitName

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String(1,20)

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Đơn vị tính

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> Quantity

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Số lượng hàng hóa, dịch vụ

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> Price

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">Y

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257"> Đơn giá hàng hóa (Chưa thuế)

</td></tr><tr style="height: 48.5625px;"><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> GrossAmount

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 48.5625px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Thành tiền.

Mặc định = Quantity \* Price

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> DiscountRate

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">% Chiết khấu. Vd: 10.0

</td></tr><tr style="height: 65.3125px;"><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> DiscountAmount

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Số tiền Chiết khấu.

Mặc định = GrossAmount \* DiscountRate/100

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> TaxTypeID

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">String

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">*Xem bảng TaxTypeID*

</td></tr><tr style="height: 31.75px;"><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> TaxRate

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 31.75px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">% Thuế. Vd: 8.0

</td></tr><tr style="height: 65.3125px;"><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.07px;" valign="top" width="36">3

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 235.148px;" valign="top" width="203"> TaxAmount</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 89.082px;" valign="top" width="42">N

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 166.133px;" valign="top" width="114">Number

</td><td style="height: 65.3125px; border-color: rgb(0, 0, 0); width: 259.316px;" valign="top" width="257">Số tiền thuế. Mặc định = (GrossAmount – DiscountAmount) \* TaxRate/100

</td></tr></tbody></table>

 **3.3.3 Response Success**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-9" style="width: 100%; border-collapse: collapse; border-width: 1px; border-spacing: 0px; border-color: rgb(0, 0, 0);"><tbody><tr><td style="width: 10.611%; border: 1pt solid rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.0179%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.611%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.7914%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 30.8793%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174">ResponseCode

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">= 200

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174">RequestID

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">RequestID

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174">Status

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">Number

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">= 0: Thành công

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174">**Data**

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">Object

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> PartnerInvoiceID

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243"> ID Hóa đơn của Partner

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> InvoiceID

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243"> ID hóa đơn trên eInvoice

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> InvoiceForm

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243"> Mẫu số hóa đơn điều chỉnh

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> InvoiceNo

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243"> Số hóa đơn điều chỉnh

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> InvoiceLookupCode

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">Mã tra cứu hóa đơn

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> TaxAuthorityCode

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">Mã của Cơ quan thuế

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> Provider

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">Mã của NCC HĐĐT

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> ProviderInvoiceID

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">ID hóa đơn của NCC HĐĐT

</td></tr><tr><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="72">2

</td><td style="width: 28.0179%; border-color: rgb(0, 0, 0);" valign="top" width="174"> dUrlLookup

</td><td style="width: 10.611%; border-color: rgb(0, 0, 0);" valign="top" width="65">Y

</td><td style="width: 19.7914%; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; border-color: rgb(0, 0, 0);" valign="top" width="243">Link tra cứu của NCC HĐĐT

</td></tr></tbody></table>

 **3.3.4 Response Error**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-level-parameter-requ-10" style="width: 100%; height: 197.031px; border-collapse: collapse; border-width: 1px; border-spacing: 0px; border-color: rgb(0, 0, 0);"><tbody><tr style="height: 30.0391px;"><td style="width: 10.611%; border: 1pt solid rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="71">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Level</span>**

</td><td style="width: 28.0179%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="156">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Parameter</span>**

</td><td style="width: 10.611%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="84">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Require</span>**

</td><td style="width: 19.7914%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="99">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Data Type</span>**

</td><td style="width: 30.8793%; border-top: 1pt solid rgb(0, 0, 0); border-right: 1pt solid rgb(0, 0, 0); border-bottom: 1pt solid rgb(0, 0, 0); border-image: initial; border-left: none rgb(0, 0, 0); background: rgb(191, 191, 191); padding: 0in 5.25pt; height: 15pt;" valign="top" width="243">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Description</span>**

</td></tr><tr style="height: 46.8359px;"><td style="width: 10.611%; height: 46.8359px; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; height: 46.8359px; border-color: rgb(0, 0, 0);" valign="top" width="156">ResponseCode

</td><td style="width: 10.611%; height: 46.8359px; border-color: rgb(0, 0, 0);" valign="top" width="84">Y

</td><td style="width: 19.7914%; height: 46.8359px; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; height: 46.8359px; border-color: rgb(0, 0, 0);" valign="top" width="243">&lt;&gt; 200; Xem bảng Phụ lục mã lỗi trả về

</td></tr><tr style="height: 30.0391px;"><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="156">RequestID

</td><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="84">Y

</td><td style="width: 19.7914%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="243">RequestID

</td></tr><tr style="height: 30.0391px;"><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="156">Status

</td><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="84">Y

</td><td style="width: 19.7914%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="243">= 1: thất bại

</td></tr><tr style="height: 30.0391px;"><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="156">ResponseDesc

</td><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="84">Y

</td><td style="width: 19.7914%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="243">Mô tả nội dung

</td></tr><tr style="height: 30.0391px;"><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="72">1

</td><td style="width: 28.0179%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="156">Data

</td><td style="width: 10.611%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="84">N

</td><td style="width: 19.7914%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="99">String

</td><td style="width: 30.8793%; height: 30.0391px; border-color: rgb(0, 0, 0);" valign="top" width="243">Object

</td></tr></tbody></table>

## **<span class="TextRun SCXW182286961 BCX8" data-contrast="none" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW182286961 BCX8" data-ccp-parastyle="heading 1">4. PHỤ LỤC</span></span>**

 **4.1 Danh sách ProviderID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-provider-t%C3%AAn-nh%C3%A0-cun" style="width: 1031px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 142.625px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="109">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Provider</span>**

</td><td style="width: 698.969px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="545">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tên Nhà cung cấp hóa đơn điện tử</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 142.625px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="109"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">BKAV</span>

</td><td style="width: 698.969px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="545"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Công ty cổ phần Bkav</span>

</td></tr><tr style="mso-yfti-irow: 2; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 142.625px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="109"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">MOBI</span>

</td><td style="width: 698.969px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="545"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mobifone Invoice</span>

</td></tr></tbody></table>

 **4.2 Danh sách SubmitInvoiceType**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-submitinvoicetype-ap" style="width: 1038px; border-collapse: collapse; border: none; height: 200px;" width="651"><tbody><tr style="height: 20px;"><td style="width: 145.266px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 20px;" valign="top" width="160">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoiceType</span>**

</td><td style="width: 307.266px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 20px;" valign="top" width="164">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">API áp dụng</span>**

</td><td style="width: 391.297px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 20px;" valign="top" width="327">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">100</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo hóa đơn mới, chưa cấp số hóa đơn</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">101</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo hóa đơn mới, cấp sẵn số hóa đơn, chưa ký</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">102</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo mới hóa đơn và ký phát hành luôn</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">110</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Sửa hóa đơn</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">201</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo hóa đơn mới điều chỉnh, cấp sẵn số hóa đơn, chưa ký</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">202</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo mới hóa đơn điều chỉnh và ký phát hành luôn</span>

</td></tr><tr style="height: 20px;"><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">210</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitAdjustInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Sửa hóa đơn điều chỉnh</span>

</td></tr><tr><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">301</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitReplaceInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo hóa đơn mới thay thế, cấp sẵn số hóa đơn, chưa ký</span>

</td></tr><tr><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">302</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitReplaceInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="327"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tạo mới hóa đơn thay thế và ký phát hành luôn</span>

</td></tr><tr><td style="width: 145.266px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 20px;" valign="top" width="160"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">310</span>

</td><td style="width: 307.266px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;" valign="top" width="164"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SubmitReplaceInvoice</span>

</td><td style="width: 391.297px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 20px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Sửa hóa đơn thay thế</span>

</td></tr></tbody></table>

 **4.3 Danh sách ReferenceTypeID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-referencetypeid-lo%E1%BA%A1i" style="width: 1045px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 144.133px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="117">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReferenceTypeID</span>**

</td><td style="width: 704.469px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="537">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Loại hóa đơn</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 144.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0</span>

</td><td style="width: 704.469px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn gốc</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 144.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 704.469px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn điều chỉnh</span>

</td></tr><tr style="mso-yfti-irow: 3; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 144.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 704.469px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn thay thế</span>

</td></tr></tbody></table>

 **4.4 Danh sách InvoiceTyopeID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-invoicetypeid-lo%E1%BA%A1i-h" style="width: 1043px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 146.625px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="117">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceTypeID</span>**

</td><td style="width: 700.969px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="537">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Loại hóa đơn</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 146.625px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 700.969px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn Giá trị gia tăng</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 146.625px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 700.969px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn bán hàng</span>

</td></tr><tr style="mso-yfti-irow: 3; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 146.625px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">6</span>

</td><td style="width: 700.969px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Phiếu xuất kho &amp; vận chuyển nội bộ</span>

</td></tr></tbody></table>

 **4.5 Danh sách PaymentMethodID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-paymentmethodid-t%C3%AAn-" style="width: 1039px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 148.628px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="149">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">PaymentMethodID</span>**

</td><td style="width: 226.913px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="199">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tên hình thức thanh toán</span>**

</td><td style="width: 469.944px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="306">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TM</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tiền mặt</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">CK</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Chuyển khoản</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TM/CK</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Tiền mặt/Chuyển khoản</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xuất hàng cho chi nhánh</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Xuất hàng cho chi nhánh</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">5</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hàng biếu tặng</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hàng biếu tặng</span>

</td></tr><tr style="mso-yfti-irow: 6; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">6</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Cấn trừ công nợ</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Cấn trừ công nợ</span>

</td></tr><tr style="mso-yfti-irow: 7; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 148.628px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="149"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">7</span>

</td><td style="width: 226.913px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="199"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Trả hàng</span>

</td><td style="width: 469.944px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="306"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Trả hàng</span>

</td></tr></tbody></table>

 **4.6 Danh sách ReceiveTypeID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-receivetypeid-h%C3%ACnh-t" style="width: 1041px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 150.117px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="117">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ReceiveTypeID</span>**

</td><td style="width: 696.492px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="537">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hình thức nhận hóa đơn</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 150.117px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 696.492px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Email</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 150.117px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 696.492px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">SMS</span>

</td></tr><tr style="mso-yfti-irow: 3; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 150.117px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="117"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 696.492px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="537"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Email &amp; SMS</span>

</td></tr></tbody></table>

 **4.7 Danh sách ItemTypeID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-itemtypeid-m%C3%B4-t%E1%BA%A3-1-h" style="width: 1039px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 152.211px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="119">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">ItemTypeID</span>**

</td><td style="width: 693.391px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="534">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 152.211px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 693.391px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hàng hóa, dịch vụ</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 152.211px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 693.391px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Khuyến mại</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 152.211px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">3</span>

</td><td style="width: 693.391px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Chiết khấu thương mại (trong trường hợp muốn thể hiện </span>

<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">thông tin chiết khấu theo dòng)</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 152.211px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">4</span>

</td><td style="width: 693.391px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Ghi chú/diễn giải</span>

</td></tr><tr style="mso-yfti-irow: 5; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 152.211px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">5</span>

</td><td style="width: 693.391px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hàng hóa đặc trưng</span>

</td></tr></tbody></table>

 **4.8 Danh sách TaxTypeID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-taxtypeid-m%C3%B4-t%E1%BA%A3-t00-" style="width: 1039px; border-collapse: collapse; border: none;" width="654"><tbody><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 15.0pt;"><td style="width: 152.25px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="119">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxTypeID</span>**

</td><td style="width: 693.375px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 15pt;" valign="top" width="534">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="mso-yfti-irow: 1; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">T00</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thuế suất 0%</span>

</td></tr><tr style="mso-yfti-irow: 2; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">T05</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thuế suất 5%</span>

</td></tr><tr style="mso-yfti-irow: 3; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">T08</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thuế suất 8%</span>

</td></tr><tr style="mso-yfti-irow: 4; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">T10</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thuế suất 10%</span>

</td></tr><tr style="mso-yfti-irow: 5; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">KCT</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Không chịu thuế GTGT</span>

</td></tr><tr style="mso-yfti-irow: 6; mso-yfti-lastrow: yes; height: 15.0pt;"><td style="width: 152.25px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 15pt;" valign="top" width="119"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">KKK</span>

</td><td style="width: 693.375px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 15pt;" valign="top" width="534"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Không kê khai thuế</span>

</td></tr></tbody></table>

 **4.9 Danh sách InvoiceStatusID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-invoicestatusid-m%C3%B4-t" style="width: 882px; border-collapse: collapse; border: none; height: 219.914px;" width="651"><tbody><tr style="height: 19.9922px;"><td style="width: 153.133px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">InvoiceStatusID</span>**

</td><td style="width: 694.344px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn mới tạo, chưa có số hóa đơn</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">1</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn mới tạo, đã cấp số hóa đơn</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">2</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn đã phát hành</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">5</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn thay thế đã cấp số hóa đơn, chờ ký</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">6</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn thay thế đã phát hành</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">7</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn điều chỉnh đã cấp số hóa đơn, chờ ký</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">8</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn điều chỉnh thay thế đã phát hành</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">9</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn bị thay thế</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">10</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn bị điều chỉnh</span>

</td></tr><tr style="height: 19.9922px;"><td style="width: 153.133px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">99</span>

</td><td style="width: 694.344px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 19.9922px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Trạng thái khác</span>

</td></tr></tbody></table>

 **4.10 Danh sách TaxStatusID**

<table border="1" cellpadding="0" cellspacing="0" class="MsoTableGrid" id="bkmrk-taxstatusid-tr%E1%BA%A1ng-th" style="width: 849px; border-collapse: collapse; border: none; height: 211.111px;" width="651"><tbody><tr style="height: 21.1111px;"><td style="width: 154.323px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">TaxStatusID</span>**

</td><td style="width: 213.844px; border: 1pt solid windowtext; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Trạng thái</span>**

</td><td style="width: 485.392px; border-top: 1pt solid windowtext; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-image: initial; border-left: none; background: rgb(191, 191, 191); padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521">**<span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Mô tả</span>**

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130">32</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Chờ Thuế xử lý</span></td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn đang gửi lên Thuế, chờ Thuế duyệt (cấp mã hoặc tiếp nhận)</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">33</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Thuế đã duyệt</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn đã được bên Thuế duyệt</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">34</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Cần rà soát</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn bị lỗi hoặc có vấn đề được Cơ Quan Thuế (CQT) trả về cần rà soát lại</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">35</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Cấp mã bị lỗi</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn CÓ MÃ của CQT bị lỗi chưa được cấp mã</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">36</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Chờ cấp mã</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn CÓ MÃ của CQT đang gửi lên Thuế chờ cấp mã</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">37</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Có sai sót</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn sai sots được CQT trả về</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">38</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lỗi được cơ quan thuế trả về</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn không mã được CQT trả lại</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="130"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">39</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Lỗi khi đẩy vào Queue của Thuế</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;" valign="top" width="521"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn không vào được Queue của Thuế</span>

</td></tr><tr style="height: 21.1111px;"><td style="width: 154.323px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">0</span>

</td><td style="width: 213.844px; border-right: 1pt solid windowtext; border-bottom: 1pt solid windowtext; border-left: 1pt solid windowtext; border-image: initial; border-top: none; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Chưa có trạng thái của CQT</span>

</td><td style="width: 485.392px; border-top: none; border-left: none; border-bottom: 1pt solid windowtext; border-right: 1pt solid windowtext; padding: 0in 5.4pt; height: 21.1111px;"><span style="font-family: 'Times New Roman',serif; mso-fareast-font-family: 'Times New Roman';">Hóa đơn MTT chưa được gửi sang CQT</span>

</td></tr></tbody></table>

## **<span class="TextRun SCXW182286961 BCX8" data-contrast="none" lang="EN-US" xml:lang="EN-US"><span class="NormalTextRun SCXW182286961 BCX8" data-ccp-parastyle="heading 1">5. BẢNG MÃ LỖI PHẢN HỒI</span></span>**