/*
[Mã câu hỏi (qCode): trSQ1EYI].  Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). 
Yêu cầu xây dựng chương trình client thực hiện kết nối tới server trên sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự: 
a.	Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode". Ví dụ: "B16DCCN999;C64967DD"
b.	Nhận dữ liệu từ server là một chuỗi gồm các giá trị nguyên được phân tách với nhau bằng  "|"
Ex: 2|5|9|11
c.	Thực hiện tìm giá trị tổng của các số nguyên trong chuỗi và gửi lên server
Ex: 27
d.	Đóng kết nối và kết thúc
 */
package lap.trinh.mang;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class trSQ1EYI_TCP_ByteStream {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


	// TODO Auto-generated method stub
	String serverAddress = "203.162.10.109";
	int port = 2206;
	String msv = "B22DCVT025";
	String qCode = "trSQ1EYI";
		
		try (Socket socket = new Socket(serverAddress, port)){
			socket.setSoTimeout(5000);
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			String request = msv + ";" + qCode;
			out.write(request.getBytes());
			out.flush();
			System.out.println("Sent: " + request.trim());
			
			byte[] buffer = new byte[1024];
			int bytesRead = in.read(buffer);
			if(bytesRead == -1) {
				System.err.println("Khong nhan dc du lieu server");
				return;
			}
			
			String response = new String(buffer, 0, bytesRead).trim();
			System.out.println("From server: " + response);
			
			String[] parts = response.split("\\|");
			int s = 0;
			for(String p : parts) {
				s += Integer.parseInt(p.trim());
			}
			
			String result = s + "\n";
			out.write(result.getBytes());
			out.flush();
			System.out.println("Sent: " + s);
			
			in.close();
			out.close();
			socket.close();
			System.out.println("End program");
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}



