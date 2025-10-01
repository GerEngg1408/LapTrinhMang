/*
[Mã câu hỏi (qCode): MdDzdfm7].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: ";B15DCCN009;EF56GH78"
b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;data", với:
•	requestId là chuỗi ngẫu nhiên duy nhất.
•	data là một chuỗi ký tự chứa nhiều từ, được phân cách bởi dấu cách.
Ví dụ: "EF56GH78;The quick brown fox"
c. Sắp xếp các từ trong chuỗi theo thứ tự từ điển ngược (z đến a) và gửi thông điệp lên server theo định dạng "requestId;word1,word2,...,wordN".
Ví dụ: Với data = "The quick brown fox", kết quả là: "EF56GH78;quick,fox,brown,The"
d. Đóng socket và kết thúc chương trình
 */
package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Comparator;

public class UDP_String_Thequickbrownfox {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2208; // cổng server
        String studentCode = "B22DCVT025"; // mã sinh viên của bạn
        String qCode = "MdDzdfm7"; // mã câu hỏi

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
            // ===============================
            // c) Sắp xếp từ theo thứ tự điển ngược (z->a), KHÔNG phân biệt hoa/thường
            // ===============================
            String[] words = data.trim().split("\\s+");
            Arrays.sort(words, (w1, w2) -> w2.compareToIgnoreCase(w1));

            String resultWords = String.join(",", words);
            System.out.println("Sorted result: " + resultWords);

            // Gửi lại "requestId;word1,word2,..."
            String response = requestId + ";" + resultWords;


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
