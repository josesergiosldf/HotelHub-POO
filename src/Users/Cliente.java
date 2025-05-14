package Users;

import Hotel.Reserva;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private final List<Reserva> reservas;
    private int pontos;

    public Cliente(String nome, String senha, String cpf, String email) {
        super(nome, senha, cpf, email);
        this.reservas = new ArrayList<>();
        this.pontos = 0;
    }

    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public int adicionarPontos(int pontos) {
        this.pontos += pontos;
        return this.pontos;
    }

    public Reserva getReservaAtiva() {
        return reservas.stream()
                .filter(r -> !r.isCheckOutRealizado())
                .findFirst()
                .orElse(null);
    }

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservas);
    }

    public int getPontos() {
        return pontos;
    }
}
