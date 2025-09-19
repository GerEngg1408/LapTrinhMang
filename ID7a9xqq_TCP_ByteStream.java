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

