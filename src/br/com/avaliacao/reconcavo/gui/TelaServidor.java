package br.com.avaliacao.reconcavo.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.com.avaliacao.reconcavo.servidor.Servidor;


public class TelaServidor extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtPorta;
	private JTextField txtCaminho;
	private JButton botaoIniciar;
	private JButton botaoParar;
	private Servidor servidor;

	public TelaServidor() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle("Servidor");
		super.setSize(400, 200);
		super.setVisible(true);
		super.setResizable(false);
		
		this.configuraComponentes();
	}
	
	private void configuraComponentes(){
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
	
	public class BotaoIniciarActionListner extends Thread implements ActionListener{

		private String caminho;
		private int porta;
		
		@Override
		public void actionPerformed(ActionEvent e) {

			this.caminho = txtCaminho.getText();
			this.porta = Integer.parseInt(txtPorta.getText().toString());
			
			if(null == servidor){
				servidor = new Servidor(caminho, porta);
			}
			
			new Thread(this).start();
			
		}
		
		@Override
		public void run() {
			try {
				servidor.iniciar();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Erro ao iniciar servidor", "Atenção", JOptionPane.ERROR_MESSAGE);
				controlaComponentes(false);
			}
		}
	}
	
	public class BotaoPararActionListner  implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if(null != servidor){
				JOptionPane.showMessageDialog(null, "Serviço já foi parado", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			try {
				servidor.parar();
				servidor = null;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Erro ao parar servidor", "Atenção", JOptionPane.ERROR_MESSAGE);
			}finally {
				controlaComponentes(true);
			}
			
		}
	}

	private void controlaComponentes(boolean status){
		if(status){
			botaoIniciar.setEnabled(true);
		}else{
			botaoParar.setEnabled(false);
		}
			
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new TelaServidor();  // Let the constructor do the job
	         }
	      });
	}
}
