import java.net.*;
import java.io.*;
public class Server{
	public static  void main(String args[]) throws Exception{
		//�����Ҫһ������˿ڣ�����һ������������˿�
		ServerSocket server = new ServerSocket(8088);
			//ȡ�÷������˿�
		Socket sock = server.accept();
		
		FileInputStream inputStream = new FileInputStream("F:/���/�����ļ����ĵ�/��ѧ/JSP/jsp��Ƶ/�Լ��ķ�����/a.html");
	//��Ϊ�ǴӶ˿���д�붫���������Ƕ˿��еõ������
		OutputStream outStream = sock.getOutputStream();
		
		int len = 0;
				byte[] text = new byte[1024];
		len = inputStream.read(text);
		while(len!=-1){
					outStream.write(text,0,len);
		}

			inputStream.close();
			outStream.close();
			sock.close();
			server.close();
	}
}