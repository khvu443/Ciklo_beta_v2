# Ciklo_beta_v2
Project Ciklo using Spring Boot framework

**Change log v2.0.1**
- Develop:
  + Đang thêm phần hủy book và driver sẽ không thể accept book đó và lưu vào DB được
- Fixed:
  + Đã fix lỗi có những lúc không gửi được cho nhau
  + Fix lỗi còn thừa khi vửa sửa lại property của cyclo
- Bug v2.0.1
  + fix phần id bị undefined 
  ![image](https://user-images.githubusercontent.com/83583888/224651683-766da82e-df3f-44df-8f73-ba968e0bbb8b.png)
  + Vẫn chưa tìm được cách để fix lại cái size của map
  ![image](https://user-images.githubusercontent.com/83583888/224653084-f2750770-2c9c-414a-9eea-33a3e0bd4ce4.png)
  
  **Change log v2.0.2**
- Developed:
  + Đã thêm phần hủy book và driver sẽ không thể accept book đó và lưu vào DB được
- Fixed:
  + Đã fix đc phần bị undefined
  ![image](https://user-images.githubusercontent.com/83583888/224651683-766da82e-df3f-44df-8f73-ba968e0bbb8b.png)
- Bug v2.0.1
  + Phần update trạng thái free vẫn còn bị bug có lúc đổi được từ busy thành free
  + Chỉnh lại phần bảng ko nằm trong modal
  
  + Vẫn chưa tìm được cách để fix lại cái size của map
  ![image](https://user-images.githubusercontent.com/83583888/224653084-f2750770-2c9c-414a-9eea-33a3e0bd4ce4.png)

  **Change log v2.0.3**
- Fixed:
  + Hoàn thành phần hủy hóa đơn và phần update status của driver
- Bug v2.0.1
  + Phần datatable của bill nó sẽ reset lại nên khi chuyển qua trang thứ 2 thì sau khi reset sẽ tự động về lại trang 1
