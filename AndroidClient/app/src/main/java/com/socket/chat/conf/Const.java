package com.socket.chat.conf;

public class Const {
	public static final String TAG = "xlui";
	public static final String placeholder = "placeholder";

	/**
	 * <code>im</code> in address is the endpoint configured in server.
	 * If you are using AVD provided by Android Studio, you should uncomment the upper address.
	 * If you are using Genymotion, nothing else to do.
	 * If you are using your own phone, just change the server address and port.
	 */
	 private static final String IP_ADDRESS = "192.168.68.107";
//	 private static final String IP_ADDRESS = "172.16.213.166";
	public static final String address = "ws://"+IP_ADDRESS+":8080/im/websocket";
	public static final String rest_address = "http://"+IP_ADDRESS+":8080";
	public static final String chat = "/chat";
	public static final String chatResponse = "/user/" + placeholder + "/msg";
}
