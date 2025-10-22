/*
[Mã câu hỏi (qCode): EvDeoi4s].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN000;XbYdNZ3”.

b. Nhận thông điệp là một chuỗi từ server theo định dạng “requestId;b1,b2”, trong đó:
    requestId là chuỗi ngẫu nhiên duy nhất.
    b1 là số nhị phân thứ nhất
    b2 là số nhị phân thứ hai.
Ví dụ: requestId;0100011111001101,1101000111110101
c. Thực hiện tính tổng hai số nhị phân nhận được, chuyển về dạng thập phân và gửi lên server theo định dạng “requestId;sum”
Kết quả: requestId;72130 
d. Đóng socket và kết thúc chương trình.
 */
package UDP;

import java.net.*;

public class UDP_String_Tong2NhiPhan {
       public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // Địa chỉ server
        int port = 2208; // Cổng server
        String studentCode = "B22DCVT025"; // Mã sinh viên của bạn
        String qCode = "EvDeoi4s"; // Mã câu hỏi

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000); // Timeout 5 giây
            InetAddress server = InetAddress.getByName(serverAddress);

            // a) Gửi thông điệp ";studentCode;qCode"
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
            socket.send(sendPacket);
            System.out.println("Sent: " + request);

            // b) Nhận thông điệp "requestId;b1,b2"
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + received);

            // Tách chuỗi thành requestId, b1, b2
            String[] parts = received.split(";");
            String requestId = parts[0];
            String[] binaryNumbers = parts[1].split(",");
            String b1 = binaryNumbers[0];
            String b2 = binaryNumbers[1];

            // c) Tính tổng hai số nhị phân
            int num1 = Integer.parseInt(b1, 2); // Chuyển b1 từ nhị phân sang thập phân
            int num2 = Integer.parseInt(b2, 2); // Chuyển b2 từ nhị phân sang thập phân
            int sum = num1 + num2;

            // Tạo chuỗi kết quả: "requestId;sum"
            String response = requestId + ";" + sum;
            byte[] resultData = response.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, server, port);
            socket.send(resultPacket);
            System.out.println("Sent: " + response);

            // d) Đóng socket và kết thúc
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
