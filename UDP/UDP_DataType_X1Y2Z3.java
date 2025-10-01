/*
[Mã câu hỏi (qCode): kjGbT2It].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode".
Ví dụ: ";B15DCCN010;D3F9A7B8"
b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;a;b", với:
•	requestId là chuỗi ngẫu nhiên duy nhất.
•	a và b là chuỗi thể hiện hai số nguyên lớn (hơn hoặc bằng 10 chữ số).
Ví dụ: "X1Y2Z3;9876543210;123456789"
c. Tính tổng và hiệu của hai số a và b, gửi thông điệp lên server theo định dạng "requestId;sum;difference".Ví dụ: 
Nếu nhận được "X1Y2Z3;9876543210,123456789", tổng là 9999999999 và hiệu là 9753086421. Kết quả gửi lại sẽ là "X1Y2Z3;9999999999,9753086421".
 */
package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.math.BigInteger;

public class UDP_DataType_X1Y2Z3 {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2207; // cổng server
        String studentCode = "B22DCVT025"; // mã sinh viên của bạn
        String qCode = "kjGbT2It"; // mã câu hỏi

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
            // b) Nhận thông điệp "requestId;a;b"
            // ===============================
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + received);

            // Tách chuỗi thành requestId, a, b
            String[] parts = received.split(";");
            String requestId = parts[0];
            BigInteger a = new BigInteger(parts[1]);
            BigInteger b = new BigInteger(parts[2]);

            // ===============================
            // c) Tính tổng và hiệu
            // ===============================
            BigInteger sum = a.add(b);
            BigInteger diff = a.subtract(b);

            // Tạo chuỗi kết quả: "requestId;sum,difference"
            String response = requestId + ";" + sum.toString() + "," + diff.toString();
            byte[] resultData = response.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, server, port);
            socket.send(resultPacket);
            System.out.println("Sent: " + response);

            // ===============================
            // d) Đóng socket và kết thúc
            // ===============================
            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    
}
