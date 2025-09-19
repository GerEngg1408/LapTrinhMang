/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class bmB0Ipfy_TCP_ByteStream {



    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2206;
        String studentCode = "B22DCVT025";   // đổi sang MSSV của bạn
        String qCode = "bmB0lpfy";

        try (Socket socket = new Socket(serverAddress, port);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            socket.setSoTimeout(5000); // timeout 5s

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.write(request.getBytes(StandardCharsets.UTF_8));
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận dữ liệu từ server
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            while (true) {
                int b;
                try {
                    b = in.read();
                } catch (SocketTimeoutException e) {
                    // Không có byte mới trong 5s => coi như server đã gửi xong
                    break;
                }
                if (b == -1 || b == '\n') break; // kết thúc thông điệp
                if (b != '\r') buf.write(b);     // bỏ CR nếu có
            }
            String response = buf.toString(StandardCharsets.UTF_8.name()).trim();
            System.out.println("From server: [" + response + "]");

            // c) Tính tổng các số nguyên tố trong chuỗi phân tách bằng dấu ","
            long sum = 0;
            if (!response.isEmpty()) {
                String[] tokens = response.split(",");
                for (String t : tokens) {
                    if (t == null) continue;
                    t = t.trim();
                    if (t.isEmpty()) continue; // bỏ qua token rỗng
                    try {
                        long v = Long.parseLong(t);
                        if (isPrime(v)) {
                            sum += v;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Bỏ qua token không hợp lệ: '" + t + "'");
                    }
                }
            }

            // d) Gửi kết quả lên server (có \n)
            String result = sum + "\n";
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

