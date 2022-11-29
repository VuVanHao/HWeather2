# HWeather2
1. Mô tả : 
 + HWeather2 là một ứng dụng dự báo thời tiết trên thiết bị Android.
 + Ứng dụng cung cấp thông tin chi tiết về thành phố vào từng thời gian trong ngày và liên tục trong 5 ngày.
 + Được viết bằng Android Native sử dụng Kotlin, là phiên bản nâng cấp của HWeather với một số chức năng được cải tiến :
      -> Tối đa việc sử dụng Fragment cho các chức năng
      -> Sử dụng Coroutines, Dagger2 trong quá trình phát triển : Đa luồng, tốc độ xử lí cao hơn
      -> Sử dụng Room Database để lưu trữ dữ liệu offline khi không có Internet
      -> Được xây dựng phát triển trên mô hình MVVM, tối ưu việc phát triển, dễ bảo trì sau này
      
      ![image](https://user-images.githubusercontent.com/84240962/204559287-cf6c8531-b38b-4420-81ed-e6d3917d4f14.png)
2. Chức năng : Đa số là giống với HWeather

  2.1 : Hướng dẫn người dùng sử dụng lần đầu
  ![image](https://user-images.githubusercontent.com/84240962/204559635-763786be-2f32-4c78-b1f1-bf76b179a7de.png)

  2.2 : Xem thông tin chi tiết thành phố
  ![image](https://user-images.githubusercontent.com/84240962/204559777-aa7201d6-4dbe-418f-8262-11c745677a6f.png)
  ![image](https://user-images.githubusercontent.com/84240962/204559823-7d05911c-48e5-4447-802c-3938a6b53a4d.png)
  ![image](https://user-images.githubusercontent.com/84240962/204559849-23d5b2d4-5370-42e7-b8b2-cb2cd6d31293.png)
  
  2.3 : Quản lí danh sách thành phố : Người dùng có thể xem thông tin chi tiết các thành phố có ở trong danh sách ngày ngoài màn hình chính. Người dùng có thể chỉnh sửa : thêm, xóa các thành phố
  ![image](https://user-images.githubusercontent.com/84240962/204560006-2b007341-2d7b-473b-bbc0-a8adcc1c545b.png)
  ![image](https://user-images.githubusercontent.com/84240962/204560041-fdd3daf5-a918-4b04-9782-3880d5f2e750.png)
  
  2.4 : Tìm kiếm nhanh : Người dùng tìm kiếm thời tiết bằng tên thành phố.
  ![image](https://user-images.githubusercontent.com/84240962/204560195-e9d4fc66-4f27-4ee2-8b9c-dc3bb2534f84.png)
  ![image](https://user-images.githubusercontent.com/84240962/204560249-8b0df231-d570-403e-b59c-e0789e040fbb.png)

  2.5 : Tìm kiếm trên bản đồ : một khu vực cụ thể dựa vào kinh độ và vĩ độ 
  ![image](https://user-images.githubusercontent.com/84240962/204560351-35be4bba-a27a-4933-a779-9a5e7f0c2dbe.png)
  ![image](https://user-images.githubusercontent.com/84240962/204560580-43686156-e9b5-48c8-8e6c-1bba6379a951.png)
  
  2.6 : Tạo Widget hiện thị thông tin nhanh ngoài màn hình chính
  ![image](https://user-images.githubusercontent.com/84240962/204560696-50eb7db1-6b09-49a2-af9d-5ec1a57d77f4.png)

  2.7 : Cài đặt ngôn ngữ và đơn vị nhiệt độ
  ![image](https://user-images.githubusercontent.com/84240962/204560823-23836914-49d5-457a-8bcf-3ea92806ad88.png)



