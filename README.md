{+ Copyright: Aptech C2209G - Nhóm 6 +}

**Tên ứng dụng: Nhóm sẽ quyết định**

**Công nghệ và phát triển:**

- Ngôn ngữ lập trình: Java (sử dụng JavaFX cho giao diện người dùng).
- Thư viện giao diện người dùng: JFoenix, BootstrapFX (để tạo giao diện đẹp và thân thiện).
- Cơ sở dữ liệu: MySQL và MongoDB (cho lưu trữ dữ liệu và tìm kiếm hiệu quả).
- Bảo mật: Sử dụng bcrypt để mã hóa mật khẩu người dùng.
- Môi trường phát triển: IntelliJ IDEA (IDE).
- Tích hợp: Có thể tích hợp các công nghệ khác như Spring Framework cho phát triển backend, WebSocket cho tính năng chat real-time, và Firebase Cloud Messaging cho thông báo.

_Dưới đây là mô tả về một ứng dụng quản lý blog với các tính năng chính bao gồm quản lý bài viết, quản lý thẻ tag, và quản lý người dùng (bao gồm cả admin)..._

**Chức năng chính**

- [ ] Đăng ký: Người dùng có thể tạo một tài khoản mới bằng cách cung cấp thông tin như tên, địa chỉ email và mật khẩu.
- [ ] Đăng nhập: Người dùng đã đăng ký có thể đăng nhập vào tài khoản của họ bằng cách nhập địa chỉ email và mật khẩu đã đăng ký.
- [ ] Khoá Tài Khoản: Admin có quyền khoá tài khoản người dùng nếu cần thiết, ngăn cản họ truy cập vào ứng dụng.
- [ ] Cập nhật role: Admin có quyền nâng cấp role cho người dùng.
- [ ] Đổi Mật Khẩu: Người dùng có thể thay đổi mật khẩu của họ bằng cách nhập mật khẩu cũ và mật khẩu mới.
- [ ] Quên Mật Khẩu: Nếu người dùng quên mật khẩu, họ có thể yêu cầu thiết lập lại mật khẩu qua email hoặc tin nhắn văn bản. Hệ thống sẽ gửi một liên kết hoặc mã OTP cho phép người dùng đặt lại mật khẩu.
- [ ] Quản Lý Bài Viết: Người dùng có thể tạo, chỉnh sửa và xóa bài viết của họ. Mỗi bài viết bao gồm tiêu đề, nội dung và thẻ tag (categories) để phân loại.
- [ ] Quản Lý Thẻ Tag (Categories): Admin có thể tạo, chỉnh sửa và xóa thẻ tag để gắn vào các bài viết. Thẻ tag giúp người viết bài dễ dàng tìm kiếm và sắp xếp bài viết theo chủ đề.
- [ ] Quản Lý Người Dùng: Admin có quyền quản lý tài khoản người dùng, bao gồm việc xem thông tin, cập nhật role của người dùng (ví dụ: quyền admin hoặc người dùng thường), và khoá tài khoản.
- [ ] Nhận Thông Báo: Người dùng có thể đăng ký để nhận thông báo về bài viết mới, bình luận, hoặc sự kiện quan trọng trong ứng dụng (tùy chọn).
- [ ] Chat Real-Time: Ứng dụng có tích hợp tính năng chat real-time giữa người người viết bài và phía ban quản trị (tùy chọn).
- [ ] Tìm Kiếm: Người dùng có thể tìm kiếm bài viết dựa trên tiêu đề, thẻ tag, hoặc nội dung.




