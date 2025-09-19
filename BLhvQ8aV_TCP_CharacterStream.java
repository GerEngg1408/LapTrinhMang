/*
[Mã câu hỏi (qCode): BLhvQ8aV].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau: 
a.	Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;EC4F899B"
b.	Nhận một chuỗi ngẫu nhiên là danh sách các một số tên miền từ server
Ví dụ: giHgWHwkLf0Rd0.io, I7jpjuRw13D.io, wXf6GP3KP.vn, MdpIzhxDVtTFTF.edu, TUHuMfn25chmw.vn, HHjE9.com, 4hJld2m2yiweto.vn, y2L4SQwH.vn, s2aUrZGdzS.com, 4hXfJe9giAA.edu
c.	Tìm kiếm các tên miền .edu và gửi lên server
Ví dụ: MdpIzhxDVtTFTF.edu, 4hXfJe9giAA.edu
d.	Đóng kết nối và kết thúc chương trình...
 */
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
/**
 *
 * @author ngduc
 */
public class BLhvQ8aV_TCP_CharacterStream {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2208;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của anh
        String qCode = "BLhvQ8aV";

        try (Socket socket = new Socket(serverAddress, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            socket.setSoTimeout(5000); // timeout 5s

            // a) Gửi "studentCode;qCode"
            String request = studentCode + ";" + qCode;
            writer.write(request);
            writer.newLine(); // rất quan trọng để server đọc được hết
            writer.flush();
            System.out.println("Sent: " + request);

            // b) Nhận danh sách domain
            String response = reader.readLine();
            System.out.println("From server: " + response);

            // c) Tìm các domain .edu
            StringBuilder eduDomains = new StringBuilder();
            if (response != null && !response.isEmpty()) {
                String[] tokens = response.split(",");
                for (String domain : tokens) {
                    domain = domain.trim();
                    if (domain.endsWith(".edu")) {
                        if (eduDomains.length() > 0) eduDomains.append(", ");
                        eduDomains.append(domain);
                    }
                }
            }

            String result = eduDomains.toString();
            writer.write(result);
            writer.newLine();
            writer.flush();
            System.out.println("Sent: " + result);

            System.out.println("End program");

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout khi chờ dữ liệu từ server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}


