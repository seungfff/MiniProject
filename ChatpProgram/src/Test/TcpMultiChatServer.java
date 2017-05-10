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
	static HashMap clients; // 객체 선언 (key,value)
    static HashMap clients2;

	public TcpMultiChatServer() { // 생성자함수
		clients = new HashMap();	
		clients2 = new HashMap();
		Collections.synchronizedMap(clients);

	}

	public void start() {
		ServerSocket s_socket = null; // 서버소켓 1
		Socket c_socket = null;// 클라이언트 소켓 1

		try {
			s_socket = new ServerSocket(7777);
			System.out.println("서버 시작 되었습니다");
			
			while (true) {
				c_socket = s_socket.accept(); // 응답대기
				System.out.println("[" + c_socket.getInetAddress() + " :" + c_socket.getPort() + "] 에서 접속하였습니다");
				ServerReceiver thread = new ServerReceiver(c_socket); // user
																		// class
																		// //서버소켓
																		// 가져감
				thread.start();  new KickOut(c_socket).start();
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start() end

	public void sendToAll(String msg) { // user method
		Iterator it = clients.keySet().iterator(); // key 값 출력

		while (it.hasNext()) { // 다음 요소가 있다면....
			try {
				DataOutputStream dos = (DataOutputStream) clients.get(it.next()); // 다음요소
				dos.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // end while
	} // sendToAll(String msg) end

	class ServerReceiver extends Thread { // inner class
		Socket c_socket; // 클라이언트 소켓
		DataInputStream dis; // 자료형 타입
		DataOutputStream dos; // 자료형 타입

		public ServerReceiver(Socket c_socket) { // 생성자 함수
			this.c_socket = c_socket;
			try {
				dis = new DataInputStream(c_socket.getInputStream()); // 네트워크를 통해서 읽기
				dos = new DataOutputStream(c_socket.getOutputStream());// 네트워크를 통해서 쓰기
																		
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // constructor end

		public void run( ) { // 스레드 실행부
			String name = "";
			try {
				name = dis.readUTF(); // 한글 깨짐방지			
			    sendToAll("#" + name + " 님이 입장하셨습니다."); // user method
				clients.put(name, dos); // HashMap 넣기
				System.out.println("현재 서버 접속자 수는 : " + clients.size() + " 입니다"); // 접속자
			    while (dis != null) {
					sendToAll(dis.readUTF());
				}
			} catch (Exception e) {
			} finally {
				sendToAll("#" + name + " 님이 나가셨습니다.");
				clients.remove(name); // 접속자 수 제거하기
				System.out.println("[" + c_socket.getInetAddress() + " : " + c_socket.getPort() + "] 에서 접속 종료하였습니다.");
				System.out.println("현재 서버 접속자 수는 : " + clients.size() + " 입니다");
			}
		} // run() end
	} // ServerReceiver class end
	class KickOut extends Thread{
		Socket c_socket; // 클라이언트 소켓
		DataInputStream dis; // 자료형 타입
		DataOutputStream dos;
		public KickOut(Socket c_socket) { // 생성자 함수
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
			System.out.println("추방할 아이디를 입력하세요");
			String banId =sc.nextLine();
			String name = null;
			try{
				name = dis.readUTF();
				System.out.println(name);
				clients2.put(name, c_socket);
				
				if(clients2.containsKey(name)){
					System.out.println("들어감");
				}
				System.out.println(clients2.get(name));
				if(banId == name){
					System.out.println(name+"를 추방했습니다");
				
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
