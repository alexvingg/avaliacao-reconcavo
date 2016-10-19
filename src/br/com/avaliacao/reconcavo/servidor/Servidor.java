package br.com.avaliacao.reconcavo.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.avaliacao.reconcavo.thread.ClienteThread;
import br.com.avaliacao.reconcavo.util.Constantes;

/**
 * Servidor é uma thread responsável por criar a conexão com o socket e receber os Clientes 
 * que se conectam.
 * @author alex.costa
 * @see ClienteThread
 *
 */
public class Servidor extends Thread {

	protected Socket clientSocket;
	private ServerSocket serverSocket;
	private String caminho;
	private int porta;

	/**
	 * Cria um novo Servidor definindo caminho e porta
	 * @param caminho do arquivo
	 * @param porta para inicializar socket
	 */
	public Servidor(String caminho, int porta) {
		this.caminho = caminho;
		this.porta = porta;
	}

	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(porta);
			System.out.println("Conexão com o servidor criado");
			while (true) {

				clientSocket = serverSocket.accept();

				int code = clientSocket.getInputStream().read();

				if (code == Constantes.CODIGO_PARA_SERVIDOR) {
					break;
				}

				System.out.println("Aguardando por conexões");
				new ClienteThread(clientSocket, caminho).start();

			}
		} catch (IOException e) {
			System.err.println("Erro ao conectar ao servidor.");
		}

		try {
			serverSocket.close();
			System.out.println("Conexão com o servidor perdida");
		} catch (IOException e) {
			System.out.println("Erro ao desconectar com o servidor");

		}

	}

}
