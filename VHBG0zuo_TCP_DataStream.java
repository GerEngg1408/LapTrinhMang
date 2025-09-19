/*
[Mã câu hỏi (qCode): VHBG0zuo].  Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu xây dựng chương trình client thực hiện giao tiếp với server sử dụng luồng data (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B10DCCN001;A1B2C3D4"
b. Nhận một số nguyên hệ thập phân từ server. Ví dụ: 255
c. Chuyển đổi số nguyên nhận được sang hai hệ cơ số 8 và 16. Gửi lần lượt chuỗi kết quả lên server. Ví dụ: Với số 255 hệ thập phân, kết quả gửi lên sẽ là hai chuỗi "377" và "FF".
d. Đóng kết nối và kết thúc chương trình.
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

