package com.group.chatroom;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends ChatWindow{
	private ServerSocket ss;
	private ArrayList<Socket> sockList=new ArrayList<Socket>();
	private AcceptThread at=new AcceptThread();
	Server(){
		super("Server");
		ta.append("This is Server...\n");
	}
	protected void start(){
		if(alive==true){
		//	ta.append("Server has already started\n\n\n");
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
				this.ss=new ServerSocket(portNum);
				alive=true;
				ta.append("Start Server Succeed\n\n\n");
				new Thread(at).start();
			}
			catch(IOException io){
				ta.append("Server start failed\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	protected void stop(){
		if(alive==false){
		//	ta.append("Server has already stopped\n");
			return;
		}
		for(Socket tmp:sockList){
			try {
				if(!tmp.isClosed())
					tmp.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			ss.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		alive=false;
		ta.append("Server stopped\n");
	}
	
	protected void send(){
		if(alive==false){
			ta.append("Server is not working now\n");
			return;
		}
		if(sockList.size()<=0){
			ta.append("No client to send data\n");
			return;
		}
		String content;
		if((content=tf.getText()).length()<=0){
			ta.append("Nothing to send\n");
			return;
		}
		tf.setText("");
		DataOutputStream tdos;
		for(Socket tmp:sockList){
			sendData(tmp,content);
		}
	}
	private void sendData(Socket to,String content){
		try {
			DataOutputStream todos=new DataOutputStream(to.getOutputStream());
			todos.writeUTF(Integer.toString(to.getPort())+" : \n"+content);
			todos.flush();
			ta.append("Send to client "+to.getPort()+" : "+content+"\n");
		} catch (IOException e) {
			ta.append("Server send data failed\n");
			e.printStackTrace();
		}
	}
	
	private class ServerThread implements Runnable{
		private Socket s;
	    private DataInputStream dis = null;
		ServerThread(Socket s){
			this.s=s;
			try {
				dis=new DataInputStream(s.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run(){
			String content;
			try {
				while((content=dis.readUTF())!=null){
					ta.append("rcv from "+s.getPort()+" : "+content+"\n");
					for(Socket tmp:sockList){
						if(tmp!=s)
							sendData(tmp,content);
					}
				}
			}
			catch(IOException io){
				try {
					if(dis!=null)
						dis.close();
					if(s!=null)
						s.close();
					sockList.remove(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
				ta.append("client has exited\n");	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private class AcceptThread implements Runnable{
		public void run(){
			while(alive){
			try {
				Socket s = ss.accept();
				sockList.add(s);
				ta.append("recv connect from "+s.getPort()+"\n");
				new Thread(new ServerThread(s)).start();
			} 
			catch (IOException e) {
				ta.append("server not working now\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			}
		}
	}
	public static void main(String[] args){
		Server ser=new Server();
	}
}