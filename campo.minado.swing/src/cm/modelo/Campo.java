package cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false; // marcado com uma bandeira, tem uma mina, não deve ser aberto

	private List<Campo> vizinhos = new ArrayList<>(); // essa lista só aceita elementos do tipo campo.Auto
														// relacionamento

	private List<CampoObservador> observadores = new ArrayList<>();

	// private List <BiConsumer<Campo,CampoEvento>> observadores2 = new
	// ArrayList<>();
	// opção de associar um evento a um observador.

	public Campo(int linha, int coluna) {

		this.linha = linha;
		this.coluna = coluna;
	}

	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}

	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}

	public boolean adicionarVizinho(Campo vizinho) { // a lógica se baseia na distância entre os campos.

		// determina o que é diagonal
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(linha - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;

		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}

	public void alternarMarcacao() { // Marcado com bandeira
		if (!aberto) {
			marcado = !marcado;

			if (marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrir() {
		if (!aberto && !marcado) {

			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			setAberto(true);

			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir()); // abre os campos enquanto estiver seguro.

			}

			return true;

		} else {

			return false;

		}

	}

	public boolean vizinhancaSegura() {

		return vizinhos.stream().noneMatch(v -> v.minado); // verifica se a vizinhança está segura ou não.

	}

	public void minar() {
		minado = true;
	}

	public boolean isMinado() {
		return minado;
	}

	public boolean isMarcado() { // funciona como "get", mas não tem o nome "get". //
		return marcado;

	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;

		if (aberto) {
			notificarObservadores(CampoEvento.ABRIR); // notifica os observadores que o evento abrir acontecer.
		}
	}

	public boolean isAberto() {

		return aberto;
	}

	public boolean isFechado() {

		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	public int minasNavizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}

	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);

	}

}

// fluxo: alguém se registra no campo observador e vai para a lista, para notificar e mcima de um evento 
//que ocorreu, percorrer todos os observers cadastrado para cada um deles, chamaremos um evento.   
