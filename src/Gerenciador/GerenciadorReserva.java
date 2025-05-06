package Gerenciador;

import Hotel.Reserva;
import java.util.List;

public interface GerenciadorReserva {
    void adicionarReserva(Reserva reserva);
    Reserva buscarReservaPorId(int id);
    boolean cancelarReserva(int id);
    boolean realizarCheckIn(int id);
    boolean realizarCheckOut(int id);
    List<Reserva> listarReservas();
    List<Reserva> listarReservasPorCliente(String cpf);
    List<Reserva> listarReservasPorStatus(Reserva.Status status);
    void atualizarReserva(Reserva reservaAtualizada);
}
