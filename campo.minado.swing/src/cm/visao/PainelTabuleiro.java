package cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel { // agrupador de outros componentes, tem uma série de botões ( conteiner).
	// colocar todos os botões das linhas e colunas

	public PainelTabuleiro(Tabuleiro tabuleiro) {

		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getLinhas())); // método para difinir como os
																					// componentes visuais serão
																					// organizados na tela.

		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		tabuleiro.registrarObservador(e -> {

			SwingUtilities.invokeLater(() -> {

			if (e.isGanhou()) {
				JOptionPane.showMessageDialog(this, "Ganhou :)");
			} else {

				JOptionPane.showMessageDialog(this, "Perdeu :(");
			}

			tabuleiro.reiniciar();
			});
		});

	}

}
