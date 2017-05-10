package Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class TcpMultiChatServer {
	static HashMap clients; // ��ü ���� (key,value)
    static HashMap clients2;

	public TcpMultiChatServer() { // �������Լ�
		clients = new HashMap();	
		clients2 = new HashMap();
		Collections.synchronizedMap(clients);

	}

	public void start() {
		ServerSocket s_socket = null; // �������� 1
		Socket c_socket = null;// Ŭ���̾�Ʈ ���� 1

		try {
			s_socket = new ServerSocket(7777);
			System.out.println("���� ���� �Ǿ����ϴ�");
			
			while (true) {
				c_socket = s_socket.accept(); // ������
				System.out.println("[" + c_socket.getInetAddress() + " :" + c_socket.getPort() + "] ���� �����Ͽ����ϴ�");
				ServerReceiver thread = new ServerReceiver(c_socket); // user
																		// class
																		// //��������
																		// ������
				thread.start();  new KickOut(c_socket).start();
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start() end

	public void sendToAll(String msg) { // user method
		Iterator it = clients.keySet().iterator(); // key �� ���

		while (it.hasNext()) { // ���� ��Ұ� �ִٸ�....
			try {
				DataOutputStream dos = (DataOutputStream) clients.get(it.next()); // �������
				dos.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // end while
	} // sendToAll(String msg) end

	class ServerReceiver extends Thread { // inner class
		Socket c_socket; // Ŭ���̾�Ʈ ����
		DataInputStream dis; // �ڷ��� Ÿ��
		DataOutputStream dos; // �ڷ��� Ÿ��

		public ServerReceiver(Socket c_socket) { // ������ �Լ�
			this.c_socket = c_socket;
			try {
				dis = new DataInputStream(c_socket.getInputStream()); // ��Ʈ��ũ�� ���ؼ� �б�
				dos = new DataOutputStream(c_socket.getOutputStream());// ��Ʈ��ũ�� ���ؼ� ����
																		
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // constructor end

		public void run( ) { // ������ �����
			String name = "";
			try {
				name = dis.readUTF(); // �ѱ� ��������			
			    sendToAll("#" + name + " ���� �����ϼ̽��ϴ�."); // user method
				clients.put(name, dos); // HashMap �ֱ�
				System.out.println("���� ���� ������ ���� : " + clients.size() + " �Դϴ�"); // ������
			    while (dis != null) {
					sendToAll(dis.readUTF());
				}
			} catch (Exception e) {
			} finally {
				sendToAll("#" + name + " ���� �����̽��ϴ�.");
				clients.remove(name); // ������ �� �����ϱ�
				System.out.println("[" + c_socket.getInetAddress() + " : " + c_socket.getPort() + "] ���� ���� �����Ͽ����ϴ�.");
				System.out.println("���� ���� ������ ���� : " + clients.size() + " �Դϴ�");
			}
		} // run() end
	} // ServerReceiver class end
	class KickOut extends Thread{
		Socket c_socket; // Ŭ���̾�Ʈ ����
		DataInputStream dis; // �ڷ��� Ÿ��
		DataOutputStream dos;
		public KickOut(Socket c_socket) { // ������ �Լ�
			this.c_socket = c_socket;
			try {
			     dis = new DataInputStream(c_socket.getInputStream());
			     dos = new DataOutputStream(c_socket.getOutputStream());
			} catch (Exception e) {
			e.printStackTrace();
			} 
		}
		@Override
		public void run() {
			Scanner sc = new Scanner(System.in);
			while(true){
			String cm=sc.next();
			if(cm.equalsIgnoreCase("ban")){
			System.out.println("�߹��� ���̵� �Է��ϼ���");
			String banId =sc.nextLine();
			String name = null;
			try{
				name = dis.readUTF();
				System.out.println(name);
				clients2.put(name, c_socket);
				
				if(clients2.containsKey(name)){
					System.out.println("��");
				}
				System.out.println(clients2.get(name));
				if(banId == name){
					System.out.println(name+"�� �߹��߽��ϴ�");
				
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		}
		}
	}
	public static void main(String[] args) {
		new TcpMultiChatServer().start();
	   
	}
}
