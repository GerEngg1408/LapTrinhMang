/*
[Mã câu hỏi (qCode): 8VeXQtd3].  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau:
Đối tượng trao đổi là thể hiện của lớp UDP.Employee được mô tả:
    Tên đầy đủ lớp: UDP.Employee
    Các thuộc tính: id (String), name (String), salary (double), hireDate (String)
    Hàm khởi tạo:
        public Employee(String id, String name, double salary, String hireDate)
    Trường dữ liệu: private static final long serialVersionUID = 20261107L

Thực hiện:
a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode". Ví dụ: ";B23DCCN006;ITleSdqV"
b. Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Employee từ server. Trong đó, các thuộc tính id, name, salary và hireDate đã được thiết lập sẵn.
c. Thực hiện:
    Chuẩn hóa name: viết hoa chữ cái đầu của mỗi từ, ví dụ "john doe" thành "John Doe".
    Tăng salary: tăng x% lương, với x bằng tổng các chữ số của năm bắt đầu làm việc (hireDate).
    Chuyển đổi hireDate từ định dạng yyyy-mm-dd sang dd/mm/yyyy. Ví dụ: "2023-07-15" thành "15/07/2023".
    Gửi lại đối tượng đã được chuẩn hóa về server với cấu trúc: 08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Employee đã được sửa đổi.
d. Đóng socket và kết thúc chương trình.
 */
package UDP;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

class Employee implements Serializable {
    private static final long serialVersionUID = 20261107L;
    String id;
    String name;
    double salary;
    String hireDate;

    public Employee(String id, String name, double salary, String hireDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + salary + ";" + hireDate;
    }
}

public class UDP_Object_Employee {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int port = 2209;
        String studentCode = "B22DCVT025"; // MSSV của bạn
        String qCode = "8VeXQtd3";

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            InetAddress server = InetAddress.getByName(serverAddress);

            // a) Gửi ";studentCode;qCode"
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes();
            socket.send(new DatagramPacket(sendData, sendData.length, server, port));
            System.out.println("Sent: " + request);

            // b) Nhận dữ liệu (8 byte requestId + object Employee)
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            byte[] fullData = receivePacket.getData();
            String requestId = new String(fullData, 0, 8).trim(); // 8 byte đầu
            System.out.println("RequestId: " + requestId);

            // phần còn lại deserialize thành Employee
            ByteArrayInputStream bais = new ByteArrayInputStream(fullData, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Employee emp = (Employee) ois.readObject();
            System.out.println("Received Employee: " + emp);

            // c.1) Chuẩn hoá name
            emp.name = normalizeName(emp.name);

            // c.2) Tăng salary theo % tổng chữ số năm
            int year = Integer.parseInt(emp.hireDate.substring(0, 4));
            int sumDigits = sumOfDigits(year);
            emp.salary = emp.salary * (1 + sumDigits / 100.0);

            // c.3) Đổi format hireDate yyyy-mm-dd → dd/MM/yyyy
            emp.hireDate = convertDate(emp.hireDate);

            System.out.println("Modified Employee: " + emp);

            // d) Gửi lại (8 byte requestId + object Employee đã sửa)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(emp);
            oos.flush();

            byte[] objectData = baos.toByteArray();

            // ghép lại: 8 byte requestId + object
            byte[] finalData = new byte[8 + objectData.length];
            byte[] reqIdBytes = requestId.getBytes();
            System.arraycopy(reqIdBytes, 0, finalData, 0, Math.min(8, reqIdBytes.length));
            System.arraycopy(objectData, 0, finalData, 8, objectData.length);

            socket.send(new DatagramPacket(finalData, finalData.length, server, port));
            System.out.println("Sent modified Employee back to server!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    private static String normalizeName(String name) {
        String[] words = name.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                  .append(w.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static int sumOfDigits(int year) {
        int sum = 0;
        while (year > 0) {
            sum += year % 10;
            year /= 10;
        }
        return sum;
    }

    private static String convertDate(String hireDate) throws Exception {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        return out.format(in.parse(hireDate));
    }
}

