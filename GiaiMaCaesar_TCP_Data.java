import java.io.*;
import java.net.*;

public class GiaiMaCaesar_TCP_Data {
    public static void main(String[] args) {
        String host = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCVT025";
        String qCode = "h8NcEF9M";

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // a) Gửi studentCode;qCode
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận chuỗi mã hóa và giá trị dịch chuyển
            String s = in.readUTF();
            int k = in.readInt();
            System.out.println("Received: " + s + " | shift = " + k);

            // c) Giải mã (dịch ngược lại)
            StringBuilder ans = new StringBuilder();
            for (char x : s.toCharArray()) {
                if (Character.isLetter(x)) {
                    char base = Character.isUpperCase(x) ? 'A' : 'a';
                    x = (char) ((x - base - k + 26) % 26 + base);
                }
                ans.append(x);
            }

            System.out.println("Decoded: " + ans);

            // d) Gửi kết quả lên server
            out.writeUTF(ans.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
