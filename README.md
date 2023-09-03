{+ Copyright: Aptech C2209G - Nhóm 6 +}

I. Tổng quan về ứng dụng

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

II. Java
1. Giới thiệu
Java được tạo ra với tiêu chí “Viết (code) một lần, thực thi khắp nơi” (Write Once, Run Anywhere  – WORA). Chương trình phần mềm viết bằng Java có thể chạy trên mọi nền tảng (platform) khác nhau thông qua một môi trường thực thi với điều kiện có môi trường thực thi thích hợp hỗ trợ nền tảng đó .

2. Lập trình hướng đối tượng
Lập trình hướng đối tượng (Object Oriented Programing – OOP) là một phương pháp để thiết kế một chương trình bởi sử dụng các lớp và các đối tượng.

- Đa hình (Polymorphism)
- Thừa kế (Inheritance)
- Đóng gói (Encapsulation)
- Trừu tượng (Abstraction)

3. Mô hình MVC
MVC là viết tắt của 3 chữ: Model – View – Controller.

**3 chữ này đại diện cho 3 folder/layer/package. Các lập trình viên sẽ phải tuân theo nguyên tắc của từng layer, để xây dựng nội dung của layer đó. **

a. Model
- Model đại diện cho dữ liệu và logic xử lý dữ liệu. Nó chứa các thông tin cần thiết để thể hiện trạng thái của ứng dụng. Model có thể bao gồm các lớp để truy cập cơ sở dữ liệu, xử lý logic kinh doanh, và quản lý trạng thái.

- Layer này sẽ chứa các lớp liên quan đến lưu trữ dữ liệu, hay đảm nhiệm xử lý các nghiệp vụ logic của ứng dụng. Bạn tưởng tượng model giống như bộ não của con người, nó giúp xử lý và lưu trữ dữ liệu.


b. View
- View là giao diện người dùng hiển thị thông tin cho người dùng và tương tác với người dùng. Nó có nhiệm vụ hiển thị dữ liệu từ Model và hiển thị giao diện người dùng. Mỗi phần của ứng dụng có thể có nhiều View khác nhau.

- Layer này sẽ chứa các lớp liên quan đến hiển thị, và nhận tương tác từ phía người dùng. Nếu tưởng tượng đến các cơ quan của con người, thì view chính là các giác quan, giúp “nghe”, “ngửi”, “nếm”, “nhìn”, “cảm giác” từ bên ngoài rồi chuyển vào cho model xử lý. Đồng thời có thể “nói” ra môi trường bên ngoài sau khi nhận kết quả xử lý từ model. Thực sự thì view nên trung lập nhất có thể, nó chỉ có thể nhận dữ liệu vào, và hiển thị dữ liệu ra, nó không có “cảm xúc” hay xử lý logic gì cả. Tất cả những gì nó cần làm là lấy dữ liệu từ người dùng rồi gọi đến controller hoặc model để thực hiện tiếp các tác vụ. Rồi sau đó view cũng sẽ hiển thị kết quả sau khi nhận được xử lý từ controller.


c. Controller
- Controller là lớp trung gian giữa Model và View. Nó xử lý sự kiện từ người dùng hoặc các nguồn khác và cập nhật Model hoặc giao diện View tương ứng. Controller có nhiệm vụ điều phối luồng làm việc và quản lý tương tác giữa Model và View.

- Controller là lớp trung gian giữa Model và View. Nó xử lý sự kiện từ người dùng hoặc các nguồn khác và cập nhật Model hoặc giao diện View tương ứng. Controller có nhiệm vụ điều phối luồng làm việc và quản lý tương tác giữa Model và View.


d. Ưu điểm
-> Dễ code ;)))


4. Mô hình DAO 
DAO là viết tắt của "Data Access Object," là một mô hình thiết kế trong lập trình phần mềm, thường được sử dụng để tạo một giao diện trừu tượng giữa ứng dụng và cơ sở dữ liệu (database). Mô hình DAO giúp tách biệt logic của ứng dụng và truy cập cơ sở dữ liệu, làm cho ứng dụng dễ dàng bảo trì, mở rộng và thay đổi loại cơ sở dữ liệu mà ứng dụng sử dụng mà không cần sửa đổi nhiều phần của mã nguồn.

- Tách biệt logic ứng dụng và truy cập dữ liệu, giúp quản lý mã nguồn dễ dàng hơn.
- Giúp thay đổi cơ sở dữ liệu dễ dàng hơn mà không cần sửa đổi nhiều phần của ứng dụng.
- Tích hợp bảo mật dữ liệu và kiểm soát truy cập đến cơ sở dữ liệu.


5. Kết hợp MVC + DAO 
a. Model: Đại diện cho các trường dữ liệu trên database 
Ví dụ: Tại bảng user gồm các cột (id, name, username, password)
-> Tạo một UserModel có các thuộc tính private (id, name, username, password)

Tóm lại là trên database có cột nào thì tại model sẽ có các thuộc tính đó 

b. View : Tương tự như website được hiển thị nhờ các thẻ html để xây dựng giao diện và ngôn ngữ backend (đã học php, javascript) để xử lý logic, thì trong javafx sẽ sử dụng fxml để xây dựng dao diện và một lớp Controller để xử lý sự kiện nhấn nút, hiển thị dữ liệu... cho nó 

Ví dụ: Tạo một file login.fxml để xử lý chức năng đăng nhập, thì sẽ tạo thêm một file java có tên LoginController.java để xử lý logic cho file giao diện đó 

c. Controller: Là nơi xử lý sự kiện của người dùng và là nơi trung gian giữa logic và hiển thị 

d. Dao: Là nơi làm việc với database. Dev sẽ xây dựng các phương thức để thực hiện như thêm, sửa, xoá, hiển thị dữ liệu.... Tuỳ theo nghiệp vụ mà xây dựng các phương thức bổ sung 

6. Ví dụ thực tế 
Quản lý users
B1: Tạo một UserModel để làm việc với bảng users trên database 
B2: Xây dựng dao (IUserDao và UserDaoImpl): trong đó I là viết tắt của interface, tại IUSerDao sẽ chứa các phướng thức (không có body/nội dung) ví dụ getAllUsers, getUserById, deleteUserById,... UserDaoImpl sẽ là nơi triển khai nội dung của IUserDao -> Mục đích khi tạo ra 2 file thay vì 1 nhằm giúp người khác dễ đọc xem mình đang xây dựng những phương thức nào để quản lý user đó, cũng sẽ dễ phát triển và mở rộng,
B3: Xây dựng giao diện login.fxml. Chúng ta cần có một giao diện để có thể lấy được tài khoản và mật khẩu của người dùng rồi sử dụng phương thức phù hợp trong UserDaoImpl để xử lý logic đăng nhập
B4. Xây dựng UserController để xử lý logic giữa phía Dao và view trên 


Tất nhiên trong ứng dụng thực tế ta cần xử lý nhiều vấn đề hơn là ví dụ trên nhưng mong từ những ví dụ trên đã giúp bạn hình thành được sơ đồ và luồng của ứng dụng 

Vamos 😘
