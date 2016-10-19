package br.com.avaliacao.reconcavo.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.com.avaliacao.reconcavo.cliente.Cliente;
import br.com.avaliacao.reconcavo.servidor.Servidor;
import br.com.avaliacao.reconcavo.util.Constantes;


/**
 * TelaServidor é a classe que apresenta as informações via SWING.
 * @author alex.costa
 *
 */
public class TelaServidor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtPorta;
	private JTextField txtCaminho;
	private JButton botaoIniciar;
	private JButton botaoParar;
	private Thread threadServidor;
	private String caminho;
	private int porta;
	
	public TelaServidor() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle("Servidor");
		super.setSize(400, 200);
		super.setVisible(true);
		super.setResizable(false);
		this.configuraComponentes();
	}

	private void configuraComponentes() {
		Container cp = this.getContentPane();
		cp.setLayout(new GridLayout(4, 2));
		JLabel porta = new JLabel(" Porta:");
		txtPorta = new JTextField();
		cp.add(porta);
		cp.add(txtPorta);
		txtCaminho = new JTextField();
		cp.add(new JLabel(" Caminho:"));
		cp.add(txtCaminho);
		botaoIniciar = new JButton("Iniciar");
		cp.add(botaoIniciar);
		botaoParar = new JButton("Parar");
		cp.add(botaoParar);
		botaoIniciar.addActionListener(new BotaoIniciarActionListner());
		botaoParar.setEnabled(false);
		botaoParar.addActionListener(new BotaoPararActionListner());
	}

	/**
	 * BotaoIniciarActionListner é a classe que representa a ActionListener no botão de iniciar socket
	 * @author alex.costa
	 *
	 */
	public class BotaoIniciarActionListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(!this.validaCampos()){
				return;
			}
			
			caminho = txtCaminho.getText();
			porta = Integer.parseInt(txtPorta.getText());
			threadServidor = new Servidor(caminho, porta);
			threadServidor.start();
			
			controlaComponentes(false);
		}
		
		private boolean validaCampos(){
			if("".equals(txtCaminho.getText())){
				JOptionPane.showMessageDialog(null, "Campo Caminho obrigatório", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			
			if("".equals(txtPorta.getText())){
				JOptionPane.showMessageDialog(null, "Campo Porta obrigatório", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			
			File file = new File(txtCaminho.getText());
			
			if(!file.exists()){
				JOptionPane.showMessageDialog(null, "Arquivo não existe", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			
			return true;
		}
	}
	
	/**
	 * BotaoPararActionListner é a classe que representa a ActionListener no botão de parar socket
	 * @author alex.costa
	 *
	 */
	public class BotaoPararActionListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Cliente clienteServerParar = new Cliente(Constantes.IP_LOCAL, porta);
			try {
				clienteServerParar.conectar(Constantes.CODIGO_PARA_SERVIDOR);
				clienteServerParar.paraServidorPeloCliente();
			} catch (IOException e1) {
				System.err.println("Erro ao desconectar.");
			}finally {
				controlaComponentes(true);
			}

		}
	}

	private void controlaComponentes(boolean status) {
		if (status) {
			botaoIniciar.setEnabled(true);
			botaoParar.setEnabled(false);
		} else {
			botaoParar.setEnabled(true);
			botaoIniciar.setEnabled(false);
		}

	}

	/**
	 * Metodo para inicializar a TelaServidor
	 * @param args parametros iniciais
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TelaServidor();
			}
		});
	}
}
