package com.group.chatroom;

public class ValidCheck{
	static boolean ipValid(String s){
		 String[] ss = s.split("\\.");
	        if(ss.length != 4){
	            return false;
	        }
	        byte[] bytes = new byte[ss.length];
	        for(int i = 0; i < bytes.length; i++){
	        	int a= Integer.parseInt(ss[i]);
	        	if((i==0&&a<=0)||(a<0||a>255))
	        		return false;
	            bytes[i] = (byte)a;
	        }
	        return true;
	}
	static boolean portValid(String port){
		int portNum=Integer.parseInt(port);
		if(portNum<0||portNum>65535)
			return false;
		else 
			return true;
	}
	public static void main(String[] args){
		String s="0.6.6.256";
		System.out.println(ipValid(s));
	}
}