/*
[Mã câu hỏi (qCode): eTYdhrdX].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectOutputStream/ObjectInputStream) để gửi/nhận và chuẩn hóa thông tin địa chỉ của khách hàng.
Biết rằng lớp TCP.Address có các thuộc tính (id int, code String, addressLine String, city String, postalCode String) và trường dữ liệu private static final long serialVersionUID = 20180801L.
a. Gửi đối tượng là một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;A1B2C3D4"
b. Nhận một đối tượng là thể hiện của lớp TCP.Address từ server. Thực hiện chuẩn hóa thông tin addressLine bằng cách:
•	Chuẩn hóa addressLine: Viết hoa chữ cái đầu mỗi từ, in thường các chữ còn lại, loại bỏ ký tự đặc biệt và khoảng trắng thừa (ví dụ: "123 nguyen!!! van cu" → "123 Nguyen Van Cu") 
•	Chuẩn hóa postalCode: Chỉ giữ lại số và ký tự "-" ví dụ: "123-456"
c. Gửi đối tượng đã được chuẩn hóa thông tin địa chỉ lên server.
d. Đóng kết nối và kết thúc chương trình.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import TCP.Address;

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


