package com.group.chatroom;
import java.awt.*;
import java.awt.event.*;

public abstract class ChatWindow extends Frame implements ActionListener{
	protected Panel p=new Panel(new FlowLayout(FlowLayout.LEFT,5,5));
	protected Label ipLabel=new Label("Ip:",Label.CENTER);
	protected TextField ipText=new TextField(30);
	protected Label portLabel=new Label("Port:",Label.CENTER);
	protected TextField portText=new TextField(20);
	protected Button start=new Button("Start");
	protected Button quit=new Button("Quit");
	protected Button send=new Button("send");
	protected TextArea ta=new TextArea("Welcome !!!\n",24,78);
	protected TextField tf=new TextField(71);
	protected boolean alive=false;
	ChatWindow(String name){
		super(name);
		ipLabel.setBackground(Color.GRAY);
		portLabel.setBackground(Color.GRAY);
		p.add(ipLabel);
		p.add(ipText);
		p.add(portLabel);
		p.add(portText);
		p.add(start);
		p.add(quit);
		setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
		add(p);
		ta.setEditable(false);
		add(ta);
		tf.addActionListener(this);
		add(tf);
		add(send);
		start.addActionListener(this);
		quit.addActionListener(this);
		send.addActionListener(this);
		
		setSize(600,500);
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				stop();
				System.exit(0);
			}
		});	
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==start){
			start();
		}
		else if(e.getSource()==quit){
			stop();
		}
		else if((e.getSource()==send)||e.getSource()==tf){
			send();
		}
	}
	protected abstract void start();
	protected abstract void stop();
	protected abstract void send();
}