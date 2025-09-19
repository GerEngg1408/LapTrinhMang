/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author ngduc
 */
public class QFWmW74a_TCP_DataStream {
       public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2207;
        String studentCode = "B22DCVT025"; // thay MSSV của thằng anh
        String qCode = "QFWmW74a";

        try (Socket socket = new Socket(serverAddress, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            socket.setSoTimeout(5000); // timeout 5s

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận 2 số nguyên a và b
            int a = in.readInt();
            int b = in.readInt();
            System.out.println("Received a=" + a + ", b=" + b);

            // c) Tính tổng và tích
            int sum = a + b;
            int product = a * b;

            // Gửi lần lượt sum, product
            out.writeInt(sum);
            out.writeInt(product);
            out.flush();
            System.out.println("Sent sum=" + sum + ", product=" + product);

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
