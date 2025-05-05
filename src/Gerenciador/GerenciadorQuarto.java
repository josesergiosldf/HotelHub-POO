package Gerenciador;

import Hotel.Quarto;
import java.util.List;

public interface GerenciadorQuarto {
    void adicionarQuarto(Quarto quarto);
    Quarto buscarQuartoPorNumero(int numero);
    boolean removerQuarto(int numero);
    List<Quarto> listarQuartos();
    List<Quarto> listarQuartosDisponiveis();
    void alterarDisponibilidade(int numero, boolean disponivel);
}
