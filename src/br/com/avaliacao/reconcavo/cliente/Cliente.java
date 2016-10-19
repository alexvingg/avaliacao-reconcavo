package br.com.avaliacao.reconcavo.cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.avaliacao.reconcavo.thread.ClienteThread;
import br.com.avaliacao.reconcavo.util.Constantes;

/**
 * Cliente � uma classe respons�vel por conectar ao socket.
 * @author alex.costa
 *
 */
public class Cliente {

	private Socket clienteSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private int porta;
	private String ip;
	
	/**
	 * Cria um novo cliente definindo ip e porta.
	 * 
	 * @param ip do servidor
	 * @param porta do servidor
	 */
	public Cliente(String ip, int porta){
		this.porta = porta;
		this.ip = ip;
	}
	
	/**
	 * M�todo responv�vel por conectar o cliente ao socket. Esse metodo assim que conecta envia uma mensagem para o socket para verificar se
	 * � para ser finalizado o servidor.
	 * @param statusServidor 1 para servidor / 2 continua
	 * @see Constantes
	 * @throws UnknownHostException Host n�o encontrado
	 * @throws IOException Erro conectar ao socket
	 */
	public void conectar(int statusServidor) throws UnknownHostException, IOException{
		clienteSocket = new Socket();
		clienteSocket.connect(new InetSocketAddress(this.ip, this.porta), 10000);
		out = new DataOutputStream(new BufferedOutputStream(clienteSocket.getOutputStream()));
		in = new DataInputStream(new BufferedInputStream(clienteSocket.getInputStream()));
		out.write(statusServidor);
		out.flush();
	}
	
	/**
	 * M�todo respons�vel por enviar para o servidor a string a ser pesquisada no arquivo.
	 * @see ClienteThread
	 * @param termo a ser pesquisado
	 * @return String encontrada no arquivo
	 * @throws IOException Erro conectar ao socket
	 */
	public String buscar(String termo) throws IOException{
		out.writeUTF(termo);
		out.flush();
		return in.readUTF();
	}
	
	/**
	 * M�todo respons�vel por encerrar conex�o com o socket.
	 * @throws IOException Erro conectar ao socket
	 */
	public void desconectar() throws IOException{
		out.close();
		in.close();
		clienteSocket.close();
	}
	
	public void paraServidorPeloCliente() throws IOException{
		out.writeUTF(Constantes.CODIGO_DESCONECTA_CLIENTE);
		out.flush();
		out.close();
		in.close();
		clienteSocket.close();
	}
}
