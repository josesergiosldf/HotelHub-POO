package Users;

import Hotel.Quarto;
import Hotel.Reserva;
import Repository.RepositoryQuarto;
import Repository.RepositoryReserva;
import Repository.RepositoryUsuario;

import java.util.List;

public class Admin extends Usuario {
    public Admin(String nome, String senha, String cpf, String email) {
        super(nome, senha, cpf, email);
    }

    public List<Reserva> getReservasAtivas(RepositoryReserva repoReserva) {
        return repoReserva.listarReservasPorStatus(Reserva.Status.ATIVA);
    }

    public List<Reserva> getHistoricoReservas(RepositoryReserva repoReserva) {
        return repoReserva.listarReservas();
    }

    public List<Quarto> getQuartosDisponiveis(RepositoryQuarto repoQuarto) {
        return repoQuarto.listarQuartosDisponiveis();
    }

    public List<Quarto> getTodosQuartos(RepositoryQuarto repoQuarto) {
        return repoQuarto.listarQuartos();
    }

    public double calcularLucroTotal(RepositoryReserva repoReserva, RepositoryUsuario repoUsuario) {
        double lucroBruto = repoReserva.listarReservas().stream()
                .mapToDouble(Reserva::getValorTotal)
                .sum();

        long numFuncionarios = repoUsuario.listarUsuarios().stream()
                .filter(u -> u instanceof Admin)
                .count();
        double custoFuncionarios = numFuncionarios * 50;

        return lucroBruto - custoFuncionarios;
    }
}
