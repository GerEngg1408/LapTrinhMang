/*
[Mã câu hỏi (qCode): XLwUbQpI].  Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu xây dựng chương trình client thực hiện giao tiếp với server sử dụng luồng data (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B10DCCN002;B4C5D6E7"
b. Nhận chuỗi chứa mảng số nguyên từ server, các phần tử được phân tách bởi dấu phẩy ",". Ví dụ: "1,3,2,5,4,7,6"
c. Tính số lần đổi chiều và tổng độ biến thiên trong dãy số.
- Đổi chiều: Khi dãy chuyển từ tăng sang giảm hoặc từ giảm sang tăng 
-   Độ biến thiên: Tổng giá trị tuyệt đối của các hiệu số liên tiếp
Gửi lần lượt lên server: số nguyên đại diện cho số lần đổi chiều, sau đó là số nguyên đại diện cho tổng độ biến thiên. Ví dụ: Với mảng "1,3,2,5,4,7,6", số lần đổi chiều: 5 lần, Tổng độ biến thiên 11 -> Gửi lần lượt số nguyên 5 và 11 lên server.
d. Đóng kết nối và kết thúc chương trình..
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class XLwUbQpI_TCP_DataStream {
    public static void main(String[] args) {
        String host = "203.162.10.109";   // thay bằng IP server nếu cần
        int port = 2207;
        String studentCode = "B22DCVT025";
        String qCode = "XLwUbQpI";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000); // tối đa 5 giây

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // a. Gửi mã sinh viên + qCode
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();
            System.out.println("Gửi lên server: " + request);

            // b. Nhận chuỗi số nguyên từ server
            String data = in.readUTF();
            System.out.println("Dữ liệu nhận từ server: " + data);

            // c. Tính số lần đổi chiều và tổng độ biến thiên
            String[] parts = data.split(",");
            int[] numbers = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            int doiChieu = 0;
            int tongBienThien = 0;

            // Tính độ biến thiên
            for (int i = 1; i < numbers.length; i++) {
                tongBienThien += Math.abs(numbers[i] - numbers[i - 1]);
            }

            // Tính số lần đổi chiều
            for (int i = 1; i < numbers.length - 1; i++) {
                int diff1 = numbers[i] - numbers[i - 1];
                int diff2 = numbers[i + 1] - numbers[i];
                if ((diff1 > 0 && diff2 < 0) || (diff1 < 0 && diff2 > 0)) {
                    doiChieu++;
                }
            }

            // Gửi kết quả
            System.out.println("Số lần đổi chiều: " + doiChieu);
            System.out.println("Tổng độ biến thiên: " + tongBienThien);

            out.writeInt(doiChieu);
            out.writeInt(tongBienThien);
            out.flush();

            // d. Đóng kết nối
            socket.close();
            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}


