/*
[Mã câu hỏi (qCode): ID7a9xqq].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B16DCCN999;C89DAB45"
b. Nhận dữ liệu từ server là một chuỗi các số nguyên được phân tách bởi ký tự ",".
Ví dụ: "8,4,2,10,5,6,1,3"
c. Tính tổng của tất cả các số nguyên tố trong chuỗi và gửi kết quả lên server.
Ví dụ: Với dãy "8,4,2,10,5,6,1,3", các số nguyên tố là 2, 5, 3, tổng là 10. Gửi lên server chuỗi "10".
d. Đóng kết nối và kết thúc chương trình.
 */
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
/**
 *
 * @author ngduc
 */
public class ID7a9xqq_TCP_ByteStream {
     public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // server của đề
        int port = 2206;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của thằng anh
        String qCode = "ID7a9xqq";

        try (Socket socket = new Socket(serverAddress, port);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            socket.setSoTimeout(5000); // timeout 5s

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.write(request.getBytes(StandardCharsets.UTF_8));
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận chuỗi số nguyên phân tách bởi dấu ","
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            while (true) {
                int b;
                try {
                    b = in.read();
                } catch (SocketTimeoutException e) {
                    break; // hết dữ liệu trong 5s
                }
                if (b == -1 || b == '\n') break; // kết thúc
                if (b != '\r') buf.write(b);
            }
            String response = buf.toString(StandardCharsets.UTF_8.name()).trim();
            System.out.println("From server: [" + response + "]");

            // c) Tính tổng các số nguyên tố
            long sum = 0;
            if (!response.isEmpty()) {
                String[] tokens = response.split(",");
                for (String t : tokens) {
                    t = t.trim();
                    if (t.isEmpty()) continue;
                    try {
                        long v = Long.parseLong(t);
                        if (isPrime(v)) sum += v;
                    } catch (NumberFormatException ignore) {
                        // bỏ qua token không hợp lệ
                    }
                }
            }

            // d) Gửi kết quả lên server (chỉ số, không kèm \n để tránh Socket Closed)
            String result = String.valueOf(sum);
            out.write(result.getBytes(StandardCharsets.UTF_8));
            out.flush();
            System.out.println("Sent: " + sum);

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm kiểm tra số nguyên tố
    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        if (n % 3 == 0) return n == 3;
        long r = (long) Math.sqrt(n);
        for (long i = 5; i <= r; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

}


