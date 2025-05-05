package Repository;

import Gerenciador.GerenciadorQuarto;
import Hotel.Quarto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryQuarto implements GerenciadorQuarto {
    private final Map<Integer, Quarto> quartosPorNumero = new HashMap<>();

    @Override
    public void adicionarQuarto(Quarto quarto) {
        quartosPorNumero.put(quarto.getNumero(), quarto);
    }

    @Override
    public Quarto buscarQuartoPorNumero(int numero) {
        return quartosPorNumero.get(numero);
    }

    @Override
    public boolean removerQuarto(int numero) {
        return quartosPorNumero.remove(numero) != null;
    }

    @Override
    public List<Quarto> listarQuartos() {
        return new ArrayList<>(quartosPorNumero.values());
    }

    @Override
    public List<Quarto> listarQuartosDisponiveis() {
        List<Quarto> disponiveis = new ArrayList<>();

        for (Quarto q : quartosPorNumero.values()) {
            if (q.isDisponivel()) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    @Override
    public void alterarDisponibilidade(int numero, boolean disponivel) {
        Quarto quarto = quartosPorNumero.get(numero);

        if (quarto != null) {
            quarto.setDisponivel(disponivel);
        }
    }
}
