/*
[Mã câu hỏi (qCode): OvaSe7zS].  Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu xây dựng chương trình client thực hiện giao tiếp với server sử dụng luồng data (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:
a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode".
Ví dụ: "B10DCCN003;C6D7E8F9"
b. Nhận lần lượt:
•	Một số nguyên k là độ dài đoạn.
•	Chuỗi chứa mảng số nguyên, các phần tử được phân tách bởi dấu phẩy ",".
Ví dụ: Nhận k = 3 và "1,2,3,4,5,6,7,8".
c. Thực hiện chia mảng thành các đoạn có độ dài k và đảo ngược mỗi đoạn, sau đó gửi mảng đã xử lý lên server. Ví dụ: Với k = 3 và mảng "1,2,3,4,5,6,7,8", kết quả là "3,2,1,6,5,4,8,7". Gửi chuỗi kết quả "3,2,1,6,5,4,8,7" lên server.
d. Đóng kết nối và kết thúc chương trình
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class OvaSe7zS_TCP_DataStream {
      public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; // địa chỉ server
        int port = 2207;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của thằng anh
        String qCode = "OvaSe7zS";

        try (Socket socket = new Socket(serverAddress, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            socket.setSoTimeout(5000);

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận số nguyên k và chuỗi mảng
            int k = in.readInt();
            String arrStr = in.readUTF();
            System.out.println("Received k=" + k + ", arr=" + arrStr);

            // c) Xử lý chia mảng thành đoạn k và đảo ngược từng đoạn
            String[] tokens = arrStr.split(",");
            int[] nums = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                nums[i] = Integer.parseInt(tokens[i].trim());
            }

            for (int i = 0; i < nums.length; i += k) {
                int left = i;
                int right = Math.min(i + k - 1, nums.length - 1);
                while (left < right) {
                    int tmp = nums[left];
                    nums[left] = nums[right];
                    nums[right] = tmp;
                    left++;
                    right--;
                }
            }

            // Ghép lại thành chuỗi
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nums.length; i++) {
                sb.append(nums[i]);
                if (i < nums.length - 1) sb.append(",");
            }
            String result = sb.toString();
            System.out.println("Processed result: " + result);

            // Gửi chuỗi kết quả lên server
            out.writeUTF(result);
            out.flush();
            System.out.println("Sent: " + result);

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}


