package vc4.client.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import vc4.api.io.BitOutputStream;

import vc4.api.io.BitInputStream;

public class Server {

	public Socket socket;
	BitInputStream in;
	BitOutputStream out;
	
	public Server(String ip, int port) throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
		out = new BitOutputStream(socket.getOutputStream());
		in = new BitInputStream(socket.getInputStream());
	}
}
