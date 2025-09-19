/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import TCP.Product;  
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;




import TCP.Product;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MCHTKViH_TCP_ObjectStream {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2209;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của bạn
        String qCode = "MCHTKViH";

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            socket.setSoTimeout(5000);

            // a) Gửi chuỗi studentCode;qCode
            String request = studentCode + ";" + qCode;
            out.writeObject(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận đối tượng Product từ server
            Object obj = in.readObject();
            if (obj instanceof Product) {
                Product product = (Product) obj;
                System.out.println("Received product: " + product);

                // c) Tính discount = tổng các chữ số phần nguyên của price
                int intPart = (int) product.getPrice();
                int sumDigits = 0;
                while (intPart > 0) {
                    sumDigits += intPart % 10;
                    intPart /= 10;
                }
                product.setDiscount(sumDigits);
                System.out.println("Updated product with discount=" + sumDigits);

                // gửi lại đối tượng đã cập nhật
                out.writeObject(product);
                out.flush();
                System.out.println("Sent updated product");
            } else {
                System.err.println("Nhận được kiểu khác: " + obj.getClass());
            }

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
