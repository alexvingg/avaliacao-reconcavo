package br.com.avaliacao.reconcavo.cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

	private Socket echoSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private int porta;
	private String ip;
	
	public Cliente(String ip, int porta){
		this.porta = porta;
		this.ip = ip;
	}
	
	public void conectar() throws UnknownHostException, IOException{
		echoSocket = new Socket();
		echoSocket.connect(new InetSocketAddress(this.ip, this.porta), 10000);
		out = new DataOutputStream(new BufferedOutputStream(echoSocket.getOutputStream()));
		in = new DataInputStream(new BufferedInputStream(echoSocket.getInputStream()));
	}
	
	public String buscar(String termo) throws IOException{
		out.writeUTF(termo);
		out.flush();
		return in.readUTF();
	}
	
	public void desconectar() throws IOException{
		out.writeUTF("!__##desconectar##__!");
		out.flush();
		out.close();
		in.close();
		echoSocket.close();
	}

//	public static void main(String[] args){
//		Cliente c = new Cliente("127.0.0.1", 10008);
//		
//		try {
//			c.conectar();
//			
//			while(true){
//				
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
}
