/*
[Mã câu hỏi (qCode): 0lBvkw2C].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng ký tự (BufferedReader/BufferedWriter) theo kịch bản sau:
a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;C1234567"
b. Nhận từ server một chuỗi chứa nhiều từ, các từ được phân tách bởi khoảng trắng. Ví dụ: "hello world this is a test example"
c. Sắp xếp các từ trong chuỗi theo độ dài, thứ tự xuất hiện. Gửi danh sách các từ theo từng nhóm về server theo định dạng: "a, is, this, test, hello, world, example".
d. Đóng kết nối và kết thúc chương trình.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;
/**
 *
 * @author ngduc
 */
public class OlBvkw2C_TCP_CharacterStream {
public static void main(String[] args) {
        String host = "203.162.10.109";   // thay bằng IP server nếu cần
        int port = 2208;
        String studentCode = "B22DCVT025";
        String qCode = "0lBvkw2C";

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

            // c. Sắp xếp từ theo độ dài, giữ thứ tự xuất hiện
            String[] words = data.split("\\s+");
            List<String> wordList = Arrays.asList(words);

            // sắp xếp ổn định theo độ dài
            wordList.sort(Comparator.comparingInt(String::length));

            String response = String.join(", ", wordList);
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
}

