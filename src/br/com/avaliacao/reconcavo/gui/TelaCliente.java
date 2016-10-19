package br.com.avaliacao.reconcavo.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.com.avaliacao.reconcavo.cliente.Cliente;

public class TelaCliente extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtPorta;
	private JTextField txtIp;
	private JTextField txtTermo;
	private JButton botaoConectar;
	private JButton botaoBuscar;
	private Cliente cliente;
	private boolean isStatus = true;


	public TelaCliente() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle("Cliente");
		super.setSize(400, 200);
		super.setVisible(true);
		super.setResizable(false);
		
		this.configuraComponentes();
	}
	
	private void configuraComponentes(){
		Container cp = this.getContentPane();
		cp.setLayout(new GridLayout(4, 2));
		JLabel porta = new JLabel(" Porta:");
		porta.setSize(40, 20);
		txtPorta = new JTextField();
		cp.add(porta);
		cp.add(txtPorta);
		txtIp = new JTextField();
		cp.add(new JLabel(" Ip:"));
		cp.add(txtIp);
		txtTermo = new JTextField();
		cp.add(new JLabel(" Termo:"));
		cp.add(txtTermo);
		botaoConectar = new JButton("Conectar");
		cp.add(botaoConectar);
		botaoBuscar = new JButton("Buscar");
		cp.add(botaoBuscar);
		txtTermo.setEnabled(false);
		botaoBuscar.setEnabled(false);
		botaoBuscar.addActionListener(new BotaoBuscarActionListner());
		botaoConectar.addActionListener(new BotaoConectarActionListner());
	}
	
	public class BotaoConectarActionListner implements ActionListener{

		private String ip;
		private String porta;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			this.ip = txtIp.getText();
			this.porta = txtPorta.getText();

			if("".equals(ip)){
				JOptionPane.showMessageDialog(null, "O IP é obrigatório", "Atenção", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if("".equals(porta)){
				JOptionPane.showMessageDialog(null, "A porta é obrigatório", "Atenção", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(cliente == null){
				cliente = new Cliente(ip, Integer.parseInt(porta));
			}

			try {
				
				if(isStatus){
					controlaCampos(isStatus);
					isStatus = false;
					cliente.conectar();
				}else{
					controlaCampos(isStatus);
					isStatus = true;
					cliente.desconectar();
					cliente = null;
				}
				
			} catch (IOException e1) {
				controlaCampos(isStatus);
				isStatus = true;
				JOptionPane.showMessageDialog(null, "Erro ao conectar", "Atenção", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public class BotaoBuscarActionListner implements ActionListener{

		private String termo;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			this.termo = txtTermo.getText();
			
			if("".equals(termo)){
				JOptionPane.showMessageDialog(null, "O termo é obrigatório", "Atenção", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				String resultado = cliente.buscar(termo);
				
				if("".equals(resultado)){
					resultado = "Não foram encontrados termos.";
				}
				
				JOptionPane.showMessageDialog(null, resultado, "Resultado", JOptionPane.INFORMATION_MESSAGE);

			} catch (UnknownHostException e1) {
				controlaCampos(false);
				JOptionPane.showMessageDialog(null, "Erro ao conectar no host", "Atenção", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				controlaCampos(false);
				JOptionPane.showMessageDialog(null, "Não foi possivel realizar a busca", "Atenção", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	private void controlaCampos(final boolean status){
		if(status){
			botaoConectar.setText("Desconectar");
			botaoBuscar.setEnabled(true);
			txtTermo.setEnabled(true);
			txtIp.setEnabled(false);
			txtPorta.setEnabled(false);
		}else{
			botaoConectar.setText("Conectar");
			botaoBuscar.setEnabled(false);
			txtIp.setEnabled(true);
			txtTermo.setEnabled(false);
			txtTermo.setText("");
			txtPorta.setEnabled(true);
		}
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new TelaCliente();  // Let the constructor do the job
	         }
	      });
	}
	
}
