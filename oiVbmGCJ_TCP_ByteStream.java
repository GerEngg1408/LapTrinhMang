/*
[Mã câu hỏi (qCode): oiVbmGCJ].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B16DCCN999;D45EFA12"
b. Nhận dữ liệu từ server là một chuỗi các số nguyên được phân tách bởi ký tự ",".
Ví dụ: "10,5,15,20,25,30,35"
c. Xác định hai số trong dãy có tổng gần nhất với gấp đôi giá trị trung bình của toàn bộ dãy. Gửi thông điệp lên server theo định dạng "num1,num2" (với num1 < num2)
Ví dụ: Với dãy "10,5,15,20,25,30,35", gấp đôi giá trị trung bình là 40, hai số có tổng gần nhất là 15 và 25. Gửi lên server chuỗi "15,25".
d. Đóng kết nối và kết thúc chương trình.
 */
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
/**
 *
 * @author ngduc
 */
public class oiVbmGCJ_TCP_ByteStream {
        public static void main(String[] args) {
        String host = "203.162.10.109";   // thay bằng IP server nếu cần
        int port = 2206;
        String studentCode = "B22DCVT025";
        String qCode = "oiVbmGCJ";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000); // tối đa 5 giây

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // a. Gửi mã sinh viên + qCode
            String request = studentCode + ";" + qCode;
            out.write(request.getBytes());
            out.flush();

            // b. Nhận chuỗi số nguyên từ server
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            String data = new String(buffer, 0, bytesRead).trim();
            System.out.println("Dữ liệu nhận từ server: " + data);

            // c. Xử lý tìm 2 số có tổng gần nhất với 2 * trung bình
            String[] parts = data.split(",");
            int[] numbers = Arrays.stream(parts)
                                  .mapToInt(Integer::parseInt)
                                  .toArray();

            double avg = Arrays.stream(numbers).average().orElse(0);
            double target = 2 * avg;

            int num1 = 0, num2 = 0;
            double minDiff = Double.MAX_VALUE;

            for (int i = 0; i < numbers.length; i++) {
                for (int j = i + 1; j < numbers.length; j++) {
                    int sum = numbers[i] + numbers[j];
                    double diff = Math.abs(sum - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        num1 = Math.min(numbers[i], numbers[j]);
                        num2 = Math.max(numbers[i], numbers[j]);
                    }
                }
            }

            String response = num1 + "," + num2;
            System.out.println("Gửi lên server: " + response);

            out.write(response.getBytes());
            out.flush();

            // d. Đóng kết nối
            socket.close();
            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

