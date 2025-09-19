/*
[Mã câu hỏi (qCode): MCHTKViH].  Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectOutputStream/ObjectInputStream) theo kịch bản dưới đây:
Biết lớp TCP.Product gồm các thuộc tính (id int, name String, price double, int discount) và private static final long serialVersionUID = 20231107;
a. Gửi đối tượng là một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;1E08CA31"
b. Nhận một đối tượng là thể hiện của lớp TCP.Product từ server.
c. Tính toán giá trị giảm giá theo price theo nguyên tắc: Giá trị giảm giá (discount) bằng tổng các chữ số trong phần nguyên của giá sản phẩm (price). Thực hiện gán giá trị cho thuộc tính discount và gửi lên đối tượng nhận được lên server.
d. Đóng kết nối và kết thúc chương trình.
 */
import TCP.Product;  
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;




import TCP.Product;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MCHTKViH_TCP_ObjectStream {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2209;
        String studentCode = "B22DCVT025"; // thay bằng MSSV của bạn
        String qCode = "MCHTKViH";

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            socket.setSoTimeout(5000);

            // a) Gửi chuỗi studentCode;qCode
            String request = studentCode + ";" + qCode;
            out.writeObject(request);
            out.flush();
            System.out.println("Sent: " + request);

            // b) Nhận đối tượng Product từ server
            Object obj = in.readObject();
            if (obj instanceof Product) {
                Product product = (Product) obj;
                System.out.println("Received product: " + product);

                // c) Tính discount = tổng các chữ số phần nguyên của price
                int intPart = (int) product.getPrice();
                int sumDigits = 0;
                while (intPart > 0) {
                    sumDigits += intPart % 10;
                    intPart /= 10;
                }
                product.setDiscount(sumDigits);
                System.out.println("Updated product with discount=" + sumDigits);

                // gửi lại đối tượng đã cập nhật
                out.writeObject(product);
                out.flush();
                System.out.println("Sent updated product");
            } else {
                System.err.println("Nhận được kiểu khác: " + obj.getClass());
            }

            System.out.println("End program");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

