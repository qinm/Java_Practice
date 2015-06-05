package com.group.chatroom;
import java.io.*;
import java.net.*;


public class Client extends ChatWindow{
	private Socket s;
	private DataOutputStream dos = null;
	private DataInputStream dis=null;
	private ClientThread ct=new ClientThread();
	Client(){
		super("Client");
		ta.append("This is client...\n");
	}
	protected void start(){
		if(alive==true){
		//	ta.append("Client has already connected\n");
			return ;			
		}
		String ip=ipText.getText();
		String port=portText.getText();
		if((ip==null)||(port==null)||(!ValidCheck.ipValid(ip))||(!ValidCheck.portValid(port))){
			ta.append("Wrong IP or Port\nPlease input availble IP and Port...\n");
			return ;
		}
		else{
			try{
				int portNum=Integer.parseInt(port);
				s=new Socket(ip,portNum);
				dos=new DataOutputStream(s.getOutputStream());
				dis=new DataInputStream(s.getInputStream());
				alive=true;
				ta.append("Client "+s.getLocalPort()+" connect to server Succeed\n\n\n");
				new Thread(ct).start();
			}
			catch(IOException io){
				ta.append("Client connect to server failed\nPlease check IP and Port\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	protected void stop(){
		if(alive==false){
		//	ta.append("Client has already stopped\n");
			return;
		}
		try{
			if(dis!=null)
				dis.close();
			if(dos!=null)
				dos.close();
			if(s!=null)
				s.close();		
			alive=false;
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	protected void send(){
		if(alive==false){
			ta.append("Client has not connected with server\n");
			return;
		}
		String content;
		if((content=tf.getText()).length()<=0){
			ta.append("Client has nothing to send\n");
			return;
		}
		tf.setText("");
		try {
			dos.writeUTF(content);
			dos.flush();
			ta.append("myself:\n"+content+"\n\n");
		} catch (IOException io) {
			ta.append("Client send data failed\nPlease check connection\n");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private class ClientThread implements Runnable{
		public void run(){
			if(alive==false||dis==null||s==null){
				return;
			} 
			try{
				String content;
				while((content=dis.readUTF())!=null)
					ta.append(content+"\n\n");
			}
			catch(IOException io){
				ta.append("server closed\n");
				stop();
				System.exit(0);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	public static void main(String[] args){
		Client c=new Client();
	}
}