# Hướng dẫn chạy app và Katalon cho thành viên nhóm

Tài liệu này hướng dẫn chạy toàn bộ project local gồm:

- Backend: `node-express-realworld-example-app`
- Frontend: `react-redux-realworld-example-app`
- Katalon automation project: `Katalon-conduit`

> Lưu ý quan trọng: frontend hiện gọi API tại `http://localhost:3001/api`, vì vậy backend cần chạy ở port `3001` khi test bằng frontend/Katalon.

---

## 1. Chuẩn bị môi trường

Cài đặt trước:

1. Node.js và npm
2. Katalon Studio
3. Chrome hoặc Edge browser
4. Git

Kiểm tra Node/npm:

```powershell
node --version
npm --version
```

---

## 2. Clone project từ GitHub

```powershell
git clone https://github.com/Auzema/testing_project.git
cd testing_project
```

Cấu trúc chính sau khi clone:

```text
testing_project/
├─ node-express-realworld-example-app/
├─ react-redux-realworld-example-app/
├─ Katalon-conduit/
├─ README.md
└─ RUN_APP_AND_KATALON_GUIDE.md
```

---

## 3. Cài và chạy backend

Mở PowerShell terminal thứ nhất.

Đi vào backend folder:

```powershell
cd node-express-realworld-example-app
```

Cài dependencies:

```powershell
npm install
```

Generate Prisma client:

```powershell
npx prisma generate --schema src/prisma/schema.prisma
```

Reset database sạch trước khi test:

```powershell
npx prisma migrate reset --skip-seed --force --schema src/prisma/schema.prisma
```

Chạy backend ở port `3001`:

```powershell
$env:PORT=3001; npx nx serve api
```

Khi thành công sẽ thấy dòng tương tự:

```text
server up on port 3001
```

Giữ terminal này mở trong suốt quá trình test.

---

## 4. Cài và chạy frontend

Mở PowerShell terminal thứ hai.

Đi vào frontend folder:

```powershell
cd react-redux-realworld-example-app
```

Cài dependencies:

```powershell
npm install
```

Chạy frontend:

```powershell
npm start
```

Frontend chạy tại:

```text
http://localhost:4100/
```

Nếu gặp lỗi OpenSSL/Webpack trên Node.js mới, chạy lại bằng lệnh:

```powershell
$env:NODE_OPTIONS="--openssl-legacy-provider"; npm start
```

Giữ terminal frontend mở trong suốt quá trình test.

---

## 5. Kiểm tra app thủ công trước khi chạy Katalon

Mở browser:

```text
http://localhost:4100/
```

Kiểm tra nhanh:

1. Homepage load được.
2. Click `Sign up`.
3. Đăng ký user test, ví dụ:
   - Username: `manual_test_001`
   - Email: `manual_test_001@example.com`
   - Password: `Password123!`
4. Sau khi đăng ký thành công, menu phải có:
   - `New Post`
   - `Settings`
   - username của user

Nếu bước này fail thì chưa nên chạy Katalon. Cần kiểm tra backend/frontend/port trước.

---

## 6. Mở Katalon project

Mở Katalon Studio.

Chọn:

```text
File → Open Project
```

Chọn file:

```text
Katalon-conduit/Katalon-conduit.prj
```

Không chọn nhầm folder cũ `tai`.

---

## 7. Kiểm tra Katalon Profile

Trong Katalon, mở:

```text
Profiles/default
```

Kiểm tra các biến chính:

| Variable | Expected Value |
|---|---|
| `FRONTEND_URL` | `http://localhost:4100/` |
| `DEFAULT_TIMEOUT` | `8` hoặc cao hơn |
| `TEST_PASSWORD` | `Password123!` |
| `STEP_DELAY_SECONDS` | `0` hoặc `1` |

---

## 8. Chạy Katalon smoke/regression suite

Trong Katalon, mở:

```text
Test Suites/Web_Smoke_Regression
```

Click Run.

Suite này gồm 16 test cases kiểm tra các chức năng chính:

- Homepage
- Register
- Login validation
- Article
- Comment
- Tag filter
- Favorite/unfavorite
- Profile settings
- Invalid route
- Follow/unfollow

Sau khi chạy xong, chụp screenshot kết quả để đưa vào báo cáo.

---

## 9. Chạy data-driven suite

Trước khi chạy data-driven suite nhiều lần, nên reset database lại để tránh trùng email/user:

```powershell
npx prisma migrate reset --skip-seed --force --schema src/prisma/schema.prisma
```

Sau đó chạy lại backend:

```powershell
$env:PORT=3001; npx nx serve api
```

Trong Katalon, chạy suite:

```text
Test Suites/AI-DATA DRIVEN
```

Suite nên dùng là `AI-DATA DRIVEN`. Duplicate suite cũ `AI_Data_Driven_Suite` đã được bỏ khỏi project để tránh lỗi XML/duplicate closing tag.

Data-driven suite dùng các data files:

- `TD_Register`
- `TD_Login`
- `TD_Article`
- `TD_Comment`
- `TD_Profile`
- `TD_Favorite`
- `TD_Logout`
- `TD_Edge_Security`

Sau khi chạy xong, chụp screenshot kết quả để đưa vào báo cáo.

---

## 10. Thứ tự chạy đề xuất cho demo/báo cáo

1. Reset database sạch.
2. Start backend ở port `3001`.
3. Start frontend ở port `4100`.
4. Mở browser kiểm tra homepage.
5. Mở Katalon project.
6. Run `Web_Smoke_Regression`.
7. Reset database lại nếu cần.
8. Run `AI-DATA DRIVEN`.
9. Chụp screenshot kết quả pass/fail.
10. Ghi defect report nếu có lỗi thật.

---

## 11. Screenshot cần lấy cho báo cáo

Tối thiểu nên có:

1. Backend terminal chạy thành công.
2. Frontend homepage tại `http://localhost:4100/`.
3. Katalon project tree.
4. Katalon Data Files.
5. Kết quả `Web_Smoke_Regression`.
6. Kết quả `AI-DATA DRIVEN`.
7. Screenshot bug nếu phát hiện defect thật.

---

## 12. Lỗi thường gặp

### Backend không chạy do port 3001 bận

Kiểm tra app khác đang dùng port 3001. Đóng app đó hoặc đổi API URL frontend nếu bắt buộc dùng port khác.

### Frontend mở được nhưng gọi API fail

Kiểm tra backend có đang chạy ở `3001` không.

### Katalon test register fail do duplicate email

Reset database bằng:

```powershell
npx prisma migrate reset --skip-seed --force --schema src/prisma/schema.prisma
```

### Katalon không thấy project

Mở trực tiếp file:

```text
Katalon-conduit/Katalon-conduit.prj
```

### Data file trong Katalon không giống CSV

Katalon `.dat` đang trỏ tới `.xlsx`, nên nếu sửa data thì cần đảm bảo file `.xlsx` cũng được cập nhật.
