package UDP;

import java.net.*;
import java.math.BigInteger;

public class UDP_DataType_TongNum {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // Địa chỉ server
        int port = 2207; // Cổng server
        String studentCode = "B22DCVT025"; // Mã sinh viên của bạn
        String qCode = "V8FVe37C"; // Mã câu hỏi

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

            // b) Nhận thông điệp "requestId;num"
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + received);

            // Tách chuỗi thành requestId và num
            String[] parts = received.split(";");
            String requestId = parts[0];
            BigInteger num = new BigInteger(parts[1]);

            // c) Tính tổng các chữ số trong num
            String numStr = num.toString();
            int sumDigits = 0;
            for (int i = 0; i < numStr.length(); i++) {
                sumDigits += Character.getNumericValue(numStr.charAt(i));
            }

            // Tạo chuỗi kết quả: "requestId;sumDigits"
            String response = requestId + ";" + sumDigits;
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
