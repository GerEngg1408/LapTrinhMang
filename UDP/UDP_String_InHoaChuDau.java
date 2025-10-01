/*
[Mã câu hỏi (qCode): D5KHEcaS].  Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản dưới đây:
a.	Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;5B35BCC1”
b.	Nhận thông điệp từ server theo định dạng “requestId;data” 
-	requestId là một chuỗi ngẫu nhiên duy nhất
-	data là chuỗi dữ liệu cần xử lý
c.	Xử lý chuẩn hóa chuỗi đã nhận thành theo nguyên tắc 
i.	Ký tự đầu tiên của từng từ trong chuỗi là in hoa
ii.	Các ký tự còn lại của chuỗi là in thường
Gửi thông điệp chứa chuỗi đã được chuẩn hóa lên server theo định dạng “requestId;data”
 */
package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_String_InHoaChuDau {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2208; // cổng server
        String studentCode = "B22DCVT025"; // mã sinh viên của bạn
        String qCode = "D5KHEcaS"; // mã câu hỏi

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000); // timeout 5s
            InetAddress server = InetAddress.getByName(serverAddress);

            // ===============================
            // a) Gửi thông điệp ";studentCode;qCode"
            // ===============================
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
            socket.send(sendPacket);
            System.out.println("Sent: " + request);

            // ===============================
            // b) Nhận thông điệp "requestId;data"
            // ===============================
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + received);

            // Tách requestId và data
            String[] parts = received.split(";", 2);
            String requestId = parts[0];
            String data = parts[1];

            // ===============================
            // c) Chuẩn hoá chuỗi data
            // ===============================
            String normalized = normalizeString(data);
            System.out.println("Normalized: " + normalized);

            // ===============================
            // d) Gửi lại server "requestId;data"
            // ===============================
            String response = requestId + ";" + normalized;
            byte[] resultData = response.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, server, port);
            socket.send(resultPacket);
            System.out.println("Sent: " + response);

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    // Hàm chuẩn hoá: viết hoa chữ cái đầu, các ký tự còn lại thường
    private static String normalizeString(String input) {
        String[] words = input.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    sb.append(word.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
}
