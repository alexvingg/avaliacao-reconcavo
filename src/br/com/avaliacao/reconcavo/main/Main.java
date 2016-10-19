package br.com.avaliacao.reconcavo.main;

import javax.swing.SwingUtilities;

import br.com.avaliacao.reconcavo.gui.TelaCliente;
import br.com.avaliacao.reconcavo.gui.TelaServidor;
import br.com.avaliacao.reconcavo.util.Constantes;

/**
 * Classe principal responsavel por executar programa
 * @author alex.costa
 *
 */
public class Main {

	private static String programa = "";
	
	/**
	 * Método principal da classe
	 * @param args 1 para tela servidor 2 para tela cliente.
	 */
	public static void main(String[] args){
		
		if(args.length > 0){
			programa = args[0];
		}
		
		if("" == programa){
			programa = "1";
		}
		
		if(Constantes.PROGRAMA_CLIENTE.equals(programa)){
			SwingUtilities.invokeLater(new Runnable() {
		         @Override
		         public void run() {
		            new TelaCliente();  // Let the constructor do the job
		         }
		      });
		}else{
			SwingUtilities.invokeLater(new Runnable() {
		         @Override
		         public void run() {
		            new TelaServidor();  // Let the constructor do the job
		         }
		      });
		}
		
	}
	
}
