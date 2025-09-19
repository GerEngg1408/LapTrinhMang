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
public class VHBG0zuo_TCP_DataStream {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của thằng anh
        String qCode = "VHBG0zuo";

        try (Socket socket = new Socket(serverAddress, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            socket.setSoTimeout(5000);

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận số nguyên hệ thập phân
            int decimal = in.readInt();
            System.out.println("Received decimal: " + decimal);

            // c) Đổi sang oct và hex
            String oct = Integer.toOctalString(decimal).toUpperCase();
            String hex = Integer.toHexString(decimal).toUpperCase();
            System.out.println("Octal: " + oct + ", Hex: " + hex);

            // d) Gửi gộp 2 chuỗi "oct;hex" lên server
            String result = oct + ";" + hex;
            out.writeUTF(result);
            out.flush();
            System.out.println("Sent: " + result);

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
