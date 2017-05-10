package Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TcpMultiChatClient {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("���̵� �Է��ϼ���!~");
		String str =sc.nextLine();
		if (str != null) {
			System.out.println("USAGE : java TcpMultiChatClient ��ȭ��");
		} //end if
		else if(str == null){
			System.exit(0);
		}
		
		try {
			Socket c_socket = new Socket("localhost", 7777);
			
			System.out.println("������ ���� �Ǿ����ϴ�.");
			Thread sender = new Thread(new ClientSender(c_socket, str));
			Thread receiver = new Thread(new ClientRecievier(c_socket));
			sender.start();			receiver.start();
		} catch (Exception e) {			e.printStackTrace();		}		
	} //main end
	
	static class ClientSender extends Thread {
		Socket c_socket;
		DataOutputStream dos;
		String name;
		
		public ClientSender(Socket c_socket, String name) {   // inner class (�Ű����� 2�� �������Լ�)
			this.c_socket = c_socket;
			try {
				dos = new DataOutputStream(c_socket.getOutputStream()); // ��Ʈ��ũ�� ���ؼ� ����
				this.name = name;
			} catch (Exception e) {		e.printStackTrace();	}			
		}

		public void run() {
			Scanner sc = new Scanner(System.in);
			try {
				if ( dos != null )  dos.writeUTF(name);
				while ( dos != null ) dos.writeUTF("[" + name + "]" + sc.nextLine());
			} catch (Exception e) {		e.printStackTrace();	}			
		}
	} //ClientSender end
	
	static class ClientRecievier extends Thread {
		Socket c_socket;
		DataInputStream dis;
		
		public ClientRecievier(Socket c_socket) { //�Ű����� 1�� �������Լ�
			this.c_socket = c_socket;
			try {
				dis = new DataInputStream(c_socket.getInputStream());
			} catch (Exception e) {		e.printStackTrace();	}
		}

		public void run() {
			while(true){
			if( dis != null ) {
					try {
						System.out.println(dis.readUTF());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			}// while end
		}
	} // ClientRecievier end	
	
		
		
	

