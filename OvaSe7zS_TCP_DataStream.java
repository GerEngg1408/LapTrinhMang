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
