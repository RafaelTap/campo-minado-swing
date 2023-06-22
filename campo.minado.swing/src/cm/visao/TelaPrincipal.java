package cm.visao;

import javax.swing.JFrame;

import cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame{ // por herança ele é um Jframe
	
	// construtor da tela
	
	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50); 
		add(new PainelTabuleiro(tabuleiro)); // representa visualmente o tabuleiro
		
		//PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);
		//add(tabuleiro);
		
		setTitle("Campo Minado"); // título.
		setSize(690, 438); //tamanho.
		setLocationRelativeTo(null); // localização da tela, neste caso, centralizada.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // seleciona por padrão a ação que segue após o usuário apertar o fechar, deve ser selicionado uma opção.
		setVisible(true); // mostra a janela ou esconde, depende do paramentro passado.
		
		
	
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
		
	}

}
