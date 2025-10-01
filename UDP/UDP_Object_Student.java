/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
