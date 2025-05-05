package Repository;

import Gerenciador.GerenciadorReserva;
import Hotel.Reserva;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class RepositoryReserva implements GerenciadorReserva {
    private final Map<Integer, Reserva> reservas = new HashMap<>();

    @Override
    public void adicionarReserva(Reserva novaReserva) {
        if (verificaConflitoReserva(novaReserva)) {
            throw new IllegalStateException("Já existe uma reserva ativa para esse quarto no período informado.");
        }
        reservas.put(novaReserva.getId(), novaReserva);
    }

    private boolean verificaConflitoReserva(Reserva novaReserva) {
        for (Reserva r : reservas.values()) {
            if (r.getQuarto().getNumero() == novaReserva.getQuarto().getNumero()
                    && r.getStatus() == Reserva.Status.ATIVA
                    && datasConflitam(r.getDataCheckIn(), r.getDataCheckOut(), novaReserva.getDataCheckIn(), novaReserva.getDataCheckOut())) {
                return true;
            }
        }
        return false;
    }

    private boolean datasConflitam(LocalDate inicio1, LocalDate fim1, LocalDate inicio2, LocalDate fim2) {
        return !(fim1.isBefore(inicio2) || fim2.isBefore(inicio1));
    }

    @Override
    public Reserva buscarReservaPorId(int id) {
        return reservas.get(id);
    }

    @Override
    public boolean cancelarReserva(int id) {
        Reserva reserva = reservas.get(id);

        if (reserva != null && reserva.getStatus() == Reserva.Status.ATIVA) {
            reserva.setStatus(Reserva.Status.CANCELADA);
            return true;
        }
        return false;
    }

    @Override
    public boolean realizarCheckIn(int id) {
        Reserva reserva = reservas.get(id);

        if (reserva != null && reserva.getStatus() == Reserva.Status.ATIVA && !reserva.isCheckInRealizado()) {
            reserva.realizarCheckIn();
            return true;
        }
        return false;
    }

    @Override
    public boolean realizarCheckOut(int id) {
        Reserva reserva = reservas.get(id);

        if (reserva != null && reserva.getStatus() == Reserva.Status.ATIVA && reserva.isCheckInRealizado()) {
            reserva.realizarCheckOut();
            return true;
        }
        return false;
    }

    @Override
    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas.values());
    }

    @Override
    public List<Reserva> listarReservasPorCliente(String cpf) {
        List<Reserva> lista = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r.getCliente().getCpf().equals(cpf)) {
                lista.add(r);
            }
        }
        return lista;
    }

    @Override
    public List<Reserva> listarReservasPorStatus(Reserva.Status status) {
        List<Reserva> lista = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r.getStatus() == status) {
                lista.add(r);
            }
        }
        return lista;
    }

    @Override
    public boolean atualizarReserva(Reserva reservaAtualizada) {
        if (reservaAtualizada == null) {
            return false;
        }

        Reserva reservaExistente = reservas.get(reservaAtualizada.getId());
        if (reservaExistente == null) {
            return false;
        }

        if (verificaConflitoAtualizacao(reservaExistente, reservaAtualizada)) {
            return false;
        }

        reservas.put(reservaAtualizada.getId(), reservaAtualizada);
        return true;
    }

    private boolean verificaConflitoAtualizacao(Reserva reservaOriginal, Reserva reservaAtualizada) {
        if (reservaOriginal.getQuarto().getNumero() == reservaAtualizada.getQuarto().getNumero() &&
                reservaOriginal.getDataCheckIn().equals(reservaAtualizada.getDataCheckIn()) &&
                reservaOriginal.getDataCheckOut().equals(reservaAtualizada.getDataCheckOut())) {
            return false;
        }

        for (Reserva r : reservas.values()) {
            if (r.getId() != reservaAtualizada.getId() && // Ignora a própria reserva
                    r.getQuarto().getNumero() == reservaAtualizada.getQuarto().getNumero() &&
                    r.getStatus() == Reserva.Status.ATIVA &&
                    datasConflitam(r.getDataCheckIn(), r.getDataCheckOut(),
                            reservaAtualizada.getDataCheckIn(), reservaAtualizada.getDataCheckOut())) {
                return true;
            }
        }
        return false;
    }
}
