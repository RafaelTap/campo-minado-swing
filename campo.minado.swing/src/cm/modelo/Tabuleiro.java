package cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

	private final int linhas;
	private final int colunas;
	private final int minas;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		// forma de inicializar o tabuleiro é com o construtor.

		gerarCampos(); // so será executado uma vez.
		associarOsVizinhos(); // só será executado uma vez.
		SortearMinas(); // executado sempre que reinicializaar o jogo.

	}

	public void paraCadaCampo (Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));

	}

	public void abrir(int linha, int coluna) { // função para abrir o campo a partir do índice.

		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacao());

	}

	public void alternarMarcacao(int linha, int coluna) { // função para abrir o campo a partir do índice.
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alternarMarcacao());

	}

	private void gerarCampos() {
		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this); // instância atual para observador
				campos.add(campo); // adiciona o campo.
			}

		}
	}

	private void associarOsVizinhos() { // percorre a lista 2 vezes para associar os vizinhos.
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void SortearMinas() { // define o laço até que a quantidade de minas armadas seja a quantidade
									// desejada.
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			int aleatorio = (int) (Math.random() * campos.size()); // gera um valor aleatório e multiplica pelo tamanho
																	// da lista campos. Primeiro faz a multiplicação
																	// e depois faz o cast para um valor inteiro e
																	// associa à variável.
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas); // enquanto for menor que a quantdade de minas armadas, o laço será executado
										// até igualar.
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado()); // se todos os campos têm o objetivo alcançando,
																		// significa que ganhou o jogo.
	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar()); // ainda permanece os vizinhos, mas os atributos serão mudados.
		SortearMinas();
	}

	
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {

		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
			System.out.println("ganhou");

		}

	}

	private void mostrarMinas() {
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));

	}

}
