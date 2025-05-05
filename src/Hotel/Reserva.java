package Hotel;

import Users.Cliente;
import Misc.HotelEnums.*;
import java.time.LocalDate;

public class Reserva {
    public enum Status {
        PASSIVA,
        ATIVA,
        CONCLUIDA,
        CANCELADA
    }

    private final int id;
    private final Cliente cliente;
    private final Quarto quarto;
    private final LocalDate dataCheckIn;
    private final LocalDate dataCheckOut;
    private double valorTotal;
    private Status status;
    private TipoServico servicoQuarto;
    private TipoTour tour;
    private boolean checkInRealizado;
    private boolean checkOutRealizado;

    public Reserva(int id, Cliente cliente, Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.cliente = cliente;
        this.quarto = quarto;
        this.dataCheckIn = checkIn;
        this.dataCheckOut = checkOut;
        this.status = Status.PASSIVA;
        this.checkInRealizado = false;
        this.checkOutRealizado = false;
        this.valorTotal = calcularValorTotal();
    }

    private double calcularValorTotal() {
        long dias = dataCheckIn.until(dataCheckOut).getDays();
        double total = dias * quarto.getPrecoDiaria();

        if (servicoQuarto != null) {
            total += dias * servicoQuarto.getValor();
        }

        if (tour != null) {
            total += tour.getValor();
        }

        return total;
    }

    public void realizarCheckIn() {
        if (status == Status.PASSIVA && !checkInRealizado && !checkOutRealizado) {
            checkInRealizado = true;
            status = Status.ATIVA;
            quarto.setDisponivel(false);
        }
    }

    public void realizarCheckOut() {
        if (checkInRealizado && !checkOutRealizado) {
            checkOutRealizado = true;
            quarto.setDisponivel(true);
            status = Status.CONCLUIDA;
        }
    }

    public void adicionarServicoQuarto(TipoServico servico) {
        this.servicoQuarto = servico;
        this.valorTotal = calcularValorTotal();
    }

    public void adicionarTour(TipoTour tour) {
        this.tour = tour;
        this.valorTotal = calcularValorTotal();
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isCheckInRealizado() {
        return checkInRealizado;
    }

    public boolean isCheckOutRealizado() {
        return checkOutRealizado;
    }

    public TipoServico getServicoQuarto() {
        return servicoQuarto;
    }

    public TipoTour getTour() {
        return tour;
    }

    public void setStatus(Status status) {
        if (status == Status.CONCLUIDA && !checkOutRealizado) {
            throw new IllegalStateException("Não é possível concluir reserva sem check-out");
        }
        this.status = status;
    }
}
