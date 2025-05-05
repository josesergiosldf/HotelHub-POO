package Hotel;

import Misc.HotelEnums.*;

public class Quarto {
    private final int numero;
    private final TipoQuarto tipo;
    private boolean disponivel;

    public Quarto(int numero, TipoQuarto tipo) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número do quarto inválido");
        }
        this.numero = numero;
        this.tipo = tipo;
        this.disponivel = true;
    }

    public int getNumero() {
        return numero;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public double getPrecoDiaria() {
        return tipo.getValorDiaria();
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
