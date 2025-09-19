/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import TCP.Address;
/**
 *
 * @author ngduc
 */
public class eTYdhrdX_TCP_ObjectStream {
    public static void main(String[] args) {
        String host = "203.162.10.109";   // thay bằng IP server nếu cần
        int port = 2209;
        String studentCode = "B22DCVT025";
        String qCode = "eTYdhrdX";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // a. Gửi chuỗi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.writeObject(request);
            out.flush();
            System.out.println("Gửi lên server: " + request);

            // b. Nhận đối tượng Address từ server
            Address addr = (Address) in.readObject();
            System.out.println("Nhận từ server: " + addr);

            // Chuẩn hóa addressLine
            String normalizedLine = normalizeAddressLine(addr.getAddressLine());
            // Chuẩn hóa postalCode
            String normalizedPostal = normalizePostalCode(addr.getPostalCode());

            addr.setAddressLine(normalizedLine);
            addr.setPostalCode(normalizedPostal);

            System.out.println("Địa chỉ sau chuẩn hóa: " + addr);

            // c. Gửi đối tượng đã chuẩn hóa lên server
            out.writeObject(addr);
            out.flush();

            // d. Đóng kết nối
            socket.close();
            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm chuẩn hóa addressLine
    private static String normalizeAddressLine(String line) {
        if (line == null) return "";
        // Loại bỏ ký tự đặc biệt, giữ chữ cái, số và khoảng trắng
        line = line.replaceAll("[^a-zA-Z0-9\\s]", " ");
        // Loại bỏ khoảng trắng thừa
        line = line.trim().replaceAll("\\s+", " ");
        // Viết hoa chữ cái đầu mỗi từ
        String[] parts = line.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.length() > 0) {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) {
                    sb.append(p.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Hàm chuẩn hóa postalCode (chỉ giữ số và dấu '-')
    private static String normalizePostalCode(String code) {
        if (code == null) return "";
        return code.replaceAll("[^0-9-]", "");
    }    
}
