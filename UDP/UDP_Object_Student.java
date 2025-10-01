/*
[Mã câu hỏi (qCode): KBx5LE6l].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau:
Đối tượng trao đổi là thể hiện của lớp UDP.Student được mô tả:
•	Tên đầy đủ lớp: UDP.Student
•	Các thuộc tính: id String,code String, name String, email String
•	02 Hàm khởi tạo: 
o	public Student(String id, String code, String name, String email)
o	public Student(String code)
•	Trường dữ liệu: private static final long serialVersionUID = 20171107
Thực hiện:
•       Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”. Ví dụ: “;B15DCCN001;EE29C059”
b.	Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Student từ server. Trong đó, các thông tin được thiết lập gồm id và name.
c.	Yêu cầu:
-	Chuẩn hóa tên theo quy tắc: Chữ cái đầu tiên in hoa, các chữ cái còn lại in thường và gán lại thuộc tính name của đối tượng
-	Tạo email ptit.edu.vn từ tên người dùng bằng cách lấy tên và các chữ cái bắt đầu của họ và tên đệm. Ví dụ: nguyen van tuan nam -> namnvt@ptit.edu.vn. Gán giá trị này cho thuộc tính email của đối tượng nhận được
-	Gửi thông điệp chứa đối tượng xử lý ở bước c lên Server với cấu trúc: 08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Student đã được sửa đổi.
d.	Đóng socket và kết thúc chương trình.
 */
package UDP;

import java.io.*;
import java.net.*;
import java.util.Locale;

class Student implements Serializable {
    private static final long serialVersionUID = 20171107L;
    String id;
    String code;
    String name;
    String email;

    public Student(String id, String code, String name, String email) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
    }

    public Student(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return id + ";" + code + ";" + name + ";" + email;
    }
}

public class UDP_Object_Student {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; 
        int port = 2209;
        String studentCode = "B22DCVT025"; // MSSV của bạn
        String qCode = "KBx5LE6l";

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            InetAddress server = InetAddress.getByName(serverAddress);

            // a) Gửi ;studentCode;qCode
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes();
            socket.send(new DatagramPacket(sendData, sendData.length, server, port));
            System.out.println("Sent: " + request);

            // b) Nhận dữ liệu (8 byte requestId + object Student)
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            byte[] fullData = receivePacket.getData();
            String requestId = new String(fullData, 0, 8).trim();
            System.out.println("RequestId: " + requestId);

            // Deserialize Student
            ByteArrayInputStream bais = new ByteArrayInputStream(fullData, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Student st = (Student) ois.readObject();
            System.out.println("Received Student: " + st);

            // c) Xử lý
            st.name = normalizeName(st.name);
            st.email = generateEmail(st.name);
            System.out.println("Modified Student: " + st);

            // d) Serialize và gửi lại
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(st);
            oos.flush();

            byte[] objectData = baos.toByteArray();
            byte[] finalData = new byte[8 + objectData.length];
            byte[] reqIdBytes = requestId.getBytes();
            System.arraycopy(reqIdBytes, 0, finalData, 0, Math.min(8, reqIdBytes.length));
            System.arraycopy(objectData, 0, finalData, 8, objectData.length);

            socket.send(new DatagramPacket(finalData, finalData.length, server, port));
            System.out.println("Sent modified Student back to server!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    // Chuẩn hóa tên: chữ đầu hoa, còn lại thường
    private static String normalizeName(String name) {
        String[] words = name.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            sb.append(Character.toUpperCase(w.charAt(0)))
              .append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    // Sinh email: lastname + initials @ptit.edu.vn
    private static String generateEmail(String name) {
        String[] parts = name.trim().toLowerCase(Locale.ROOT).split("\\s+");
        if (parts.length == 0) return "";
        String last = parts[parts.length - 1];
        StringBuilder sb = new StringBuilder(last);
        for (int i = 0; i < parts.length - 1; i++) {
            sb.append(parts[i].charAt(0));
        }
        sb.append("@ptit.edu.vn");
        return sb.toString();
    }    
}

