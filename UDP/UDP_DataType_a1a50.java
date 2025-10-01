package UDP;

/*
[Mã câu hỏi (qCode): uqbEGTNa].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
a.	Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;DC73CA2E”
b.	Nhận thông điệp là một chuỗi từ server theo định dạng “requestId;a1,a2,...,a50” 
-	requestId là chuỗi ngẫu nhiên duy nhất
-	a1 -> a50 là 50 số nguyên ngẫu nhiên
c.	Thực hiện tìm giá trị lớn nhất và giá trị nhỏ nhất thông điệp trong a1 -> a50 và gửi thông điệp lên lên server theo định dạng “requestId;max,min”
d.	Đóng socket và kết thúc chương trình
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDP_DataType_a1a50 {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2207; // cổng server
        String studentCode = "B22DCVT025"; // TODO: thay mã sinh viên của bạn
        String qCode = "uqbEGTNa"; // mã câu hỏi

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000); // timeout 5s
            InetAddress server = InetAddress.getByName(serverAddress);

            // ===============================
            // a) Gửi thông điệp "studentCode;qCode"
            // ===============================
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
            socket.send(sendPacket);
            System.out.println("Sent: " + request);

            // ===============================
            // b) Nhận thông điệp từ server có dạng "requestId;a1,a2,...,a50"
            // ===============================
            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + received);

            // Tách requestId và dãy số
            String[] parts = received.split(";");
            String requestId = parts[0];
            String[] numberStr = parts[1].split(",");

            int[] numbers = new int[numberStr.length];
            for (int i = 0; i < numberStr.length; i++) {
                numbers[i] = Integer.parseInt(numberStr[i]);
            }


            // c) Tìm giá trị lớn nhất và nhỏ nhất
            // ===============================
            int max = Arrays.stream(numbers).max().getAsInt();
            int min = Arrays.stream(numbers).min().getAsInt();
            System.out.println("Calculated: max=" + max + ", min=" + min);

            // Gửi lại theo định dạng "requestId;max,min"
            String response = requestId + ";" + max + "," + min;
            byte[] resultData = response.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, server, port);
            socket.send(resultPacket);
            System.out.println("Sent: " + response);

            // ===============================
            // d) Đóng socket và kết thúc chương trình
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
