/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
/**
 *
 * @author ngduc
 */
public class uGUZJgz5_TCP_CharacterStream {
    public static void main(String[] args) {
        String host = "203.162.10.109";   // thay bằng IP server nếu cần
        int port = 2208;
        String studentCode = "B22DCVT025";
        String qCode = "uGUZJgz5";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000); // tối đa 5 giây

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // a. Gửi mã sinh viên + qCode
            String request = studentCode + ";" + qCode;
            out.write(request);
            out.newLine();
            out.flush();
            System.out.println("Gửi lên server: " + request);

            // b. Nhận chuỗi từ server
            String data = in.readLine();
            System.out.println("Dữ liệu nhận từ server: " + data);

            // c. Đảo ngược từng từ và nén RLE
            String[] words = data.split("\\s+");
            StringBuilder result = new StringBuilder();

            for (int w = 0; w < words.length; w++) {
                String reversed = new StringBuilder(words[w]).reverse().toString();
                String encoded = rleEncode(reversed);
                result.append(encoded);
                if (w < words.length - 1) {
                    result.append(" ");
                }
            }

            String response = result.toString();
            System.out.println("Gửi lên server: " + response);

            out.write(response);
            out.newLine();
            out.flush();

            // d. Đóng kết nối
            socket.close();
            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm nén RLE
    private static String rleEncode(String s) {
        if (s.isEmpty()) return "";
        StringBuilder encoded = new StringBuilder();
        int count = 1;
        char prev = s.charAt(0);

        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == prev) {
                count++;
            } else {
                encoded.append(prev);
                if (count > 1) encoded.append(count);
                prev = c;
                count = 1;
            }
        }
        encoded.append(prev);
        if (count > 1) encoded.append(count);

        return encoded.toString();
    }    
}
