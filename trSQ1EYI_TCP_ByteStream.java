/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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


