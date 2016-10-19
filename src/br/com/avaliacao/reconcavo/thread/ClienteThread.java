package br.com.avaliacao.reconcavo.thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

import br.com.avaliacao.reconcavo.util.Constantes;


/**
 * A ClienteThread é uma Thread responvável por receber os 
 * sockets e realizar as operações definidas. 
 * 
 * @author alex.costa
 *
 */
public class ClienteThread extends Thread {

	private DataOutputStream out;
	private DataInputStream in;
	protected Socket clientSocket;
	private final String caminho;
	
	/**
	 * Cria uma nova ClienteThread recebendo clientSocket e caminho do arquivo.
	 * @param clientSocket cliente que se conectou ao socket
	 * @param caminho caminho do arquivo
	 */
	public ClienteThread(Socket clientSocket, String caminho) {
		this.clientSocket = clientSocket;
		this.caminho = caminho;
	}

	@Override
	public void run() {
			    
		System.out.println("Cliente conectado !");

		try {
			
			out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readUTF()) != null) {
				
				if (inputLine.equals(Constantes.CODIGO_DESCONECTA_CLIENTE)){
					break;
				}
				
				System.out.println("Server: " + inputLine);
				String retorno = pesquisaArquivo(inputLine);
				System.out.print(retorno);
				out.writeUTF(retorno);
				out.flush();
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problema na comunicação com o Servidor");
		}
	}
	
	/**
	 * Método responsável por pesquisar determinada string dentro de um arquivo.
	 * @param termo a ser pesquisado
	 * @return String achado ou algum erro encontrado no processo.
	 */
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
			sb.append("Arquivo não encontrado no sistema");
		} catch (IOException e) {
			sb.append("Erro ao ler arquivo");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.err.println("Erro ao fechar arquivo");
			}
		}
		return sb.toString();
	}
}
