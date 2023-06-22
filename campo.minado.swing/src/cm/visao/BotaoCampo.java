package cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import cm.modelo.Campo;
import cm.modelo.CampoEvento;
import cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	// para definir alguns estilos dos botões, cores.
	// RGB : RED, GREEN ,BLUE. EXISTE UMA ESCALA ATÉ 247.
	private Color BG_PADRAO = new Color(184, 184, 184);
	private Color BG_MARCAR = new Color(8, 179, 247);
	private Color BG_EXPLODIR = new Color(189, 66, 68);
	private Color TEXTO_VERDE = new Color(0, 100, 0);
	private Campo campo;

	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));

		addMouseListener(this); // interessado em saber cada vez que um evento do mouse acontecer.
		campo.registrarObservador(this); // qualquer evento que acontecer com esse campo, o método eventoOcorreu será
											// chamado.
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloAMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();

		}
		// podemos utilizar os códigos abaixo para realmente garantir que os botões sejam repintados.
		//SwingUtilities.invokeLater(() -> { repaint();
		//validate();
		//});
		
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");

	}

	private void aplicarEstiloAMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");

	}

	private void aplicarEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY)); // borda dos botões campo.
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		setBackground(BG_PADRAO);

		switch (campo.minasNavizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}

		// se a vizinhança não for segura, aparecerá o valor de minas na vizinhança,
		// caso contrario o label do botão será vazio.
		String valor = !campo.vizinhancaSegura() ? campo.minasNavizinhanca() + "" : "";
		setText(valor);
	}

	// interface dos eventos dos mouse //

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.alternarMarcacao();
			;
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}
}

//versão visual da classe campo.
// precisa ser notificado toda vez que um evento ocorrer, para que uma ação seja
// feita no visual do botão, por isso implements CampoOvservador.
// implementado o evento do mauso para escutar o que é feito com o mouse, clicks com botão direito e esquerdo.
