package br.com.avaliacao.reconcavo.servidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {

	protected Socket clientSocket;
	private ServerSocket serverSocket;
	private String caminho;
	private int porta;
	private DataOutputStream out;
	private DataInputStream in;

	public Servidor(String caminho, int porta) {
		this.caminho = caminho;
		this.porta = porta;
	}

	public void iniciar() throws IOException {

		serverSocket = new ServerSocket(porta);
		System.out.println("Conexão com o socket criado");

		while (true) {
			System.out.println("Aguardando por conexões");
			new Servidor(serverSocket.accept());
		}

	}

	public void parar() throws IOException {

		if (!serverSocket.isClosed()) {
			serverSocket.close();
		}
		
		if(null != out){
			out.close();
		}
		
		if(null != in){
			in.close();
		}
		
	}

	private Servidor(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	@Override
	public void run() {
		System.out.println("Cliente conectado !");

		try {
			out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readUTF()) != null) {
				System.out.println("Server: " + inputLine);

				String retorno = pesquisaArquivo(inputLine);

				System.out.print(retorno);

				out.writeUTF(retorno);
				out.flush();

				if (inputLine.equals("!__##desconectar##__!"))
					break;
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problema na comunicação com o Servidor");
		}
	}

	private synchronized String pesquisaArquivo(String termo) {

		File file = new File(this.caminho);
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			while ((text = reader.readLine()) != null) {

				if (text.contains(termo)) {
					sb.append(text);
					sb.append(System.lineSeparator());
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}

		return sb.toString();

	}

}
